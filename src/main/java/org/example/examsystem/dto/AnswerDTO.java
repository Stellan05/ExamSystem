package org.example.examsystem.dto;

import lombok.Data;

/**
 * 答案DTO
 */
@Data
public class AnswerDTO {

    private Long questionId;
    private Object answer; // String / List / null
}

