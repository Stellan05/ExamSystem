package org.example.examsystem.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 创建试卷时包含的题目信息DTO
 */
@Data
public class QuestionInExamDTO {
    /**
     * 题型：1单选 2多选 3判断 4填空 5主观
     * 支持 questionType 和 type 两种字段名
     */
    @JsonAlias({"type", "questionType"})
    private String questionType;
    
    /**
     * 题干内容
     */
    private String content;
    
    /**
     * 选项列表（仅选择/判断题需要）
     * 支持 options 和 opinions 两种字段名
     * 使用 @JsonProperty 确保 options 是主字段名，@JsonAlias 支持其他别名
     */
    @JsonProperty("options")
    @JsonAlias({"opinions"})
    private List<String> options;
    
    /**
     * 分数
     */
    private Integer score;
    
    /**
     * 标准答案（支持 answer 和 correctAnswer 两种字段名）
     */
    @JsonAlias({"answer"})
    private String answer;
    
    /**
     * 答案解析（可选，支持 answerAnalysis 和 analysis 两种字段名）
     */
    @JsonAlias({"answerAnalysis", "analysis"})
    private String answerAnalysis;
}


