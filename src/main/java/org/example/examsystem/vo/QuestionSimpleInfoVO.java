package org.example.examsystem.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 试题VO类
 */
@Data
public class QuestionSimpleInfoVO {
    private Long questionId;
    private Integer type;
    private String content;
    private Double score;

    // 如果是选择题 -- 选项VO链表
    private List<OptionVO> options;

    /**
     * 答案解析（支持 analysis 和 answerAnalysis 两种字段名）
     */
    @JsonProperty("analysis")
    private String answerAnalysis;
}
