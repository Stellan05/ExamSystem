package org.example.examsystem.dto;

import lombok.Data;

@Data
public class QuestionScoreDTO {
    private Long questionId;
    private Double userScore;
}
