package org.example.examsystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.examsystem.utils.JwtUtils;
import org.example.examsystem.vo.*;
import org.example.examsystem.service.IService.IWrongQuestionService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/wrong-question")
@RequiredArgsConstructor
public class WrongQuestionController {

    private final IWrongQuestionService wrongQuestionService;

    @GetMapping("/byType")
    public PageResult<WrongQuestionVO> getWrongQuestionsByType(@RequestParam Integer type,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        IPage<WrongQuestionVO> resultPage = wrongQuestionService.getWrongQuestionsByType(type,page,size);
        return new PageResult<>(
                resultPage.getRecords(),
                resultPage.getTotal(),
                resultPage.getSize(),
                resultPage.getCurrent()
        );
    }

    @GetMapping("/answer/random")
    public Result getRandomWrongQuestions(
            @RequestParam(value = "exclude", required = false) String exclude,
            @RequestHeader(value = "Authorization", required = false)String authorization){
        if(authorization==null||!authorization.startsWith("Bearer ")){
            return Result.info(401, "未登录或缺少授权信息");
        }
        String token = authorization.substring(7);
        Long studentId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Object uid = claims.get("userId");
            if (uid == null) {
                return Result.info(401, "授权信息不完整");
            }
            if (uid instanceof Number) {
                studentId = ((Number) uid).longValue();
            } else {
                studentId = Long.valueOf(uid.toString());
            }
        } catch (Exception e) {
            log.error("解析token失败", e);
            return Result.info(401, "token无效或已过期");
        }
        // 解析 exclude 参数
        List<Long> excludeQuestionIds = null;
        if (StringUtils.hasText(exclude)) {
            try {
                excludeQuestionIds = Arrays.stream(exclude.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::valueOf)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                log.warn("exclude参数格式错误: {}", exclude);
                return Result.fail("exclude参数格式错误，应为逗号分隔的数字");
            }
        }
        // 查询随机错题
        RandomWrongQuestionVO wrongQuestion = wrongQuestionService.getRandomWrongQuestion(studentId, excludeQuestionIds);

        if (wrongQuestion == null) {
            // 无错题时返回 data: null
            return Result.ok(null);
        }
        // 查询答案
        QuestionAnswerVO questionAnswer = wrongQuestionService.getQuestionAnswer(wrongQuestion.getQuestionId());
        //封装结果
        WrongQuestionWithAnswerVO result = new WrongQuestionWithAnswerVO();
        result.setQuestion(wrongQuestion);
        result.setAnswer(questionAnswer);

        return Result.ok(wrongQuestion);

    }


}
