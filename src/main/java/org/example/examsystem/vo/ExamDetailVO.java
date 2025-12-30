package org.example.examsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.examsystem.entity.Exam;

import java.util.List;

/**
 * 考试详细VO
 */
@Data
@AllArgsConstructor
public class ExamDetailVO {
    private Exam exam;
    private List<QuestionDetailVO> questionDetailVOList;
}
