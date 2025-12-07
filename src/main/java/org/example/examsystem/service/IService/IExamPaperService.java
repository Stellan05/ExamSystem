package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.examsystem.entity.AnswerRecord;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.vo.QuestionDetailVO;

import java.util.List;

public interface IExamPaperService extends IService<AnswerRecord> {
    List<QuestionDetailVO>  getQuestionDetail(Long examId,Long userId);
}
