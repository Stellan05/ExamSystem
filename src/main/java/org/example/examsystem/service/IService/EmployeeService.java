package org.example.examsystem.service.IService;

import org.example.examsystem.vo.Result;

public interface EmployeeService {

    /**
     * 登录并返回结果（含token与用户信息）
     * @param username 用户名/邮箱
     * @param password 密码明文
     * @return 统一返回结果
     */
    Result login(String username, String password);
}

