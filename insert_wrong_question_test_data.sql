-- 错题表测试数据
-- 注意：请根据实际情况调整 student_id、question_id 等字段的值
-- 确保 question_id 在 question 表中存在，student_id 在 user 表中存在

-- 1. 第一条错题数据（单选题）
-- 假设 student_id=1001, question_id=1（单选题）
INSERT INTO `wrong_question` (`student_id`, `question_id`, `wrong_count`, `student_answer`, `correct_answer`, `note`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1001, 1, 2, 'A', 'B', '这道题需要重点复习，主要考察Java基础语法', '2024-12-20 10:30:00', '2024-12-22 15:20:00', 0);

-- 2. 第二条错题数据（多选题）
-- 假设 student_id=1001, question_id=2（多选题）
INSERT INTO `wrong_question` (`student_id`, `question_id`, `wrong_count`, `student_answer`, `correct_answer`, `note`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1001, 2, 3, 'A,C', 'A,B,C', '多选题容易漏选，需要仔细审题', '2024-12-21 14:15:00', '2024-12-23 09:45:00', 0);

-- 如果需要为不同学生创建错题，可以使用以下示例：
-- INSERT INTO `wrong_question` (`student_id`, `question_id`, `wrong_count`, `student_answer`, `correct_answer`, `note`, `create_time`, `update_time`, `is_deleted`) 
-- VALUES (1002, 1, 1, 'C', 'B', '第一次做错，需要理解概念', '2024-12-22 11:00:00', '2024-12-22 11:00:00', 0);









