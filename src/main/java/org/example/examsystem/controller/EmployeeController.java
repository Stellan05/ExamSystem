package org.example.examsystem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest request) {
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            return Result.fail("用户名或密码不能为空");
        }
        return authService.login(request.getUsername(), request.getPassword());
    }

    /**
     * 注册接口（邮箱注册）
     */
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
        return authService.register(request.getEmail(), request.getPassword(), request.getRealName(), request.getRole(), request.getVerificationCode());
    }

    /**
     * 发送注册验证码到邮箱
     */
    @PostMapping("/register/code")
    public Result sendRegisterCode(@RequestBody SendCodeRequest request) {
        log.info("收到发送验证码请求: email={}", request.getEmail());
        
        if (!StringUtils.hasText(request.getEmail())) {
            log.warn("发送验证码失败: 邮箱为空");
            return Result.fail("邮箱不能为空");
        }
        
        log.info("开始发送验证码: email={}", request.getEmail());
        return authService.sendRegisterCode(request.getEmail());
    }

    /**
     * 退出登录接口
     */
    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        String token = null;
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        return authService.logout(token);
    }

    /**
     * 修改密码接口
     */
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody EditPasswordRequest request) {
        if (request.getEmpId() == null) {
            return Result.fail("用户ID不能为空");
        }
        if (!StringUtils.hasText(request.getOldPassword()) || !StringUtils.hasText(request.getNewPassword())) {
            return Result.fail("旧密码和新密码均不能为空");
        }
        return authService.editPassword(request.getEmpId(), request.getOldPassword(), request.getNewPassword());
    }
}

