package org.example.examsystem.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.examsystem.dto.CreateExamRequest;
import org.example.examsystem.dto.UpdateExamBasicInfoRequest;
import org.example.examsystem.service.IService.IExamPaperService;
import org.example.examsystem.service.IService.IExamService;
import org.example.examsystem.utils.JwtUtils;
import org.example.examsystem.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 提供 /api 前缀的考试相关接口
 */
@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class ApiExamController {

    private final IExamService examService;
    private final IExamPaperService examPaperService;

    /**
     * 创建考试/试卷
     * - creatorId 从 token 中获取，前端无需传递（即使传递也会被覆盖）
     */
    @PostMapping("/exams")
    public Result createExam(@RequestHeader(value = "Authorization", required = false) String authorization,
                             @RequestBody CreateExamRequest request) {
        log.info("创建考试: code={}, name={}", request.getExamCode(), request.getExamName());

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
        log.info("创建考试: creatorId={} (从token获取)", creatorId);

        try {
            // Controller层统一包装成Result
            return Result.ok(examService.createExam(request));
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message.contains("考试码已存在")) {
                return Result.info(409, message);
            }
            return Result.fail(message);
        }
    }

    /**
     * 获取试卷详细（出题者/创建者视角）
     * - 返回考试基本信息 + 题目列表（含分值/选项/标准答案/解析）
     */
    @GetMapping("/exams/{examId}/paper/detail")
    public Result getExamPaperDetail(@RequestHeader(value = "Authorization", required = false) String authorization,
                                     @PathVariable Long examId) {
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

        try {
            return Result.ok(examPaperService.getPaperDetailForCreator(examId, userId));
        } catch (IllegalArgumentException e) {
            // 这里用 fail 返回即可；如果你更想区分 403/404，也可以继续细分
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 修改试卷基本信息（仅创建者可修改，默认仅未开始 status=0 允许修改）
     */
    @PostMapping("/exams/{examId}/update")
    public Result updateExamBasicInfo(@RequestHeader(value = "Authorization", required = false) String authorization,
                                      @PathVariable Long examId,
                                      @RequestBody UpdateExamBasicInfoRequest request) {
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

        try {
            examService.updateExamBasicInfo(examId, userId, request);
            // Controller层统一包装成Result
            return Result.ok("修改成功");
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 完成试卷编辑（验证试卷完整性）
     * - 检查题目数量（至少1道）
     * - 检查所有题目是否有标准答案（主观题可选）
     * - 检查所有题目是否有分数
     */
    @PostMapping("/exams/{examId}/complete")
    public Result completeExamEdit(@RequestHeader(value = "Authorization", required = false) String authorization,
                                    @PathVariable Long examId) {
        log.info("完成试卷编辑验证: examId={}", examId);

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

        try {
            examService.completeExamEdit(examId, userId);
            // Controller层统一包装成Result
            return Result.ok("完成编辑");
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }
}
