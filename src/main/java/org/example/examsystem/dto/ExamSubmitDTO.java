package org.example.examsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 交卷DTO
 */
@Data
public class ExamSubmitDTO {

    private Long examId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long submitTime;  // 时间戳
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer duration;

    private List<AnswerDTO> answers;
}

