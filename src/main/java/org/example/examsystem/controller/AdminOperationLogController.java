package org.example.examsystem.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.examsystem.anno.Log;
import org.example.examsystem.dto.BatchDeleteRequest;
import org.example.examsystem.dto.OperationLogQueryParams;
import org.example.examsystem.dto.OperationLogSearchParams;
import org.example.examsystem.entity.OperationLog;
import org.example.examsystem.service.IService.IOperationLogService;
import org.example.examsystem.utils.JwtUtils;
import org.example.examsystem.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员操作日志控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminOperationLogController {

    private final IOperationLogService operationLogService;

    /**
     * 获取操作日志列表
     */
    @GetMapping("/operation-logs")
    public Result getOperationLogs(@RequestHeader(value = "Authorization", required = false) String authorization,
                                    @RequestParam(required = false) Long userId,
                                    @RequestParam(required = false) String module,
                                    @RequestParam(required = false) String operationType,
                                    @RequestParam(required = false) Integer status,
                                    @RequestParam(required = false) String startTime,
                                    @RequestParam(required = false) String endTime) {
        validateToken(authorization);

        OperationLogQueryParams params = new OperationLogQueryParams();
        params.setUserId(userId);
        params.setModule(module);
        params.setOperationType(operationType);
        params.setStatus(status);
        params.setStartTime(startTime);
        params.setEndTime(endTime);
        // 应该这样实现
        Object result = operationLogService.getOperationLogs(params);
        System.out.println(Result.ok(result)); // 仅调试用
        return Result.ok(result); // 实际返回

    }

    /**
     * 获取操作日志详情
     */
    @GetMapping("/operation-logs/{id}")
    public Result getOperationLogDetail(@RequestHeader(value = "Authorization", required = false) String authorization,
                                        @PathVariable Long id) {
        validateToken(authorization);
        
        OperationLog operationLog = operationLogService.getOperationLogDetail(id);
        if (operationLog == null) {
            return Result.info(404, "操作日志不存在");
        }
        
        // Controller层统一包装成Result
        return Result.ok(operationLog);
    }

    /**
     * 搜索操作日志
     */
    @GetMapping("/operation-logs/search")
    public Result searchOperationLogs(@RequestHeader(value = "Authorization", required = false) String authorization,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) String startDate,
                                      @RequestParam(required = false) String endDate,
                                      @RequestParam(required = false) String module,
                                      @RequestParam(required = false) String operationType) {
        validateToken(authorization);

        OperationLogSearchParams params = new OperationLogSearchParams();
        params.setKeyword(keyword);
        params.setStartDate(startDate);
        params.setEndDate(endDate);
        params.setModule(module);
        params.setOperationType(operationType);

        // Controller层统一包装成Result
        return Result.ok(operationLogService.searchOperationLogs(params));
    }

    /**
     * 导出操作日志
     * 成功时返回CSV文件流，失败时返回Result格式的错误信息
     */
    @GetMapping("/operation-logs/export")
    public void exportOperationLogs(@RequestHeader(value = "Authorization", required = false) String authorization,
                                    @RequestParam(required = false) Long userId,
                                    @RequestParam(required = false) String module,
                                    @RequestParam(required = false) String operationType,
                                    @RequestParam(required = false) Integer status,
                                    @RequestParam(required = false) String startTime,
                                    @RequestParam(required = false) String endTime,
                                    HttpServletResponse response) {
        validateToken(authorization);

        OperationLogQueryParams params = new OperationLogQueryParams();
        params.setUserId(userId);
        params.setModule(module);
        params.setOperationType(operationType);
        params.setStatus(status);
        params.setStartTime(startTime);
        params.setEndTime(endTime);

        // 导出失败时会抛出异常，由GlobalExceptionHandler统一处理并返回Result格式
        operationLogService.exportOperationLogs(params, response);
    }

    /**
     * 删除操作日志
     */
    @Log(module = "操作日志管理", operationType = "删除", description = "删除操作日志")
    @DeleteMapping("/operation-logs/{id}")
    public Result deleteOperationLog(@RequestHeader(value = "Authorization", required = false) String authorization,
                                     @PathVariable Long id) {
        validateToken(authorization);
        
        if (id == null) {
            return Result.fail("操作日志ID不能为空");
        }
        
        try {
            operationLogService.deleteOperationLog(id);
            // Controller层统一包装成Result
            return Result.ok("删除成功");
        } catch (IllegalArgumentException e) {
            return Result.info(404, e.getMessage());
        }
    }

    /**
     * 批量删除操作日志
     */
    @Log(module = "操作日志管理", operationType = "批量删除", description = "批量删除操作日志")
    @PostMapping("/operation-logs/batch-delete")
    public Result batchDeleteOperationLogs(@RequestHeader(value = "Authorization", required = false) String authorization,
                                           @RequestBody BatchDeleteRequest request) {
        validateToken(authorization);
        
        try {
            operationLogService.batchDeleteOperationLogs(request);
            // Controller层统一包装成Result
            return Result.ok("批量删除成功");
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 验证token并检查管理员权限
     * 仅允许 role=0 的管理员访问
     */
    private void validateToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("未登录或缺少授权信息");
        }
        String token = authorization.substring(7);
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Object uid = claims.get("userId");
            if (uid == null) {
                throw new IllegalArgumentException("授权信息不完整");
            }
            
            // 检查管理员权限：role=0 为管理员
            Object roleObj = claims.get("role");
            if (roleObj == null) {
                throw new IllegalArgumentException("权限不足，仅管理员可访问");
            }
            
            Integer role;
            if (roleObj instanceof Number) {
                role = ((Number) roleObj).intValue();
            } else {
                try {
                    role = Integer.valueOf(roleObj.toString());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("权限不足，仅管理员可访问");
                }
            }
            
            // 只有 role=0 的管理员才能访问
            if (role == null || role != 0) {
                throw new IllegalArgumentException("权限不足，仅管理员可访问");
            }
        } catch (IllegalArgumentException e) {
            // 重新抛出权限相关的异常
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("token无效或已过期");
        }
    }
}

