package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.examsystem.entity.AnswerRecord;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.vo.ProgressVO;
import org.example.examsystem.vo.QuestionDetailVO;
import org.example.examsystem.vo.TeacherReviewQuestionDetailVO;

import java.util.List;

public interface IExamPaperService extends IService<AnswerRecord> {

    List<QuestionDetailVO>  getQuestionDetail(Long examId,Long userId);

    List<Long> getQuestionIdByExamId(Long examId);

    TeacherReviewQuestionDetailVO getReviewQuestionDetail(Long examId, Long questionId, Long studentId);

    List<Long> getSubjectiveQuestionIds(Long examId);

    Boolean judge(Long examId,Long questionId,Long studentId,Integer score);

    ProgressVO getReviewProgress(Long examId);
}
