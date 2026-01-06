package org.example.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.mapper.AnswerRecordMapper;
import org.example.examsystem.mapper.ExamMapper;
import org.example.examsystem.service.IService.IExamService;
import org.example.examsystem.service.IService.IGradeService;
import org.example.examsystem.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试相关模块controller
 */
@Slf4j
@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ExamController {

    private final IExamService examService;
    private final ExamMapper examMapper;
    private final AnswerRecordMapper answerRecordMapper;
    private final IGradeService gradeService;

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
            @RequestParam("userId") long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if(examService.getTesterExams(userId,page,size)!=null){
            return Result.ok(examService.getTesterExams(userId,page,size));
        }else{
            return Result.fail("错误参数");
        }
    }

    @PostMapping("/my-exam/creator")
    public Result getCreatorExam(@RequestParam("userId") long userId,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size){
        val result = examService.getCreatorExams(userId, page, size);
        if(result!=null){
            return Result.ok(result);
        }else{
            return Result.fail("错误参数");
        }
    }

    @GetMapping("my-exam/details/{examId}")
    public Result getMyExamDetails(
            @PathVariable("examId") Long examId,
            @RequestParam Long userId
    ){
        List<QuestionDetailVO> list = answerRecordMapper.getQuestionDetails(examId,userId);
        return Result.ok(list);
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
        try{
            queryWrapper.eq(Exam::getExamCode,Integer.valueOf(code));
        }catch (NumberFormatException ex){
            return Result.fail("考试码必须为6位数字");
        }
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
