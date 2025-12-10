package org.example.examsystem.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.examsystem.dto.StudentReviewCount;
import org.example.examsystem.entity.AnswerRecord;
import org.example.examsystem.entity.ExamQuestion;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.mapper.AnswerRecordMapper;
import org.example.examsystem.mapper.ExamQuestionMapper;
import org.example.examsystem.mapper.TesterExamMapper;
import org.example.examsystem.service.IService.IExamPaperService;
import org.example.examsystem.vo.ProgressVO;
import org.example.examsystem.vo.QuestionDetailVO;
import org.example.examsystem.vo.TeacherReviewQuestionDetailVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamPaperServiceImpl extends ServiceImpl<AnswerRecordMapper, AnswerRecord> implements IExamPaperService {

    private final AnswerRecordMapper answerRecordMapper;
    private final ExamQuestionMapper examQuestionMapper;
    private final TesterExamMapper  testerExamMapper;

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

    /**
     * 获取本次考试题目Id --- 主观题
     * @param examId 考试Id
     * @return 题目链表
     */
    @Override
    public List<Long> getQuestionIdByExamId(Long examId) {
        return new LambdaQueryChainWrapper<>(examQuestionMapper)
                .eq(ExamQuestion::getExamId, examId)
                .eq(ExamQuestion::getIsDeleted, 0)
                .orderByAsc(ExamQuestion::getSort)
                .list()
                .stream()
                .map(ExamQuestion::getQuestionId)
                .toList();
    }

    /**
     * 获取本次考试需要人工批阅的题目Id（主观题）
     * @param examId 考试ID
     * @return 题目列表
     */
    @Override
    public List<Long> getSubjectiveQuestionIds(Long examId) {
        return examQuestionMapper.getSubjectiveQuestionIds(examId);
    }

    /**
     *  批阅时查询主观题详细
     * @param examId 考试ID
     * @param questionId 题目ID
     * @param studentId 考生ID
     * @return VO
     */
    @Override
    public TeacherReviewQuestionDetailVO getReviewQuestionDetail(Long examId, Long questionId, Long studentId) {
        return answerRecordMapper.getTeacherReviewDetail(examId,questionId,studentId);
    }

    /**
     * @param examId 考试ID
     * @param questionId 问题ID
     * @param studentId 考生ID
     * @param score 分数
     * @return success
     */
    @Override
    public Boolean judge(Long examId, Long questionId, Long studentId,Integer score) {
        return null;
    }

    /**
     * 获取评卷进度
     * @param examId 考试Id
     * @return ProgressVO
     */
    @Override
    public ProgressVO getReviewProgress(Long examId) {
        // 1. 总学生数
        long totalStudents = testerExamMapper.selectCount(
                Wrappers.<TesterExam>lambdaQuery()
                        .eq(TesterExam::getExamId, examId)
                        .eq(TesterExam::getIsDeleted, 0)
        );

        // 说明本次考试都是客观题
        if (totalStudents == 0) {
            return new ProgressVO(0, 0);
        }

        // 2. 考试中主观题数量（question_type = 5）
        Integer subjectiveCount = answerRecordMapper.countSubjectiveQuestions(examId);
        if (subjectiveCount == null || subjectiveCount == 0) {
            return new ProgressVO(totalStudents, totalStudents);
        }

        // 3. 每个学生已批阅的主观题数
        List<StudentReviewCount> reviewList = answerRecordMapper.countReviewedPerStudent(examId);

        // 4. 已批阅学生：已批主观题数 == 总主观题数
        long reviewedStudents = reviewList.stream()
                .filter(rc -> rc.getReviewedCount() != null && rc.getReviewedCount() == subjectiveCount)
                .count();

        return new ProgressVO(reviewedStudents, totalStudents);
    }
}
