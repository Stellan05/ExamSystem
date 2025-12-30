package org.example.examsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AbnormalInfoVO {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String behaviorType;
    private LocalDateTime occurTime;
    private String remark;
}
