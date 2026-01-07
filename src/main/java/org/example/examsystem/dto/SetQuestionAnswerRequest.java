package org.example.examsystem.dto;

import lombok.Data;

/**
 * 设置题目答案与解析的请求 DTO
 */
@Data
public class SetQuestionAnswerRequest {

    /**
     * 标准答案
     */
    private String correctAnswer;

    /**
     * 答案解析
     */
    private String answerAnalysis;
}




















