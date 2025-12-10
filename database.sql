CREATE TABLE `answer_record` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `student_exam_id` bigint NOT NULL COMMENT '所属答卷ID',
    `question_id` bigint NOT NULL COMMENT '题目ID',
    `auto_score` double DEFAULT '0' COMMENT '客观题自动得分',
    `teacher_score` double DEFAULT '0' COMMENT '主观题人工评分',
    `final_score` double DEFAULT '0' COMMENT '最终得分',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_reviewed` tinyint DEFAULT '0' COMMENT '是否已批阅（0：未批 1：已批）',
    `student_answer` text COMMENT '考生答案',
    `is_deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_exam` (`student_exam_id`),
    KEY `idx_question` (`question_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '作答详细'

CREATE TABLE `exam` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `exam_name` varchar(200) NOT NULL COMMENT '考试名称',
    `creator_id` bigint NOT NULL COMMENT '创建教师',
    `description` text COMMENT '考试说明',
    `start_time` datetime NOT NULL,
    `end_time` datetime NOT NULL,
    `limit_minutes` int NOT NULL COMMENT '限时（分钟）',
    `status` tinyint DEFAULT '0' COMMENT '0未开始 1进行中 2已结束',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` tinyint DEFAULT '0',
    `exam_code` varchar(10) NOT NULL COMMENT '考试码，系统随机生成六位，考生通过输入考试码加入考试',
    PRIMARY KEY (`id`),
    KEY `idx_creator` (`creator_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考试表'

CREATE TABLE `exam_question` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `exam_id` bigint NOT NULL,
    `question_id` bigint NOT NULL,
    `score` int NOT NULL COMMENT '该题在本场考试中的分值',
    `sort` int DEFAULT '0' COMMENT '题目顺序',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_exam` (`exam_id`),
    KEY `idx_question` (`question_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考试与题目关联表'

CREATE TABLE `question` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `creator_id` bigint NOT NULL COMMENT '出题人ID',
    `question_type` tinyint NOT NULL COMMENT '题型:1单选 2多选 3判断 4填空 5主观 6图片选择等',
    `content` text NOT NULL COMMENT '题干(可包含图片链接)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_creator` (`creator_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题目表'

CREATE TABLE `question_answer` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `question_id` bigint NOT NULL,
    `correct_answer` varchar(255) NOT NULL COMMENT '单选A；多选A,C；填空JSON；判断true/false',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `question_id` (`question_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题目答案表'

CREATE TABLE `question_option` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `question_id` bigint NOT NULL,
    `option_key` varchar(5) NOT NULL COMMENT 'A/B/C/D',
    `option_text` varchar(255) NOT NULL,
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `question_id` (`question_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 5 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题目选项表（选择）'

CREATE TABLE `tester_exam` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `exam_id` bigint NOT NULL,
    `student_id` bigint NOT NULL,
    `start_time` datetime DEFAULT NULL,
    `submit_time` datetime DEFAULT NULL,
    `duration` int DEFAULT NULL COMMENT '实际耗时(分钟)',
    `total_score` int DEFAULT '0' COMMENT '最终得分',
    `status` tinyint DEFAULT '0' COMMENT '0未开始 1作答中 2已提交',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_exam` (`exam_id`),
    KEY `idx_student` (`student_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 17 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考试参与者考试详细信息'

CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `password` varchar(200) NOT NULL COMMENT '密码（加密存储）',
    `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `email` varchar(255) NOT NULL COMMENT '用户邮箱',
    `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除:0正常 1删除',
    `role` int DEFAULT NULL COMMENT '用户身份 1：参与者 2：出题人',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表'


