package org.example.examsystem.entity;
import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import java.util.Date;

/**
 * 学生答案表
 */
@Data
@TableName("answer_record")
public class AnswerRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 关联学生参与的考试ID
    private Long studentExamId;

    // 关联相关问题ID
    private Long questionId;

    private String studentAnswer;

    // 客观题系统自动识别判分
    private Integer autoScore;

    // 主观题教师评分
    private Integer teacherScore;

    private Integer finalScore;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Integer isDeleted;
}
