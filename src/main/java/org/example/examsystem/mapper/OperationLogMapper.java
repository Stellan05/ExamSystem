package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.dto.OperationLogQueryParams;
import org.example.examsystem.dto.OperationLogSearchParams;
import org.example.examsystem.entity.OperationLog;

import java.util.List;

/**
 * 操作日志Mapper
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * 查询操作日志列表
     */
    List<OperationLog> getOperationLogs(@Param("params") OperationLogQueryParams params);

    /**
     * 搜索操作日志
     */
    List<OperationLog> searchOperationLogs(@Param("params") OperationLogSearchParams params);

    /**
     * 导出操作日志
     */
    List<OperationLog> exportOperationLogs(@Param("params") OperationLogQueryParams params);
}

