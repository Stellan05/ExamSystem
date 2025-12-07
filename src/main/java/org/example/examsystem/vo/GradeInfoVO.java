package org.example.examsystem.vo;

import lombok.Data;

/**
 * 考生成绩列表VO
 */
@Data
public class GradeInfoVO {
    private Long userId;
    private String realName;
    private Double score;
}
