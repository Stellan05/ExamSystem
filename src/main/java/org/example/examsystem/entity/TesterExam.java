package org.example.examsystem.entity;
import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *  本场考试详细信息
 */
@Data
@TableName("tester_exam")
public class TesterExam {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 关联考试ID
    private Long examId;

    // 关联参加测试的学生ID
    private Long studentId;

    private LocalDateTime startTime;

    private LocalDateTime submitTime;

    /**
     * 持续时间（分钟）
     */
    private Integer duration;

    
    private Integer totalScore;

    // 答卷状态 0未开始 1作答中 2已提交
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
