package org.example.examsystem.vo;

import lombok.Data;

/**
 * 试卷中单题详细（出题者视角）
 */
@Data
public class PaperQuestionDetailVO {
    /**
     * 题目基本信息（含分值、选项等）
     */
    private QuestionSimpleInfoVO question;

    /**
     * 标准答案
     */
    private String correctAnswer;

    /**
     * 答案解析
     */
    private String answerAnalysis;
}













