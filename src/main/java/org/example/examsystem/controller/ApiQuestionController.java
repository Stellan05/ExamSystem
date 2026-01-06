package org.example.examsystem.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.examsystem.dto.CreateQuestionRequest;
import org.example.examsystem.dto.SetQuestionAnswerRequest;
import org.example.examsystem.dto.UpdateQuestionScoreRequest;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.mapper.ExamMapper;
import org.example.examsystem.service.IService.IQuestionService;
import org.example.examsystem.utils.JwtUtils;
import org.example.examsystem.vo.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiQuestionController {

    private final IQuestionService questionService;
    private final ExamMapper examMapper;

    /**
     * 出题接口
     * - creatorId 从 token 获取
     * - examId：优先使用请求体传参，其次 X-Current-Exam-Id 请求头，否则自动取最近创建的考试
     */
    @PostMapping("/questions")
    public Result createQuestion(@RequestHeader(value = "Authorization", required = false) String authorization,
                                 @RequestHeader(value = "X-Current-Exam-Id", required = false) Long currentExamId,
                                 @RequestBody CreateQuestionRequest request) {
        log.info("创建题目: type={}, content={}", request.getQuestionType(), request.getContent());

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.info(401, "未登录或缺少授权信息");
        }
        String token = authorization.substring(7);

        Long creatorId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Object uid = claims.get("userId");
            if (uid == null) {
                return Result.info(401, "授权信息不完整");
            }
            if (uid instanceof Number) {
                creatorId = ((Number) uid).longValue();
            } else {
                creatorId = Long.valueOf(uid.toString());
            }
        } catch (Exception e) {
            return Result.info(401, "token无效或已过期");
        }

        request.setCreatorId(creatorId);

        // 自动补 examId：request.examId > header > 最近未开始 > 最近任意
        if (request.getExamId() == null) {
            Long examId = null;
            if (currentExamId != null) {
                examId = currentExamId;
                log.info("从请求头获取当前考试ID: examId={}", examId);
            } else {
                log.info("查询未开始的考试: creatorId={}", creatorId);
                examId = examMapper.getRecentUnstartedExamId(creatorId);
                if (examId == null) {
                    log.info("未找到未开始的考试，查询最近创建的任何状态的考试: creatorId={}", creatorId);
                    examId = examMapper.getRecentExamId(creatorId);
                }
            }
            if (examId != null) {
                request.setExamId(examId);
            } else {
                log.warn("未找到可关联的考试，题目将不关联到任何考试。请先创建考试或手动指定examId。creatorId={}", creatorId);
            }
        }

        log.info("创建题目请求: examId={}, score={}", request.getExamId(), request.getScore());

        try {
            Long questionId = questionService.createQuestion(request);
            
            // 创建成功后，如果有 examId，则写入 exam_question
            if (request.getExamId() != null) {
                try {
                    Integer score = request.getScore() != null ? request.getScore() : 0;
                    log.info("开始关联题目与考试: questionId={}, examId={}, score={}", questionId, request.getExamId(), score);
                    questionService.associateQuestionWithExam(questionId, request.getExamId(), score);
                    log.info("题目与考试关联成功: questionId={}, examId={}", questionId, request.getExamId());
                } catch (Exception e) {
                    log.error("关联题目与考试失败: questionId={}, examId={}, error={}",
                            questionId, request.getExamId(), e.getMessage(), e);
                }
            }
            
            // Controller层统一包装成Result
            return Result.ok(questionId);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 设置题目的标准答案与解析
     */
    @PostMapping("/questions/{questionId}/answer")
    public Result setQuestionAnswer(@RequestHeader(value = "Authorization", required = false) String authorization,
                                    @PathVariable Long questionId,
                                    @RequestBody SetQuestionAnswerRequest request) {
        log.info("设置题目答案: questionId={}", questionId);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.info(401, "未登录或缺少授权信息");
        }
        String token = authorization.substring(7);
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            return Result.info(401, "token无效或已过期");
        }
        try {
            questionService.setQuestionAnswer(questionId, request);
            // Controller层统一包装成Result
            return Result.ok("设置成功");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message.contains("不存在")) {
                return Result.info(404, message);
            }
            return Result.fail(message);
        }
    }

    /**
     * 删除题目（本系统无“题库”概念：题目属于某场考试）
     * - 实际行为：从指定考试中移除题目（逻辑删除 exam_question 关联）
     * - examId 获取方式：优先 query 参数 examId，其次请求头 X-Current-Exam-Id
     */
    @PostMapping("/questions/{questionId}/delete")
    public Result deleteQuestion(@RequestHeader(value = "Authorization", required = false) String authorization,
                                 @RequestHeader(value = "X-Current-Exam-Id", required = false) Long currentExamId,
                                 @PathVariable Long questionId) {
        Long examId = currentExamId;
        log.info("删除题目(从考试移除): examId={}, questionId={}", examId, questionId);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.info(401, "未登录或缺少授权信息");
        }
        String token = authorization.substring(7);

        Long userId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Object uid = claims.get("userId");
            if (uid == null) {
                return Result.info(401, "授权信息不完整");
            }
            if (uid instanceof Number) {
                userId = ((Number) uid).longValue();
            } else {
                userId = Long.valueOf(uid.toString());
            }
        } catch (Exception e) {
            return Result.info(401, "token无效或已过期");
        }

        if (examId == null) {
            return Result.fail("缺少考试ID，请在请求头传 X-Current-Exam-Id 或在出题时显式传 examId");
        }
        if (questionId == null) {
            return Result.fail("题目ID不能为空");
        }

        Exam exam = examMapper.selectById(examId);
        if (exam == null || (exam.getIsDeleted() != null && exam.getIsDeleted() == 1)) {
            return Result.info(404, "考试不存在");
        }
        if (exam.getCreatorId() == null || !exam.getCreatorId().equals(userId)) {
            return Result.info(403, "无权限操作该考试");
        }

        try {
            questionService.removeQuestionFromExam(examId, questionId);
            return Result.ok("删除成功");
        } catch (Exception e) {
            log.error("删除失败(从考试移除): examId={}, questionId={}, error={}", examId, questionId, e.getMessage(), e);
            return Result.fail("删除失败: " + e.getMessage());
        }
    }

    /**
     * 修改题目在考试中的分数
     * URL: POST /api/questions/{questionId}/score
     * Body: {"examId":7,"score":10}
     */
    @PostMapping("/questions/{questionId}/score")
    public Result updateQuestionScore(@RequestHeader(value = "Authorization", required = false) String authorization,
                                      @PathVariable Long questionId,
                                      @RequestBody UpdateQuestionScoreRequest request) {
        log.info("收到修改分数请求: questionId={}, request={}", questionId, request);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.info(401, "未登录或缺少授权信息");
        }
        String token = authorization.substring(7);
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            return Result.info(401, "token无效或已过期");
        }

        if (request.getExamId() == null) {
            return Result.fail("考试ID不能为空");
        }
        if (questionId == null) {
            return Result.fail("题目ID不能为空");
        }
        if (request.getScore() == null || request.getScore() < 0) {
            return Result.fail("分数不能为空且必须大于等于0");
        }

        try {
            questionService.updateQuestionScore(request.getExamId(), questionId, request.getScore());
            log.info("题目分数修改成功: examId={}, questionId={}, score={}", request.getExamId(), questionId, request.getScore());
            return Result.ok("分数修改成功");
        } catch (Exception e) {
            log.error("修改题目分数失败: examId={}, questionId={}, error={}",
                    request.getExamId(), questionId, e.getMessage(), e);
            return Result.fail("修改失败: " + e.getMessage());
        }
    }

    /**
     * 从某场考试中移除题目（删除 exam_question 关联）
     * URL: POST /api/exams/{examId}/questions/{questionId}/remove
     */
    @PostMapping("/exams/{examId}/questions/{questionId}/remove")
    public Result removeQuestionFromExam(@RequestHeader(value = "Authorization", required = false) String authorization,
                                         @PathVariable Long examId,
                                         @PathVariable Long questionId) {
        log.info("从考试移除题目: examId={}, questionId={}", examId, questionId);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.info(401, "未登录或缺少授权信息");
        }
        String token = authorization.substring(7);

        Long userId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Object uid = claims.get("userId");
            if (uid == null) {
                return Result.info(401, "授权信息不完整");
            }
            if (uid instanceof Number) {
                userId = ((Number) uid).longValue();
            } else {
                userId = Long.valueOf(uid.toString());
            }
        } catch (Exception e) {
            return Result.info(401, "token无效或已过期");
        }

        if (examId == null || questionId == null) {
            return Result.fail("考试ID和题目ID不能为空");
        }

        // 权限：只有考试创建者允许移除题目
        Exam exam = examMapper.selectById(examId);
        if (exam == null || (exam.getIsDeleted() != null && exam.getIsDeleted() == 1)) {
            return Result.info(404, "考试不存在");
        }
        if (exam.getCreatorId() == null || !exam.getCreatorId().equals(userId)) {
            return Result.info(403, "无权限操作该考试");
        }

        try {
            questionService.removeQuestionFromExam(examId, questionId);
            return Result.ok("移除成功");
        } catch (Exception e) {
            log.error("移除失败: examId={}, questionId={}, error={}", examId, questionId, e.getMessage(), e);
            return Result.fail("移除失败: " + e.getMessage());
        }
    }
}
