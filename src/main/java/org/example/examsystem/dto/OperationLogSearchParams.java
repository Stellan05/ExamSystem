package org.example.examsystem.dto;

import lombok.Data;

/**
 * 操作日志搜索参数
 */
@Data
public class OperationLogSearchParams {
    /**
     * 关键词（搜索用户名、操作描述等）
     */
    private String keyword;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作类型
     */
    private String operationType;
}



