package org.example.examsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评卷进度VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressVO {
    private long reviewedCount;  // 已批阅学生数
    private long totalCount;     // 总学生数
}
