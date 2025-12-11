package org.example.examsystem.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.mapper.*;
import org.example.examsystem.service.IService.IExamService;
import org.example.examsystem.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl extends ServiceImpl<ExamMapper,Exam> implements IExamService {

    private final ExamMapper examMapper;
    private final TesterExamMapper testerExamMapper;
    private final AnswerRecordMapper answerRecordMapper;
    private final QuestionMapper questionMapper;

    /**
     * 用户获取自己 参加过的考试
     * @param userId 用户Id
     * @return 考试列表
     */
    @Override
    public Page<?> getTesterExams(Long userId, Integer page, Integer pageSize) {
        // 参与者查询
            Page<TesterExamInfoVO> examPage = new Page<>(page,pageSize);
            return testerExamMapper.getExamsByTesterId(examPage,userId);
    }

    /**
     * 用户获取自己 发布过的考试
     * @param userId 用户Id
     * @return 考试列表
     */
    @Override
    public Page<?> getCreatorExams(Long userId, Integer page, Integer pageSize) {
        Page<CreatorExamInfoVO> pages = testerExamMapper.getExamsByCreatorId(
                new Page<>(page, pageSize), userId);
        for(CreatorExamInfoVO vo : pages.getRecords()){
            List<QuestionDetailVO> questionDetailVOS = answerRecordMapper.getQuestionDetails(vo.getExamId(),userId);
            vo.setQuestionDetailVOList(questionDetailVOS);
        }
        return pages;
    }

    /**
     * 查询某一考试下的考试参加人数（出题者）
     * @param examId 考试Id
     * @return 参与人数
     */
    @Override
    public Long getAllCount(Long examId) {
        return testerExamMapper.selectCount(
                Wrappers.<TesterExam>lambdaQuery()
                        .eq(TesterExam::getExamId,examId)
                        .eq(TesterExam::getIsDeleted,0)
        );
    }

    /**
     * 获取考生在本场考试的排名情况
     * @param examId 考试Id
     * @param userId 考试Id
     * @return rank、total
     */
    @Override
    public RankInfoVO getMyRank(Long examId, Long userId) {
        return testerExamMapper.getRank(examId,userId);
    }

    /**
     * 获取参与本场考试的所有参与者
     * @param examId 用户ID
     * @param page 当前页
     * @param pageSize 页尺寸
     * @return 参与者链表
     */
    @Override
    public Page<UserSimpleInfoVO> getAllTesters(Long examId, Integer page, Integer pageSize) {
        Page<UserSimpleInfoVO> pageVO = new Page<>(page, pageSize);
        return testerExamMapper.getAllTestersByPage(pageVO,examId);
    }

    /**
     * 根据考试Id查询题目链表
     * @param examId 考试Id
     * @return 试题链表
     */
    @Override
    public List<QuestionSimpleInfoVO> getQuestions(Long examId) {
        return questionMapper.getQuestions(examId);
    }
}
