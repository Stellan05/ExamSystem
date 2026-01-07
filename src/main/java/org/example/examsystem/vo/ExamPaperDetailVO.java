package org.example.examsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷详细（出题者视角）
 */
@Data
public class ExamPaperDetailVO {
    private Long examId;
    private String examName;
    private Integer examCode;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer limitMinutes;
    private Integer status;
    private Boolean showAnswers;

    private List<PaperQuestionDetailVO> questions;
}

















