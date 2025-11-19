package org.example.examsystem.entity;
import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import java.util.Date;

/**
 *  答卷表
 */
@Data
@TableName("student_exam")
public class StudentExam {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 关联考试ID
    private Long examId;

    // 关联参加测试的学生ID
    private Long studentId;

    private Date startTime;

    private Date submitTime;

    private Integer duration;

    private Integer totalScore;

    // 答卷状态 0未开始 1作答中 2已提交
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Integer isDeleted;
}
