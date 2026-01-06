package org.example.examsystem.dto;

import lombok.Data;

import java.util.List;

/**
 * 评卷DTO
 */
@Data
public class JudgeRequest {
    private Long testerId;
    private List<QuestionScoreDTO> questions;

}
