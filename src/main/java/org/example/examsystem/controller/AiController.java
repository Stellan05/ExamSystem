package org.example.examsystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
            String prompt = (String) body.get("prompt");
            String userId = body.getOrDefault("userId", "lk").toString();
            boolean stream = Boolean.parseBoolean(body.getOrDefault("stream", "false").toString());

            // 构造讯飞星火请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "spark-x");
            requestBody.put("user", userId);
            requestBody.put("messages", new Map[]{ Map.of("role", "user", "content", prompt) });
            requestBody.put("stream", stream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_PASSWORD);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);

            // 解析返回 JSON
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return ResponseEntity.ok(jsonNode);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "AI调用失败", "msg", e.getMessage()));
        }
    }
}

