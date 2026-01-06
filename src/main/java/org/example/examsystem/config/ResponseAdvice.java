package org.example.examsystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.examsystem.vo.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Controller层统一响应封装
 * 自动将Controller返回的数据包装成Result格式
 */
@Slf4j
@RestControllerAdvice(basePackages = "org.example.examsystem.controller")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要对响应进行处理
     * @param returnType 返回类型
     * @param converterType 消息转换器类型
     * @return true表示需要处理，false表示不需要处理
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果返回类型已经是Result类型，则不需要再次包装
        return !returnType.getParameterType().equals(Result.class);
    }

    /**
     * 在响应写入之前进行处理
     * @param body 响应体
     * @param returnType 返回类型
     * @param selectedContentType 选中的内容类型
     * @param selectedConverterType 选中的转换器类型
     * @param request 请求
     * @param response 响应
     * @return 处理后的响应体
     */
    @Override
    public Object beforeBodyWrite(@Nullable Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        // 如果body已经是Result类型，直接返回
        if (body instanceof Result) {
            return body;
        }

        // 如果body为null，返回成功但data为null的Result
        if (body == null) {
            return Result.ok();
        }

        // 如果body是String类型，需要特殊处理（因为StringHttpMessageConverter会直接写入字符串）
        if (body instanceof String) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Result result = Result.ok(body);
                // 设置响应头为application/json
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(result);
            } catch (Exception e) {
                log.error("转换String响应失败", e);
                return Result.fail("响应转换失败");
            }
        }

        // 其他类型统一包装成Result
        return Result.ok(body);
    }
}

