package com.kasumi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kasumi.core.common.resp.RestResp;
import com.kasumi.dao.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kasumi.dto.req.ChartEditReqDto;
import com.kasumi.dto.req.ChartQueryReqDto;
import com.kasumi.dto.req.DeleteReqDto;
import com.kasumi.dto.req.GenChartByAiReqDto;
import com.kasumi.dto.resp.BiRespDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ChartService extends IService<Chart> {

    /**
     * 智能分析
     * @param multipartFile
     * @param genChartByAiReqDto
     * @param request
     * @return
     */
    RestResp<BiRespDto> genChartByAi(MultipartFile multipartFile, GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request);

    /**
     * 图表消息删除
     * @param deleteReqDto
     * @return
     */
    RestResp<Boolean> deleteChart(DeleteReqDto deleteReqDto);

    /**
     * 分页获取当前用户创建的图表信息列表
     * @param chartQueryReqDto
     * @param request
     * @return
     */
    RestResp<Page<Chart>> listMyChartByPage(ChartQueryReqDto chartQueryReqDto, HttpServletRequest request);

    /**
     * 编辑图表
     * @param chartEditReqDto
     * @return
     */
    RestResp<Boolean> editChart(ChartEditReqDto chartEditReqDto);

    /**
     * 智能分析（异步）
     * @param multipartFile
     * @param genChartByAiReqDto
     * @param request
     * @return
     */
    RestResp<BiRespDto> genChartByAiASync(MultipartFile multipartFile, GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request);

    /**
     * 智能分析（消息队列）
     * @param multipartFile
     * @param genChartByAiReqDto
     * @param request
     * @return
     */
    RestResp<BiRespDto> genChartByAiAsyncMq(MultipartFile multipartFile, GenChartByAiReqDto genChartByAiReqDto, HttpServletRequest request);

    /**
     * 根据 id 获取图表信息
     * @param id
     * @return
     */
    RestResp<Chart> getChartById(long id);
}
