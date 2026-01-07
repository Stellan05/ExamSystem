package org.example.examsystem.dto;

import lombok.Data;

/**
 * 修改试卷/考试基本信息请求 DTO（前端按需传字段，未传则不修改）
 */
@Data
public class UpdateExamBasicInfoRequest {
    /**
     * 考试名称
     */
    private String examName;

    /**
     * 考试说明/描述
     */
    private String description;

    /**
     * 开始日期：yyyy-MM-dd（可选，但与 startTime 配套使用）
     */
    private String startDate;

    /**
     * 开始时间：HH:mm 或 HH:mm:ss（可选，但与 startDate 配套使用）
     */
    private String startTime;

    /**
     * 考试时长（分钟）
     */
    private Integer duration;

    /**
     * 考试结束后是否展示答案
     */
    private Boolean showAnswers;
}




















