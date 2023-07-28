package com.kasumi.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kasumi.core.api.OpenAiApi;
import com.kasumi.core.common.constant.ErrorCodeEnum;
import com.kasumi.core.common.exception.BusinessException;
import com.kasumi.core.common.exception.ThrowUtils;
import com.kasumi.core.common.resp.RestResp;
import com.kasumi.core.constant.AiConstant;
import com.kasumi.core.constant.ChartConstant;
import com.kasumi.core.constant.CommonConstant;
import com.kasumi.core.mq.BiMessageProducer;
import com.kasumi.core.utils.ExcelUtils;
import com.kasumi.core.utils.SqlUtils;
import com.kasumi.dao.entity.Chart;
import com.kasumi.dao.entity.User;
import com.kasumi.dto.req.ChartEditReqDto;
import com.kasumi.dto.req.ChartQueryReqDto;
import com.kasumi.dto.req.DeleteReqDto;
import com.kasumi.dto.req.GenChartByAiReqDto;
import com.kasumi.dto.resp.BiRespDto;
import com.kasumi.manager.AiManager;
import com.kasumi.manager.RedisLimiterManager;
import com.kasumi.service.ChartService;
import com.kasumi.dao.mapper.ChartMapper;
import com.kasumi.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
        implements ChartService {

    @Resource
    private UserService userService;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private OpenAiApi openAiApi;

    @Resource
    private AiManager aiManager;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private BiMessageProducer biMessageProducer;

    private final static String GEN_CHART_BY_AI = "genChartByAi_";


    @Override
    public RestResp<Boolean> deleteChart(DeleteReqDto deleteReqDto) {
        if (deleteReqDto == null || deleteReqDto.getId() <= 0) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        long id = deleteReqDto.getId();
        // 判断是否存在
        Chart chart = this.getById(id);
        ThrowUtils.throwIf(chart == null, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        boolean b = this.removeById(id);
        return RestResp.success(b);
    }

    @Override
    public RestResp<Page<Chart>> listMyChartByPage(ChartQueryReqDto chartQueryReqDto, HttpServletRequest request) {
        if (chartQueryReqDto == null) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        chartQueryReqDto.setUserId(loginUser.getId());
        long current = chartQueryReqDto.getCurrent();
        long size = chartQueryReqDto.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        Page<Chart> chartPage = this.page(new Page<>(current, size),
                getQueryWrapper(chartQueryReqDto));
        return RestResp.success(chartPage);
    }

    @Override
    public RestResp<Boolean> editChart(ChartEditReqDto chartEditReqDto) {
        if (chartEditReqDto == null || chartEditReqDto.getId() <= 0) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        long id = chartEditReqDto.getId();
        // 判断是否存在
        Chart oldChart = this.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditReqDto, chart);
        boolean result = this.updateById(chart);
        return RestResp.success(result);
    }

    @Override
    public RestResp<BiRespDto> genChartByAi(MultipartFile multipartFile, GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request) {
        String name = genChartByAiReqDto.getName();
        String goal = genChartByAiReqDto.getGoal();
        String chartType = genChartByAiReqDto.getChartType();

        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "分析目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);

        //  校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();

        //  校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "文件超过 1M");

        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx", "xls");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "文件后缀非法");

        User loginUser = userService.getLoginUser(request);

        // 每个用户一个限流器
        redisLimiterManager.doRateLimit(GEN_CHART_BY_AI + loginUser.getId());


        //  构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        //  压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");

        String result = openAiApi.doChat(userInput.toString());
        String[] splits = result.split("【【【【【");
        if (request == null) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }

        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setStatus(ChartConstant.SUCCEED);
        chart.setUserId(loginUser.getId());
        boolean saveResult = this.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCodeEnum.SYSTEM_ERROR);

        BiRespDto biRespDto = new BiRespDto();
        biRespDto.setGenChart(genChart);
        biRespDto.setGenResult(genResult);
        biRespDto.setChartId(chart.getId());
        return RestResp.success(biRespDto);
    }

    @Override
    public RestResp<BiRespDto> genChartByAiASync(MultipartFile multipartFile, GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request) {
        String name = genChartByAiReqDto.getName();
        String goal = genChartByAiReqDto.getGoal();
        String chartType = genChartByAiReqDto.getChartType();
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "分析目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);

        //  校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        //  校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx", "xls");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "文件后缀非法");

        User loginUser = userService.getLoginUser(request);

        // 每个用户一个限流器
        redisLimiterManager.doRateLimit(GEN_CHART_BY_AI + loginUser.getId());

        //  构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        //  压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");

        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus("wait");
        chart.setUserId(loginUser.getId());
        boolean saveResult = this.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCodeEnum.SYSTEM_ERROR, "图表保存失败");

        CompletableFuture.runAsync(() -> {
            // 先修改图表任务状态为 “执行中”，等任务成功后，修改为 ”已完成“,保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
            Chart updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus(ChartConstant.RUNNING);
            boolean b = this.updateById(updateChart);
            if (!b) {
                handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
                return;
            }
            String result = aiManager.doChat(AiConstant.BI_MODEL_ID, userInput.toString());
            String[] splits = result.split("【【【【【");
            if (splits.length < 3) {
                handleChartUpdateError(chart.getId(), "AI 生成错误");
                return;
            }
            String genChart = splits[1].trim();
            String genResult = splits[2].trim();
            Chart updateChartResult = new Chart();
            updateChartResult.setId(chart.getId());
            updateChartResult.setGenChart(genChart);
            updateChartResult.setGenResult(genResult);
            updateChartResult.setStatus(ChartConstant.SUCCEED);
            boolean updateResult = this.updateById(updateChartResult);
            if (!updateResult) {
                handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
            }

        }, threadPoolExecutor);


        BiRespDto biRespDto = new BiRespDto();
        biRespDto.setChartId(chart.getId());
        return RestResp.success(biRespDto);
    }

    @Override
    public RestResp<BiRespDto> genChartByAiAsyncMq(MultipartFile multipartFile, GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request) {
        String name = genChartByAiReqDto.getName();
        String goal = genChartByAiReqDto.getGoal();
        String chartType = genChartByAiReqDto.getChartType();
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "分析目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        // 校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx", "xls");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "文件后缀非法");

        User loginUser = userService.getLoginUser(request);
        // 限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit(GEN_CHART_BY_AI + loginUser.getId());

        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);

        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus(ChartConstant.WAIT);
        chart.setUserId(loginUser.getId());
        boolean saveResult = this.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCodeEnum.SYSTEM_ERROR, "图表保存失败");
        long newChartId = chart.getId();
        biMessageProducer.sendMessage(String.valueOf(newChartId));
        BiRespDto biRespDto = new BiRespDto();
        biRespDto.setChartId(newChartId);

        return RestResp.success(biRespDto);
    }


    @Override
    public RestResp<Chart> getChartById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        Chart chart = this.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND_ERROR);
        }
        return RestResp.success(chart);
    }

    /**
     * 获取查询包装类
     *
     * @param chartQueryReqDto
     * @return
     */
    private QueryWrapper<Chart> getQueryWrapper(ChartQueryReqDto chartQueryReqDto) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryReqDto == null) {
            return queryWrapper;
        }
        Long id = chartQueryReqDto.getId();
        String name = chartQueryReqDto.getName();
        String goal = chartQueryReqDto.getGoal();
        String chartType = chartQueryReqDto.getChartType();
        Long userId = chartQueryReqDto.getUserId();
        String sortField = chartQueryReqDto.getSortField();
        String sortOrder = chartQueryReqDto.getSortOrder();

        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage("execMessage");
        boolean updateResult = this.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }
}




