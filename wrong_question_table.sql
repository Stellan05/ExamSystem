-- 错题表
CREATE TABLE IF NOT EXISTS `wrong_question` (
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT(20) NOT NULL COMMENT '关联学生ID',
    `question_id` BIGINT(20) NOT NULL COMMENT '关联题目ID',
    `wrong_count` INT(11) DEFAULT 1 COMMENT '累计错误次数',
    `student_answer` VARCHAR(500) DEFAULT NULL COMMENT '学生答案',
    `correct_answer` VARCHAR(500) DEFAULT NULL COMMENT '正确答案',
    `note` TEXT COMMENT '学生笔记',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` INT(1) DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_question_id` (`question_id`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_create_time` (`create_time`),
    UNIQUE KEY `uk_student_question` (`student_id`, `question_id`, `is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题表';






