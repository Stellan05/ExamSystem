package org.example.examsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 完成试卷编辑验证结果 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteExamEditResponse {
    /**
     * 是否通过验证
     */
    private Boolean isValid;

    /**
     * 验证消息
     */
    private String message;

    /**
     * 题目总数
     */
    private Integer totalQuestions;

    /**
     * 缺少标准答案的题目ID列表
     */
    private List<Long> questionsWithoutAnswer;

    /**
     * 缺少分数的题目ID列表
     */
    private List<Long> questionsWithoutScore;

    /**
     * 验证详情（可选，用于前端展示）
     */
    private List<String> details;

    /**
     * 是否已发布（验证通过后自动发布）
     */
    private Boolean published;
}

