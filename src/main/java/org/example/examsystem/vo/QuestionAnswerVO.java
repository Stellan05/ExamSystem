package org.example.examsystem.vo;

import lombok.Data;

/**
 * 题目答案VO
 */
@Data
public class QuestionAnswerVO {
    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 标准答案（单选返回 "B"，多选返回 "A,C"，判断返回 "对"/"错"，填空返回答案文本，主观题可能为空）
     */
    private String correctAnswer;

    /**
     * 答案解析（可选）
     */
    private String analysis;
}

