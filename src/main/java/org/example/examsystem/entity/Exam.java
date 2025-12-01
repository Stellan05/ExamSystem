package org.example.examsystem.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("exam")
public class Exam {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String examName;

    /**
     * 考试码
     */
    private Integer examCode;

    /**
     *  创建者ID--关联教师ID
     */
    private Long creatorId;

    private String description;

    private Date startTime;

    private Date endTime;

    private Integer limitMinutes;

    /**
     * 考试状态:0未开始 1进行中 2已结束
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}