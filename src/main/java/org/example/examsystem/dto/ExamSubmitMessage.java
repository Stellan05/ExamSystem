package org.example.examsystem.dto;

import lombok.Data;

import java.util.List;

/**
 * 交卷入redis缓存
 */
@Data
public class ExamSubmitMessage {
    private Long examId;
    private Long userId;          // 从 token 注入
    private Long attemptId;       // tester_exam ID 即作答时的id
    private Long submitTime;      // 时间戳
    private Boolean finalSubmit; // 区分是暂存还是交卷

    private List<AnswerDTO> answers;
}
