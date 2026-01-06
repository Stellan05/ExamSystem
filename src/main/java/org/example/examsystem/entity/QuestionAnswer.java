package org.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@TableName("question_answer")
@Data
public class QuestionAnswer {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联问题ID
     */
    private Long questionId;

    /**
     * 正确答案
     */
    private String correctAnswer;

    /**
     * 答案解析
     */
    @TableField("answer_analysis")
    private String answerAnalysis;

    /**
     * 本题解析
     */
    private String answerAnalysis;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
