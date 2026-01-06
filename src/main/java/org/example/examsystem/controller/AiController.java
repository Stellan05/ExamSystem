package org.example.examsystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ai")
@CrossOrigin // 允许前端跨域访问
public class AiController {

    private static final String API_URL = "https://spark-api-open.xf-yun.com/v2/chat/completions";
    private static final String API_PASSWORD = "UHxnxXMcugqTZpXtZwnz:VBgaDukATLINpTZpGiLr";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<?> getAiAnswer(@RequestBody Map<String, Object> body) {
        try {
            String type = (String) body.get("type");
            String content = (String) body.get("content");
            Object optionObj = body.get("options");
            Object answerObj = body.get("answer");
            Object userAnswerObj = body.get("userAnswer");
            String userId = body.getOrDefault("userId", "lk").toString();
            boolean stream = Boolean.parseBoolean(body.getOrDefault("stream", "false").toString());

            // 处理正确答案
            String answerStr;
            if(answerObj instanceof List) {
                answerStr = String.join(", ", (List<String>) answerObj);
            } else if(answerObj != null) {
                answerStr = answerObj.toString();
            } else {
                answerStr = "";
            }

            // 处理考生作答
            String userAnswerStr;
            if(userAnswerObj instanceof List) {
                userAnswerStr = String.join(", ", (List<String>) userAnswerObj);
            } else if(userAnswerObj != null) {
                userAnswerStr = userAnswerObj.toString();
            } else {
                userAnswerStr = "未作答";
            }

            // 拼接提示词
            StringBuilder prompt = new StringBuilder();
            prompt.append("你是一名考试解析助手，请对以下试题进行详细讲解：\n\n")
                    .append("【题型】").append(type).append("\n")
                    .append("【题目】").append(content).append("\n");

            // 处理选项
            if(optionObj instanceof List) {
                List<String> optionList = (List<String>) optionObj;
                prompt.append("【选项】\n");
                char ch = 'A';
                for(String opt : optionList) {
                    prompt.append(ch).append(": ").append(opt).append("\n");
                    ch++;
                }
            }

            prompt.append("【正确答案】").append(answerStr).append("\n")
                    .append("【考生作答】").append(userAnswerStr).append("\n\n")
                    .append("请说明：\n1. 解题思路\n2. 正确答案依据\n3. 若考生错误，指出原因");

            // 构造讯飞星火请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "spark-x");
            requestBody.put("user", userId);
            requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt.toString())));
            requestBody.put("stream", stream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_PASSWORD);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            log.info("询问完成");

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            log.info(jsonNode.toString());
            return ResponseEntity.ok(jsonNode);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "AI调用失败", "msg", e.getMessage()));
        }
    }
}

