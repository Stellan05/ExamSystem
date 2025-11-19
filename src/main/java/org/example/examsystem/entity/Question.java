package org.example.examsystem.entity;
import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import java.util.Date;

/**
 * 题库及其答案
 */
@Data
@TableName("question")
public class Question {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 添加该题目的教师ID
    private Long creatorId;

    // 题目类型:1单选 2多选 3判断 4填空 5主观 6图片选择等
    private Integer questionType;

    // 题干
    private String content;

    // 选项
    private String optionsJson;

    // 标准答案
    private String answer;

    private Integer score;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Integer isDeleted;
}