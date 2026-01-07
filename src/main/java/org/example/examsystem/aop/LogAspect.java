package org.example.examsystem.aop;

import com.alibaba.fastjson.JSONObject;
import org.example.examsystem.anno.Log;
import org.example.examsystem.entity.OperationLog;
import org.example.examsystem.service.IService.IOperationLogService;
import org.example.examsystem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 操作日志切面类
 * 用于记录用户操作日志并存储到数据库中
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final IOperationLogService operationLogService;

    /**
     * 环绕通知：记录操作日志
     */
    @Around("@annotation(org.example.examsystem.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            // 如果没有请求上下文，直接执行方法
            return joinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();

        // 获取日志注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 创建操作日志对象
        OperationLog operationLog = new OperationLog();
        long beginTime = System.currentTimeMillis();
        Object result = null;
        String errorMessage = null;
        Integer status = 1; // 默认成功

        try {
            // 获取用户信息
            setUserInfo(operationLog, request);

            // 设置操作信息
            setOperationInfo(operationLog, logAnnotation, joinPoint, method);

            // 设置请求信息
            setRequestInfo(operationLog, request, joinPoint);

            // 执行目标方法
            result = joinPoint.proceed();

            // 设置响应结果（限制长度，避免过长）
            String responseResult = JSONObject.toJSONString(result);
            if (responseResult != null && responseResult.length() > 2000) {
                responseResult = responseResult.substring(0, 2000) + "...(已截断)";
            }
            operationLog.setResponseResult(responseResult);

        } catch (Throwable e) {
            // 记录异常信息
            status = 0; // 失败
            errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.length() > 500) {
                errorMessage = errorMessage.substring(0, 500) + "...(已截断)";
            }
            operationLog.setErrorMessage(errorMessage);
            throw e; // 重新抛出异常
        } finally {
            // 如果用户信息为空，尝试从返回结果中获取（适用于登录接口等）
            if (operationLog.getUserId() == null && result != null) {
                extractUserInfoFromResult(operationLog, result);
            }
            
            // 计算耗时
            long endTime = System.currentTimeMillis();
            operationLog.setDuration(endTime - beginTime);
            operationLog.setStatus(status);

            // 保存日志到数据库
            try {
                operationLogService.save(operationLog);
                log.debug("操作日志记录成功: {}", operationLog);
            } catch (Exception e) {
                // 记录日志失败不影响主流程
                log.error("保存操作日志失败", e);
            }
        }

        return result;
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(OperationLog operationLog, HttpServletRequest request) {
        try {
            // 优先从Authorization请求头获取token（Bearer格式）
            String token = null;
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
            } else {
                // 如果没有Authorization，尝试从token请求头获取
                token = request.getHeader("token");
            }

            if (token != null && !token.isEmpty()) {
                Claims claims = JwtUtils.parseJWT(token);
                // 获取userId
                Object userIdObj = claims.get("userId");
                if (userIdObj != null) {
                    Long userId;
                    if (userIdObj instanceof Number) {
                        userId = ((Number) userIdObj).longValue();
                    } else {
                        userId = Long.valueOf(userIdObj.toString());
                    }
                    operationLog.setUserId(userId);
                }
                // 获取username
                Object usernameObj = claims.get("username");
                if (usernameObj != null) {
                    operationLog.setUsername(usernameObj.toString());
                }
            }
        } catch (Exception e) {
            // 解析token失败，不设置用户信息
            log.debug("获取用户信息失败，可能未登录: {}", e.getMessage());
        }
    }

    /**
     * 设置操作信息（模块、操作类型、描述等）
     */
    private void setOperationInfo(OperationLog operationLog, Log logAnnotation, 
                                   ProceedingJoinPoint joinPoint, Method method) {
        // 从注解获取模块、操作类型、描述
        if (logAnnotation != null) {
            String module = logAnnotation.module();
            String operationType = logAnnotation.operationType();
            String description = logAnnotation.description();

            if (module != null && !module.isEmpty()) {
                operationLog.setModule(module);
            }
            if (operationType != null && !operationType.isEmpty()) {
                operationLog.setOperationType(operationType);
            }
            if (description != null && !description.isEmpty()) {
                operationLog.setDescription(description);
            }
        }

        // 如果注解中没有指定模块，尝试从类名推断
        if (operationLog.getModule() == null || operationLog.getModule().isEmpty()) {
            String className = joinPoint.getTarget().getClass().getSimpleName();
            if (className.endsWith("Controller")) {
                String module = className.substring(0, className.length() - "Controller".length());
                operationLog.setModule(module);
            }
        }
    }

    /**
     * 设置请求信息（URL、请求方法、参数、IP等）
     */
    private void setRequestInfo(OperationLog operationLog, HttpServletRequest request, 
                                ProceedingJoinPoint joinPoint) {
        // 请求URL
        String requestUrl = request.getRequestURI();
        operationLog.setRequestUrl(requestUrl);

        // 请求方法
        String requestMethod = request.getMethod();
        operationLog.setRequestMethod(requestMethod);

        // 请求参数
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            try {
                String requestParams = JSONObject.toJSONString(args);
                // 限制参数长度
                if (requestParams != null && requestParams.length() > 2000) {
                    requestParams = requestParams.substring(0, 2000) + "...(已截断)";
                }
                operationLog.setRequestParams(requestParams);
            } catch (Exception e) {
                // JSON序列化失败，使用toString
                operationLog.setRequestParams(java.util.Arrays.toString(args));
            }
        }

        // IP地址
        String ipAddress = getIpAddress(request);
        operationLog.setIpAddress(ipAddress);
    }

    /**
     * 获取客户端IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果通过代理，可能会有多个IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 从返回结果中提取用户信息（适用于登录接口等）
     */
    private void extractUserInfoFromResult(OperationLog operationLog, Object result) {
        try {
            // 如果返回的是Result包装类
            if (result instanceof org.example.examsystem.vo.Result) {
                org.example.examsystem.vo.Result resultObj = (org.example.examsystem.vo.Result) result;
                Object data = resultObj.getData();
                if (data != null) {
                    // 尝试从data中提取用户信息（EmployeeLoginVO）
                    String jsonStr = JSONObject.toJSONString(data);
                    JSONObject dataJson = JSONObject.parseObject(jsonStr);
                    if (dataJson != null) {
                        // 获取id字段（可能是userId或id）
                        Object idObj = dataJson.get("id");
                        if (idObj == null) {
                            idObj = dataJson.get("userId");
                        }
                        if (idObj != null) {
                            Long userId;
                            if (idObj instanceof Number) {
                                userId = ((Number) idObj).longValue();
                            } else {
                                userId = Long.valueOf(idObj.toString());
                            }
                            operationLog.setUserId(userId);
                        }
                        // 获取username字段（可能是userName或username）
                        Object usernameObj = dataJson.get("userName");
                        if (usernameObj == null) {
                            usernameObj = dataJson.get("username");
                        }
                        if (usernameObj != null) {
                            operationLog.setUsername(usernameObj.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 提取失败不影响主流程
            log.debug("从返回结果提取用户信息失败: {}", e.getMessage());
        }
    }
}
