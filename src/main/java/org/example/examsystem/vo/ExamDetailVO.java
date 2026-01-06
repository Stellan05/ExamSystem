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
//    private Integer isPreviewed; // 0:未批阅完  1：已批阅完
}
