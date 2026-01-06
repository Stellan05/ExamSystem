package org.example.examsystem.dto;

import lombok.Data;

import java.util.List;

/**
 * 创建试卷/考试请求
 */
@Data
public class CreateExamRequest {
    /**
     * 考试码（六位数字字符串，后端会转换为整数）
     */
    private String examCode;
    private String examName;
    private String description;
    /**
     * 创建者ID（后端从token中自动获取，前端无需传递）
     */
    private Long creatorId;
    /**
     * 开始日期，格式建议：yyyy-MM-dd
     */
    private String startDate;
    /**
     * 开始时间，格式建议：HH:mm 或 HH:mm:ss
     */
    private String startTime;
    /**
     * 考试时长，单位：分钟
     */
    private Integer duration;
    /**
     * 考试结束后是否展示答案
     */
    private Boolean showAnswers;
    /**
     * 题目列表（可选，如果提供则一次性创建整张试卷）
     */
    private List<QuestionInExamDTO> questions;
}
