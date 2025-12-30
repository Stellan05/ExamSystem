package org.example.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.dto.AbnormalBehaviorDTO;
import org.example.examsystem.dto.ExamSubmitDTO;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.mapper.AnswerRecordMapper;
import org.example.examsystem.mapper.ExamMapper;
import org.example.examsystem.mapper.TesterExamMapper;
import org.example.examsystem.service.IService.IExamPaperService;
import org.example.examsystem.service.IService.IExamService;
import org.example.examsystem.service.IService.IGradeService;
import org.example.examsystem.service.Impl.AbnormalBehaviorServiceImpl;
import org.example.examsystem.service.Impl.ExamPaperServiceImpl;
import org.example.examsystem.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 考试相关模块controller
 */
@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ExamController {

    private final IExamService examService;
    private final ExamMapper examMapper;
    private final AnswerRecordMapper answerRecordMapper;
    private final IGradeService gradeService;
    private final TesterExamMapper testerExamMapper;
    private final AbnormalBehaviorServiceImpl abnormalBehaviorService;
    private final ExamPaperServiceImpl examPaperService;

    /**
     * 分页查询参加本场考试人员信息
     * @param examId 本场考试Id
     * @param page 当前页
     * @param size 页尺寸
     * @return 人员信息VO类
     */
    @GetMapping("/{examId}/testers")
    public Result getAllTest(
            @PathVariable("examId") long examId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        return Result.ok(examService.getAllTesters(examId,page,size));
    }

    /**
     * 用户获取考试信息
     * 考生获取参加过的考试
     * @return exam记录分页
     */
    @PostMapping("/my-exam/tester")
    public Result getTesterExams(

            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if(examService.getTesterExams(3L,page,size)!=null){
            return Result.ok(examService.getTesterExams(3L,page,size));
        }else{
            return Result.fail("错误参数");
        }
    }

    @GetMapping("/my-exam/creator")
    public Result getCreatorExam(
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size){
        val result = examService.getCreatorExams(2L, page, size);

        if(result!=null){
            return Result.ok(result);
        }else{
            return Result.fail("错误参数");
        }
    }

    /**
     * 获取本场考试详细信息，即整卷浏览
     * @param examId 考试ID
     * @param userId 用户ID
     * @return 题目及其作答情况
     */
    @GetMapping("my-exam/details/{examId}")
    public Result getMyExamDetails(
            @PathVariable("examId") Long examId,
            @RequestParam Long userId
    ){
        TesterExam testerExam = testerExamMapper.selectOne(
                new LambdaQueryWrapper<TesterExam>()
                        .eq(TesterExam::getExamId, examId)
                        .eq(TesterExam::getStudentId, userId)
        );
        if(testerExam==null){
            return Result.info(404,"未找到该考试记录");
        }
        List<QuestionDetailVO> questions = answerRecordMapper.getQuestionDetails(examId,userId);
        Exam exam = examMapper.selectById(examId);
        ExamDetailVO examDetailVO = new ExamDetailVO(exam,questions);

        return Result.ok(examDetailVO);
    }

    /**
     * 用户输入考试码加入考试 （创建关联表）
     * 并获取题目
     * @return 题目链表
     */
    @PostMapping("/check")
    public Result checkExam(
            @RequestParam String code
    ){
        if(code.length()!=6){
            return Result.fail("考试码必须为6位");
        }
        // 查询考试
        LambdaQueryWrapper<Exam>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getExamCode,code);
        Exam exam =  examMapper.selectOne(queryWrapper);
        if(exam==null){
            return Result.info(404,"未找到该考试！");
        }
        ExamSimpleInfoVO examSimpleInfoVO = new ExamSimpleInfoVO();
        BeanUtils.copyProperties(exam,examSimpleInfoVO);
        examSimpleInfoVO.setExamId(exam.getId());
        return Result.ok(examSimpleInfoVO);
    }

    /**
     * 点击确认后开始考试
     * @param examId 考试ID
     * @param userId 用户ID
     * @return 题目链表
     */
    @Transactional
    @PostMapping("/{examId}/start")
    public Result startExam(
            @PathVariable("examId") Long examId,
            @RequestParam("userId")  Long userId
    ){

        // 加入考试后创建考试信息关联表
        TesterExam testerExam = new TesterExam();
        testerExam.setExamId(examId);
        testerExam.setStudentId(userId);
        testerExam.setStartTime(LocalDateTime.now());
        testerExam.setStatus(1);

        int result = testerExamMapper.insert(testerExam);
        if(result==0){
            return Result.fail("开始考试异常，请联系管理员");
        }
        // 获取考试题目信息
        List<QuestionSimpleInfoVO> questions = examService.getQuestions(examId);

        return Result.ok(questions);
    }

    /**
     * 返回考试排名情况
     * @param examId 考试Id
     * @param userId 考生Id
     * @return RankInfoVO
     */
    @GetMapping("/rank/{examId}")
    public Result getRank(@PathVariable("examId") Long examId, @RequestParam("userId") Long userId){
        return Result.ok(examService.getMyRank(examId,userId));
    }

    /**
     * 出题者查询本次考试考生成绩情况
     * @param examId 考试Id
     * @return 成绩列表
     */
    @GetMapping("/{examId}/grades")
    public Result getTesterGrades(@PathVariable("examId") Long examId,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size){
        return Result.ok(gradeService.getGrades(examId,page,size));
    }

    /**
     * 获取本场考试基本的基本统计数据
     * @param examId 考试Id
     * @return 数据
     */
    @GetMapping("/{examId}/distribution")
    public Map<String, Object> getBasic(@PathVariable Long examId) {
        return examService.getBasicStats(examId);
    }

    /**
     * 获取本场考试各个分数段人数
     * @param examId 考试Id
     * @return 分数段及其人数 Map
     */
    @GetMapping("/{examId}/ranges")
    public List<Map<String, Object>> getRanges(@PathVariable Long examId) {
        return examService.getScoreRanges(examId);
    }

    /**
     * 提交异常记录
     * @param examId 考试ID
     * @param userId 用户ID
     */
    @PostMapping("/{examId}/submit/abnormal")
    public Result submitAbnormal(@PathVariable("examId") Long examId,
                                 @RequestParam("userId") Long userId,
                                 @RequestBody List<AbnormalBehaviorDTO> list){
        int result = abnormalBehaviorService.reportBehavior(examId,userId,list);
        return Result.ok(result);
    }

    /**
     * 考生交卷
     * @param examId 考试ID
     * @param dto 交卷DTO
     */
    @PostMapping("/{examId}/submit/paper")
    public Result submitPaper(@PathVariable("examId") Long examId, @RequestBody ExamSubmitDTO dto){
        // 先调用存Redis接口
        examPaperService.submitExam(dto,3L);
        return Result.ok("交卷成功");
        // 之后交给定时任务
    }
}
