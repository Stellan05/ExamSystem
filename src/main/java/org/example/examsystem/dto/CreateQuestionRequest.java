package org.example.examsystem.dto;

import lombok.Data;

/**
 * 出题请求 DTO
 */
@Data
public class CreateQuestionRequest {
    /**
     * 出题人ID（后端从token中自动获取，前端无需传递）
     */
    private Long creatorId;
    /**
     * 考试ID（可选：不传则后端尝试自动关联）
     */
    private Long examId;
    /**
     * 分数（可选：默认0）
     */
    private Integer score;
    /**
     * 题型：1单选 2多选 3判断 4填空 5主观
     */
    private String questionType;
    /**
     * 题干内容
     */
    private String content;
    /**
     * 选项字符串，可用逗号或换行分隔；仅选择/判断题需要
     */
    private String options;
}
