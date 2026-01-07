package org.example.examsystem.vo;

import lombok.Data;

@Data
public class WrongQuestionWithAnswerVO {
    /**
     * 错题信息
     */
    private RandomWrongQuestionVO question;

    /**
     * 题目答案
     */
    private QuestionAnswerVO answer;
}
