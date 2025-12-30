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

    /**
     * 邮箱注册
     * @param email 邮箱
     * @param password 密码明文
     * @param realName 真实姓名
     * @param role 用户角色，null 默认 1
     * @param verificationCode 验证码
     * @return 统一返回结果，data为RegisterResponseVO
     */
    Result register(String email, String password, String realName, Integer role, String verificationCode);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return 统一返回结果，data为SendCodeResponseVO
     */
    Result sendRegisterCode(String email);

    /**
     * 退出登录
     * @param token 传入的JWT令牌，可为空
     * @return 统一返回结果
     */
    Result logout(String token);

    /**
     * 修改密码
     * @param empId 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 统一返回结果
     */
    Result editPassword(Long empId, String oldPassword, String newPassword);
}

