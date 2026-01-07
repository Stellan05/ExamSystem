package org.example.examsystem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.examsystem.anno.Log;
import org.example.examsystem.dto.LoginRequest;
import org.example.examsystem.dto.RegisterRequest;
import org.example.examsystem.dto.SendCodeRequest;
import org.example.examsystem.dto.EditPasswordRequest;
import org.example.examsystem.service.IService.EmployeeService;
import org.example.examsystem.vo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * 员工相关接口（登录）
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService authService;

    /**
     * 登录接口
     */
    @Log(module = "用户管理", operationType = "登录", description = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest request) {
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            return Result.fail("用户名或密码不能为空");
        }
        try {
            // Controller层统一包装成Result
            return Result.ok(authService.login(request.getUsername(), request.getPassword()));
        } catch (IllegalArgumentException e) {
            return Result.info(401, e.getMessage());
        }
    }

    /**
     * 注册接口（邮箱注册）
     */
    @Log(module = "用户管理", operationType = "注册", description = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterRequest request) {
        log.info("收到注册请求: email={}, password={}, realName=[{}], role={}, verificationCode={}", 
                request.getEmail(), 
                request.getPassword() != null ? "***" : "null",
                request.getRealName() != null ? request.getRealName() : "null", 
                request.getRole(), 
                request.getVerificationCode() != null ? "***" : "null");
        
        // 如果 realName 仍然为 null，说明字段映射失败
        if (request.getRealName() == null) {
            log.warn("警告: realName 字段映射失败，前端可能使用了不同的字段名");
        }
        
        if (!StringUtils.hasText(request.getEmail())) {
            log.warn("注册失败: 邮箱为空");
            return Result.fail("邮箱不能为空");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            log.warn("注册失败: 密码为空");
            return Result.fail("密码不能为空");
        }
        if (!StringUtils.hasText(request.getVerificationCode())) {
            log.warn("注册失败: 验证码为空, email={}", request.getEmail());
            return Result.fail("验证码不能为空");
        }
        
        log.info("开始处理注册请求: email={}", request.getEmail());
        try {
            // Controller层统一包装成Result
            return Result.ok(authService.register(request.getEmail(), request.getPassword(), request.getRealName(), request.getRole(), request.getVerificationCode()));
        } catch (IllegalArgumentException e) {
            // 根据异常消息判断错误类型
            String message = e.getMessage();
            if (message.contains("验证码")) {
                return Result.info(400, message);
            } else if (message.contains("已注册")) {
                return Result.info(409, message);
            }
            return Result.fail(message);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 发送注册验证码到邮箱
     */
    @Log(module = "用户管理", operationType = "发送验证码", description = "发送注册验证码")
    @PostMapping("/register/code")
    public Result sendRegisterCode(@RequestBody SendCodeRequest request) {
        log.info("收到发送验证码请求: email={}", request.getEmail());
        
        if (!StringUtils.hasText(request.getEmail())) {
            log.warn("发送验证码失败: 邮箱为空");
            return Result.fail("邮箱不能为空");
        }
        
        log.info("开始发送验证码: email={}", request.getEmail());
        try {
            // Controller层统一包装成Result
            return Result.ok(authService.sendRegisterCode(request.getEmail()));
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message.contains("已注册")) {
                return Result.info(409, message);
            }
            return Result.fail(message);
        }
    }

    /**
     * 退出登录接口
     */
    @Log(module = "用户管理", operationType = "退出登录", description = "用户退出登录")
    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        String token = null;
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        try {
            authService.logout(token);
            // Controller层统一包装成Result
            return Result.ok("退出成功");
        } catch (IllegalArgumentException e) {
            return Result.info(401, e.getMessage());
        }
    }

    /**
     * 修改密码接口
     */
    @Log(module = "用户管理", operationType = "修改密码", description = "用户修改密码")
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody EditPasswordRequest request) {
        if (request.getEmpId() == null) {
            return Result.fail("用户ID不能为空");
        }
        if (!StringUtils.hasText(request.getOldPassword()) || !StringUtils.hasText(request.getNewPassword())) {
            return Result.fail("旧密码和新密码均不能为空");
        }
        try {
            authService.editPassword(request.getEmpId(), request.getOldPassword(), request.getNewPassword());
            // Controller层统一包装成Result
            return Result.ok("密码修改成功");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message.contains("不能与旧密码相同") || message.contains("旧密码错误")) {
                return Result.info(400, message);
            }
            return Result.fail(message);
        }
    }
}

