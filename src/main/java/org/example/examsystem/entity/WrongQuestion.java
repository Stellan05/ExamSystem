package org.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("wrong_question")
public class WrongQuestion {
    @TableId
    private Integer id;

    /**
     * 关联学生ID
     */
    private Long studentId;

    /**
     * 关联题目Id
     */
    private Long questionId;

    /**
     * 累计错误次数
     */
    private Integer wrongCount;

    /**
     * 学生答案
     */
    private String studentAnswer;

    /**
     * 正确答案
     */
    private String correctAnswer;

    /**
     * 学生笔记
     */
    private String note;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;

}
