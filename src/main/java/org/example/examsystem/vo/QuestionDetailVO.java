package org.example.examsystem.vo;

import lombok.Data;

import java.util.List;

/**
 * 每一题详细信息VO---用于整卷浏览
 */
@Data
public class QuestionDetailVO {

    // 题目基本信息
    QuestionSimpleInfoVO questionSimpleInfoVO;

    // 正确答案
    private String correctAnswer;

    // 考生提交的答案
    private String userAnswer;

    // 该题得分
    private Double userScore;
}

