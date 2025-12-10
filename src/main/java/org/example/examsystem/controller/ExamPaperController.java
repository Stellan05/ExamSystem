package org.example.examsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.examsystem.dto.JudgeRequest;
import org.example.examsystem.service.IService.IExamPaperService;
import org.example.examsystem.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试卷相关模块controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/exam/paper")
public class ExamPaperController {

    private final IExamPaperService examPaperService;

    /**
     * 获取本次考试题目Id列表
     * @param examId 考试Id
     * @return 题目Id列表
     */
    @GetMapping("/{examId}/questions")
    public Result getQuestionIds(@PathVariable("examId") Long examId){
        return Result.ok(examPaperService.getQuestionIdByExamId(examId));
    }

    /**
     * 获取主观题ID列表，即需要批阅的题目
     * @param examId 考试ID
     * @return Id列表
     */
    @GetMapping("/{examId}/questions/subjective")
    public Result getSubjectiveQuestionIds(@PathVariable("examId") Long examId){
        return Result.ok(examPaperService.getSubjectiveQuestionIds(examId));
    }

    /**
     * 查询考生的某题的具体答题情况
     * @param examId 考试Id
     * @param questionId 试题Id
     * @param userId 用户Id
     * @return 某一题答题详细
     */
    @GetMapping("/{examId}/questions/{questionId}/details")
    public Result getQuestionDetails(@PathVariable("examId") Long examId,
                                     @PathVariable("questionId") Long questionId,
                                     @RequestParam("userId") Long userId
                                     ){
        return Result.ok(examPaperService.getReviewQuestionDetail(examId,questionId,userId));
    }

    /**
     * 评卷接口
     * @param examId 考试ID
     * @param questionId 问题ID
     * @param userId 考生ID
     * @param judgeRequest 评卷DTO --- 内含分数
     * @return 评卷成功
     */
    @PostMapping("/{examId}/questions/{questionId}/judge")
    public Result judgeQuestionAnswer(@PathVariable("examId") Long examId,
                                      @PathVariable("questionId") Long questionId,
                                      @RequestParam("userId") Long userId,
                                      @RequestBody JudgeRequest judgeRequest){
        return null;
    }


    /**
     * 获取本次考试批阅进度
     * @param examId 考试ID
     * @return 进度
     */
    @GetMapping("/{examId}/progress")
    public Result getReviewProgress(@PathVariable("examId") Long examId){
        return Result.ok(examPaperService.getReviewProgress(examId));
    }
}
