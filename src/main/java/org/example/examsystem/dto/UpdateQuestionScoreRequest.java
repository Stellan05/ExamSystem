package org.example.examsystem.dto;

import lombok.Data;

/**
 * 修改题目在某场考试中的分数请求
 */
@Data
public class UpdateQuestionScoreRequest {
    /**
     * 考试ID（必填）
     */
    private Long examId;

    /**
     * 分数（必填，>=0）
     */
    private Integer score;
}




















