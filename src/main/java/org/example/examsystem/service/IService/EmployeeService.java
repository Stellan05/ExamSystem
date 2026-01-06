package org.example.examsystem.service.IService;

import org.example.examsystem.vo.EmployeeLoginVO;
import org.example.examsystem.vo.RegisterResponseVO;
import org.example.examsystem.vo.SendCodeResponseVO;

public interface EmployeeService {

    /**
     * 登录并返回结果（含token与用户信息）
     * @param username 用户名/邮箱
     * @param password 密码明文
     * @return 登录信息VO，如果登录失败返回null
     */
    EmployeeLoginVO login(String username, String password);

    /**
     * 邮箱注册
     * @param email 邮箱
     * @param password 密码明文
     * @param realName 真实姓名
     * @param role 用户角色（注册时此参数会被忽略，所有注册用户默认为 role=1 用户）
     * @param verificationCode 验证码
     * @return 注册结果VO，如果注册失败抛出异常
     */
    RegisterResponseVO register(String email, String password, String realName, Integer role, String verificationCode);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return 发送结果VO，如果发送失败抛出异常
     */
    SendCodeResponseVO sendRegisterCode(String email);

    /**
     * 退出登录
     * @param token 传入的JWT令牌，可为空
     */
    void logout(String token);

    /**
     * 修改密码
     * @param empId 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    void editPassword(Long empId, String oldPassword, String newPassword);
}

