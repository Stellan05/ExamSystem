package org.example.examsystem.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
// 错题VO 返回给前端
public class WrongQuestionVO {
    // 错题字段
    private Integer wrongQuestionId;
    private Long studentId;
    private Long questionId;
    private Integer count;
    private String studentAnswer;
    private String correctAnswer;
    private String note;
    private Date createTime;
    private Date updateTime;

    // 原题目字段
    private Integer questionType;
    private String content;
    private String optionsJson;
}
