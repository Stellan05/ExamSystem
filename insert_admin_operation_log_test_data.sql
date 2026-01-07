-- 管理员操作日志测试数据
-- 注意：请根据实际情况调整 user_id、username 等字段的值

-- 1. 查询操作日志列表（成功）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志列表', 'GET', '/admin/operation-logs', '{"userId":null,"module":null,"operationType":null,"status":null,"startTime":null,"endTime":null}', '{"code":200,"message":"success","data":[]}', '192.168.1.100', 1, NULL, 120, '2024-12-25 10:30:00', '2024-12-25 10:30:00', 0);

-- 2. 查询操作日志列表（带筛选条件）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志列表（筛选：模块=考试管理）', 'GET', '/admin/operation-logs', '{"module":"考试管理","status":1}', '{"code":200,"message":"success","data":[]}', '192.168.1.100', 1, NULL, 95, '2024-12-25 11:15:00', '2024-12-25 11:15:00', 0);

-- 3. 查询操作日志详情（成功）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志详情（ID=1）', 'GET', '/admin/operation-logs/1', NULL, '{"code":200,"message":"success","data":{"id":1,"userId":1,"username":"admin"}}', '192.168.1.100', 1, NULL, 45, '2024-12-25 12:00:00', '2024-12-25 12:00:00', 0);

-- 4. 查询操作日志详情（失败-不存在）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志详情（ID=99999，不存在）', 'GET', '/admin/operation-logs/99999', NULL, '{"code":404,"message":"操作日志不存在","data":null}', '192.168.1.100', 0, '操作日志不存在', 30, '2024-12-25 12:30:00', '2024-12-25 12:30:00', 0);

-- 5. 搜索操作日志（成功）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '搜索', '搜索操作日志（关键词：Java）', 'GET', '/admin/operation-logs/search', '{"keyword":"Java","startDate":"2024-12-01","endDate":"2024-12-31"}', '{"code":200,"message":"success","data":[]}', '192.168.1.101', 1, NULL, 180, '2024-12-25 13:00:00', '2024-12-25 13:00:00', 0);

-- 6. 搜索操作日志（按模块和操作类型）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '搜索', '搜索操作日志（模块=考试管理，操作类型=创建）', 'GET', '/admin/operation-logs/search', '{"module":"考试管理","operationType":"创建"}', '{"code":200,"message":"success","data":[]}', '192.168.1.101', 1, NULL, 150, '2024-12-25 13:30:00', '2024-12-25 13:30:00', 0);

-- 7. 导出操作日志（成功）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '导出', '导出操作日志为CSV文件', 'GET', '/admin/operation-logs/export', '{"startTime":"2024-12-01 00:00:00","endTime":"2024-12-31 23:59:59"}', '{"code":200,"message":"导出成功"}', '192.168.1.102', 1, NULL, 350, '2024-12-25 14:00:00', '2024-12-25 14:00:00', 0);

-- 8. 导出操作日志（带筛选条件）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '导出', '导出操作日志（筛选：状态=成功）', 'GET', '/admin/operation-logs/export', '{"status":1,"module":"操作日志管理"}', '{"code":200,"message":"导出成功"}', '192.168.1.102', 1, NULL, 280, '2024-12-25 14:30:00', '2024-12-25 14:30:00', 0);

-- 9. 删除操作日志（成功）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '删除', '删除操作日志（ID=10）', 'DELETE', '/admin/operation-logs/10', NULL, '{"code":200,"message":"删除成功","data":null}', '192.168.1.103', 1, NULL, 65, '2024-12-25 15:00:00', '2024-12-25 15:00:00', 0);

-- 10. 删除操作日志（失败-不存在）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '删除', '删除操作日志（ID=99999，不存在）', 'DELETE', '/admin/operation-logs/99999', NULL, '{"code":404,"message":"操作日志不存在","data":null}', '192.168.1.103', 0, '操作日志不存在', 40, '2024-12-25 15:30:00', '2024-12-25 15:30:00', 0);

-- 11. 批量删除操作日志（成功）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '批量删除', '批量删除操作日志（IDs=[5,6,7,8,9]）', 'POST', '/admin/operation-logs/batch-delete', '{"ids":[5,6,7,8,9]}', '{"code":200,"message":"批量删除成功","data":null}', '192.168.1.104', 1, NULL, 120, '2024-12-25 16:00:00', '2024-12-25 16:00:00', 0);

-- 12. 批量删除操作日志（失败-参数为空）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '批量删除', '批量删除操作日志（参数为空）', 'POST', '/admin/operation-logs/batch-delete', '{"ids":[]}', '{"code":500,"message":"请选择要删除的操作日志","data":null}', '192.168.1.104', 0, '请选择要删除的操作日志', 25, '2024-12-25 16:30:00', '2024-12-25 16:30:00', 0);

-- 13. 访问操作日志接口（权限不足-非管理员）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (2, 'user001', '操作日志管理', '查询', '查询操作日志列表（权限不足）', 'GET', '/admin/operation-logs', NULL, '{"code":500,"message":"权限不足，仅管理员可访问","data":null}', '192.168.1.105', 0, '权限不足，仅管理员可访问', 15, '2024-12-25 17:00:00', '2024-12-25 17:00:00', 0);

-- 14. 访问操作日志接口（token无效）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (NULL, NULL, '操作日志管理', '查询', '查询操作日志列表（token无效）', 'GET', '/admin/operation-logs', NULL, '{"code":500,"message":"token无效或已过期","data":null}', '192.168.1.106', 0, 'token无效或已过期', 10, '2024-12-25 17:30:00', '2024-12-25 17:30:00', 0);

-- 15. 查询操作日志列表（按时间范围筛选）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志列表（时间范围：2024-12-20 00:00:00 至 2024-12-25 23:59:59）', 'GET', '/admin/operation-logs', '{"startTime":"2024-12-20 00:00:00","endTime":"2024-12-25 23:59:59"}', '{"code":200,"message":"success","data":[]}', '192.168.1.107', 1, NULL, 200, '2024-12-25 18:00:00', '2024-12-25 18:00:00', 0);

-- 16. 查询操作日志列表（按用户ID筛选）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志列表（用户ID=1）', 'GET', '/admin/operation-logs', '{"userId":1}', '{"code":200,"message":"success","data":[]}', '192.168.1.108', 1, NULL, 110, '2024-12-25 18:30:00', '2024-12-25 18:30:00', 0);

-- 17. 查询操作日志列表（按状态筛选-成功）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志列表（状态=成功）', 'GET', '/admin/operation-logs', '{"status":1}', '{"code":200,"message":"success","data":[]}', '192.168.1.109', 1, NULL, 85, '2024-12-25 19:00:00', '2024-12-25 19:00:00', 0);

-- 18. 查询操作日志列表（按状态筛选-失败）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志列表（状态=失败）', 'GET', '/admin/operation-logs', '{"status":0}', '{"code":200,"message":"success","data":[]}', '192.168.1.110', 1, NULL, 90, '2024-12-25 19:30:00', '2024-12-25 19:30:00', 0);

-- 19. 搜索操作日志（按日期范围）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '搜索', '搜索操作日志（日期范围：2024-12-20 至 2024-12-25）', 'GET', '/admin/operation-logs/search', '{"startDate":"2024-12-20","endDate":"2024-12-25"}', '{"code":200,"message":"success","data":[]}', '192.168.1.111', 1, NULL, 165, '2024-12-25 20:00:00', '2024-12-25 20:00:00', 0);

-- 20. 查询操作日志详情（成功-完整数据）
INSERT INTO `operation_log` (`user_id`, `username`, `module`, `operation_type`, `description`, `request_method`, `request_url`, `request_params`, `response_result`, `ip_address`, `status`, `error_message`, `duration`, `create_time`, `update_time`, `is_deleted`) 
VALUES (1, 'admin', '操作日志管理', '查询', '查询操作日志详情（ID=20，完整数据）', 'GET', '/admin/operation-logs/20', NULL, '{"code":200,"message":"success","data":{"id":20,"userId":1,"username":"admin","module":"操作日志管理","operationType":"查询","description":"查询操作日志详情","requestMethod":"GET","requestUrl":"/admin/operation-logs/20","requestParams":null,"responseResult":"{\\"code\\":200}","ipAddress":"192.168.1.111","status":1,"errorMessage":null,"duration":50,"createTime":"2024-12-25T20:00:00","updateTime":"2024-12-25T20:00:00"}}', '192.168.1.112', 1, NULL, 50, '2024-12-25 20:30:00', '2024-12-25 20:30:00', 0);






