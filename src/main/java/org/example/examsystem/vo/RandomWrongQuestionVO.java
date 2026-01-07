package org.example.examsystem.vo;

import lombok.Data;
import java.util.List;

/**
 * 随机错题VO（不含答案）
 */
@Data
public class RandomWrongQuestionVO {
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
}

