package org.example.examsystem.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册请求参数
 */
@Data
public class RegisterRequest {
    @JsonIgnore
    private Long id; // 忽略前端传来的 id 字段
    
    private String email;
    private String password;
    
    private String realName;
    
    private Integer role; // 注册时忽略此字段，所有注册用户默认为 role=1（用户），管理员需通过其他方式创建
    
    @JsonProperty("verificationCode")
    private String verificationCode;
    
    // 用于存储未知字段
    private Map<String, Object> additionalProperties = new HashMap<>();
    
    // 处理未知字段，特别是 realname, code 等
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        if ("realname".equalsIgnoreCase(name) || "real_name".equalsIgnoreCase(name) || "name".equalsIgnoreCase(name)) {
            if (this.realName == null && value != null) {
                this.realName = value.toString();
            }
        } else if ("code".equalsIgnoreCase(name) || "verification_code".equalsIgnoreCase(name)) {
            if (this.verificationCode == null && value != null) {
                this.verificationCode = value.toString();
            }
        } else {
            additionalProperties.put(name, value);
        }
    }
}

