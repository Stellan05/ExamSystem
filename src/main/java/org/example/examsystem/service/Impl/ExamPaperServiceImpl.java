package org.example.examsystem.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.examsystem.entity.AnswerRecord;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.mapper.AnswerRecordMapper;
import org.example.examsystem.mapper.TesterExamMapper;
import org.example.examsystem.service.IService.IExamPaperService;
import org.example.examsystem.vo.QuestionDetailVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamPaperServiceImpl extends ServiceImpl<AnswerRecordMapper, AnswerRecord> implements IExamPaperService {

    private final AnswerRecordMapper answerRecordMapper;

    /**
     * 考生查看作答详细
     * @param examId 本场考试Id
     * @param userId 考生Id
     * @return 考试信息链表
     */
    @Override
    public List<QuestionDetailVO> getQuestionDetail(Long examId, Long userId) {
        return answerRecordMapper.getQuestionDetails(examId,userId);
    }
}
