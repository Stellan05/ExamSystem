package org.example.examsystem.vo;

import lombok.Data;
import java.util.List;

@Data
public class WrongQuestionWithAnswerVO {
    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 题干内容
     */
    private String content;

    /**
     * 题目类型（1-单选，2-多选，3-判断，4-填空，5-主观）
     */
    private Integer questionType;

    /**
     * 选项列表（仅选择题和判断题有，其他题型为空列表）
     */
    private List<OptionVO> options;

    /**
     * 标准答案（单选返回 "B"，多选返回 "A,C"，判断返回 "对"/"错"，填空返回答案文本，主观题可能为空）
     */
    private String correctAnswer;

    /**
     * 答案解析（可选）
     */
    private String analysis;
}
