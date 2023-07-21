package com.kasumi.dto.req;

import com.kasumi.core.common.req.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 * @author zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryReqDto extends PageRequest implements Serializable {

    /**
     * 图表名称
     */
    private String name;

    /**
     * id
     */
    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 用户 id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}