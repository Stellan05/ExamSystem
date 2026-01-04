package org.example.examsystem.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object answer;
    // 如果是选择题 -- 选项VO链表
    private List<OptionVO> options;
}
