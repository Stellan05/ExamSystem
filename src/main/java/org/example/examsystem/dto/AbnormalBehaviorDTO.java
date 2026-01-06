package org.example.examsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 异常数据DTO
 */
@Data
public class AbnormalBehaviorDTO {
    private String behaviorType;
    private String remark;
    private LocalDateTime occurTime;
}
