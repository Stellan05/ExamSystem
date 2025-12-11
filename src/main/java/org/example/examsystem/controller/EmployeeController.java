package org.example.examsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.examsystem.dto.LoginRequest;
import org.example.examsystem.service.IService.EmployeeService;
import org.example.examsystem.vo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工相关接口（登录）
 */
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
}

