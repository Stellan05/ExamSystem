package org.example.examsystem.dto;

import lombok.Data;

/**
 * 修改密码请求
 */
@Data
public class EditPasswordRequest {
    private Long empId;
    private String newPassword;
    private String oldPassword;
}




















