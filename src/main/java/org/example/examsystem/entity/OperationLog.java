package org.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@TableName("operation_log")
public class OperationLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户ID
     */
    private Long userId;

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 操作模块（如：考试管理、题目管理、用户管理等）
     */
    private String module;

    /**
     * 操作类型（如：创建、修改、删除、查询等）
     */
    private String operationType;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求方法（GET、POST、PUT、DELETE等）
     */
    private String requestMethod;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应结果
     */
    private String responseResult;

    /**
     * 操作IP地址
     */
    private String ipAddress;

    /**
     * 操作状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * 错误信息（如果操作失败）
     */
    private String errorMessage;

    /**
     * 操作耗时（毫秒）
     */
    private Long duration;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;
}













