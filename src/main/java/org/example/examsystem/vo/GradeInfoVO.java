package org.example.examsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考生成绩列表VO
 */
@Data
public class GradeInfoVO {
    private Long userId;
    private String realName;
    private String email;
    private Double score;
    private Integer status;
    private LocalDateTime submitTime;
}
