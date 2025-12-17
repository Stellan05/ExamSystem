package org.example.examsystem.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.examsystem.dto.CreateExamRequest;
import org.example.examsystem.service.IService.IExamService;
import org.example.examsystem.utils.JwtUtils;
import org.example.examsystem.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供 /api 前缀的考试相关接口
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiExamController {

    private final IExamService examService;

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

        return examService.createExam(request);
    }
}
