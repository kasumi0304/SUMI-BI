package com.kasumi.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author kasumi
 * @Description: 文件上传请求
 */
@Data
public class GenChartByAiRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图表名称
     */
    private String name;

    /**
     * 目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;
}
