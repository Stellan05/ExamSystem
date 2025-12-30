package org.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("exam_abnormal_behavior")
public class ExamAbnormalBehavior {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 考试ID
     */
    private Long examId;

    /**
     * 考生ID
     */
    private Long userId;

    /**
     * 本次作答ID
     */
    private Long attemptId;

    /**
     * 异常行为类型
     */
    private String behaviorType;

    /**
     * 异常发生时间
     */
    private LocalDateTime occurTime;

    /**
     * 补充说明
     */
    private String remark;
}
