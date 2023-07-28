package com.kasumi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kasumi.core.common.resp.RestResp;
import com.kasumi.dao.entity.Chart;
import com.kasumi.dto.req.ChartEditReqDto;
import com.kasumi.dto.req.ChartQueryReqDto;
import com.kasumi.dto.req.DeleteReqDto;
import com.kasumi.dto.req.GenChartByAiReqDto;
import com.kasumi.dto.resp.BiRespDto;
import com.kasumi.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    /**
     * 图表信息删除接口
     *
     * @param deleteReqDto
     * @return
     */
    @PostMapping("/delete")
    public RestResp<Boolean> deleteChart(@RequestBody DeleteReqDto deleteReqDto) {
        return chartService.deleteChart(deleteReqDto);
    }

    /**
     * 根据 id 获取图表信息接口
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public RestResp<Chart> getChartById(long id) {
        return chartService.getChartById(id);
    }

    /**
     * 分页获取当前用户创建的图表信息列表接口
     *
     * @param chartQueryReqDto
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public RestResp<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryReqDto chartQueryReqDto,
                                                   HttpServletRequest request) {
        return chartService.listMyChartByPage(chartQueryReqDto, request);
    }


    /**
     * 编辑图表信息接口
     *
     * @param chartEditReqDto
     * @return
     */
    @PostMapping("/edit")
    public RestResp<Boolean> editChart(@RequestBody ChartEditReqDto chartEditReqDto) {
        return chartService.editChart(chartEditReqDto);
    }


    /**
     * 智能分析接口
     *
     * @param multipartFile
     * @param genChartByAiReqDto
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public RestResp<BiRespDto> genChart(@RequestPart("file") MultipartFile multipartFile,
                                        GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request) {


        return chartService.genChartByAi(multipartFile, genChartByAiReqDto, request);

    }


    /**
     * 智能分析接口（异步）
     *
     * @param multipartFile
     * @param genChartByAiReqDto
     * @param request
     * @return
     */
    @PostMapping("/gen/async")
    public RestResp<BiRespDto> genChartByAiASync(@RequestPart("file") MultipartFile multipartFile,
                                                 GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request) {
        return chartService.genChartByAiASync(multipartFile, genChartByAiReqDto, request);

    }

    /**
     * 智能分析接口（异步消息队列）
     *
     * @param multipartFile
     * @param genChartByAiReqDto
     * @param request
     * @return
     */
    @PostMapping("/gen/async/mq")
    public RestResp<BiRespDto> genChartByAiAsyncMq(@RequestPart("file") MultipartFile multipartFile,
                                                   GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request) {
        return chartService.genChartByAiAsyncMq(multipartFile, genChartByAiReqDto, request);
    }


}
