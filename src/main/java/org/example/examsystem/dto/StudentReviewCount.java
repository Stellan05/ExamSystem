package org.example.examsystem.dto;


import lombok.Data;

/**
 * 标识已批阅学生
 */
@Data
public class StudentReviewCount {
    private Long studentExamId;
    private Integer reviewedCount;
}
