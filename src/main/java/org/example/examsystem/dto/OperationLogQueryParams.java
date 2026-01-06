package org.example.examsystem.dto;

import lombok.Data;

/**
 * 操作日志查询参数
 */
@Data
public class OperationLogQueryParams {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}



