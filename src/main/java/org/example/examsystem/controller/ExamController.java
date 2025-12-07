package org.example.examsystem.controller;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.mapper.ExamMapper;
import org.example.examsystem.mapper.TesterExamMapper;
import org.example.examsystem.service.IService.IExamPaperService;
import org.example.examsystem.service.IService.IExamService;
import org.example.examsystem.service.IService.IGradeService;
import org.example.examsystem.vo.QuestionDetailVO;
import org.example.examsystem.vo.QuestionSimpleInfoVO;
import org.example.examsystem.vo.Result;
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
    private final TesterExamMapper testerExamMapper;
    private final IExamPaperService examPaperService;
    private final IGradeService gradeService;

    /**
     * 分页查询参加本场考试人员信息
     * @param examId 本场考试Id
     * @param page 当前页
     * @param size 页尺寸
     * @return 人员信息VO类
     */
    @GetMapping("/testers")
    public Result getAllTest(
            @RequestParam long examId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        return Result.ok(examService.getAllTesters(examId,page,size));
    }

    /**
     * 用户获取考试信息
     * 测试者获取参加过的考试
     * 出题者获取发布过的试题
     * @return exam记录分页
     */
    @PostMapping("/my-exam")
    public Result getMyExams(
            @RequestBody Map<String,Object> map,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        long userId = Long.parseLong(map.get("userId").toString());
        int role= Integer.parseInt(map.get("role").toString());
        if(examService.getMyExams(userId,role,page,size)!=null){
            return Result.ok(examService.getMyExams(userId,role,page,size));
        }else{
            return Result.fail("错误参数");
        }
    }

    @GetMapping("my-exam/details/{examId}")
    public Result getMyExamDetails(
            @PathVariable("examId") Long examId,
            @RequestParam Long userId
    ){
        List<QuestionDetailVO> list = examPaperService.getQuestionDetail(examId,userId);
        return Result.ok(list);
    }

    /**
     * 用户输入考试码加入考试 （创建关联表）
     * 并获取题目
     * @return 题目链表
     */
    @Transactional
    @PostMapping("/join")
    public Result joinExam(
            @RequestBody Map<String,Object> map
    ){
        long userId = Long.parseLong(map.get("userId").toString());
        long examId = Long.parseLong(map.get("examId").toString());
        if(map.get("code").toString().length()!=6){
            return Result.fail("考试码必须为6位");
        }
        int code = Integer.parseInt(map.get("code").toString());
        // 查询考试
        Exam exam = examMapper.selectById(examId);
        if(exam==null){
            return Result.fail("未找到该考试！");
        }
        // 验证考试码
        if(code != exam.getExamCode()){
            return Result.fail("考试码错误！");
        }
        // 加入考试后创建考试信息关联表
        TesterExam testerExam = new TesterExam();
        testerExam.setExamId(examId);
        testerExam.setStudentId(userId);
        testerExam.setStartTime(LocalDateTime.now());
        testerExam.setStatus(1);

        int result = testerExamMapper.insert(testerExam);
        if(result==0){
            return Result.fail("加入考试异常，请联系管理员");
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
}
