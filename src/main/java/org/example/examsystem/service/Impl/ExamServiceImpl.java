package org.example.examsystem.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.dto.CreateExamRequest;
import org.example.examsystem.mapper.*;
import org.example.examsystem.service.IService.IExamService;
import org.example.examsystem.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    /**
     * 创建考试并绑定题目
     */
    @Override
    public Result createExam(CreateExamRequest request) {
        if (!StringUtils.hasText(request.getExamName())) {
            return Result.fail("考试名称不能为空");
        }
        if (!StringUtils.hasText(request.getExamCode())) {
            return Result.fail("考试码不能为空");
        }
        if (request.getCreatorId() == null) {
            return Result.fail("创建者ID不能为空");
        }
        if (request.getDuration() == null || request.getDuration() <= 0) {
            return Result.fail("考试时长必须大于0");
        }
        Integer examCodeInt;
        try {
            examCodeInt = Integer.valueOf(request.getExamCode());
        } catch (NumberFormatException ex) {
            return Result.fail("考试码必须为六位数字");
        }
        if (request.getExamCode().length() != 6) {
            return Result.fail("考试码必须为六位数字");
        }

        LocalDateTime start;
        try {
            start = LocalDateTime.of(LocalDate.parse(request.getStartDate()), LocalTime.parse(request.getStartTime()));
        } catch (Exception ex) {
            return Result.fail("开始时间格式错误，应为 yyyy-MM-dd 与 HH:mm:ss");
        }

        // 唯一性：考试码不能重复
        Long exists = examMapper.selectCount(Wrappers.<Exam>lambdaQuery()
                .eq(Exam::getExamCode, examCodeInt)
                .eq(Exam::getIsDeleted, 0));
        if (exists != null && exists > 0) {
            return Result.info(409, "考试码已存在");
        }

        Exam exam = new Exam();
        exam.setExamName(request.getExamName());
        exam.setExamCode(examCodeInt);
        exam.setCreatorId(request.getCreatorId());
        exam.setDescription(request.getDescription());
        exam.setLimitMinutes(request.getDuration());
        exam.setPaperShow(Boolean.TRUE.equals(request.getShowAnswers()));
        exam.setStartTime(start);
        exam.setEndTime(start.plusMinutes(request.getDuration()));
        exam.setStatus(0);
        examMapper.insert(exam);

        Map<String, Object> data = new HashMap<>();
        data.put("examId", exam.getId());
        data.put("examCode", exam.getExamCode());
        return Result.ok(data);
    }
}
