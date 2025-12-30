package org.example.examsystem.dto;

import lombok.Data;

import java.util.List;

/**
 * 交卷DTO
 */
@Data
public class ExamSubmitDTO {

    private Long examId;
    private Long submitTime;  // 时间戳
    private Integer duration;

    private List<AnswerDTO> answers;
}

