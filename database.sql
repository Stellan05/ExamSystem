CREATE TABLE `user` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码（加密存储）',
    real_name VARCHAR(50) COMMENT '真实姓名',
    role TINYINT NOT NULL COMMENT '角色: 1学生 2教师 3管理员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除:0正常 1删除'
) COMMENT='用户表';

CREATE TABLE question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator_id BIGINT NOT NULL COMMENT '出题教师ID',
    question_type TINYINT NOT NULL COMMENT '题型:1单选 2多选 3判断 4填空 5主观 6图片选择等',
    content TEXT NOT NULL COMMENT '题干(可包含图片链接)',
    options_json TEXT COMMENT '题目选项(JSON格式，可为空)',
    answer TEXT COMMENT '标准答案(JSON 或字符串)',
    score INT DEFAULT 1 COMMENT '默认分值',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    INDEX idx_creator(creator_id)
) COMMENT='题库表';

CREATE TABLE exam (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exam_name VARCHAR(200) NOT NULL COMMENT '考试名称',
    creator_id BIGINT NOT NULL COMMENT '创建教师',
    description TEXT COMMENT '考试说明',
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    limit_minutes INT NOT NULL COMMENT '限时（分钟）',
    status TINYINT DEFAULT 0 COMMENT '0 未开始 1 进行中 2 已结束',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    INDEX idx_creator(creator_id)
) COMMENT='考试表';

CREATE TABLE exam_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exam_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    score INT NOT NULL COMMENT '该题在本场考试中的分值',
    sort INT DEFAULT 0 COMMENT '题目顺序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    INDEX idx_exam(exam_id),
    INDEX idx_question(question_id)
) COMMENT='考试与题目关联表';

CREATE TABLE student_exam (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exam_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    start_time DATETIME,
    submit_time DATETIME,
    duration INT COMMENT '实际耗时(秒)',
    total_score INT DEFAULT 0 COMMENT '最终得分',
    status TINYINT DEFAULT 0 COMMENT '0未开始 1作答中 2已提交',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    INDEX idx_exam(exam_id),
    INDEX idx_student(student_id)
) COMMENT='答卷表';

CREATE TABLE student_answer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_exam_id BIGINT NOT NULL COMMENT '所属答卷ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    student_answer TEXT COMMENT '学生答案(JSON 或字符串)',
    auto_score INT DEFAULT 0 COMMENT '客观题自动得分',
    teacher_score INT DEFAULT 0 COMMENT '主观题人工评分',
    final_score INT DEFAULT 0 COMMENT '最终得分',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    INDEX idx_exam(student_exam_id),
    INDEX idx_question(question_id)
) COMMENT='答案明细表';

