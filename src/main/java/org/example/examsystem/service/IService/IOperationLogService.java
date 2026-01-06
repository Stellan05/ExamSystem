package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.examsystem.dto.BatchDeleteRequest;
import org.example.examsystem.dto.OperationLogQueryParams;
import org.example.examsystem.dto.OperationLogSearchParams;
import org.example.examsystem.entity.OperationLog;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志服务接口
 */
public interface IOperationLogService extends IService<OperationLog> {

    /**
     * 获取操作日志列表
     */
    List<OperationLog> getOperationLogs(OperationLogQueryParams params);

    /**
     * 获取操作日志详情
     */
    OperationLog getOperationLogDetail(Long id);

    /**
     * 搜索操作日志
     */
    List<OperationLog> searchOperationLogs(OperationLogSearchParams params);

    /**
     * 导出操作日志
     */
    void exportOperationLogs(OperationLogQueryParams params, HttpServletResponse response);

    /**
     * 删除操作日志
     */
    void deleteOperationLog(Long id);

    /**
     * 批量删除操作日志
     */
    void batchDeleteOperationLogs(BatchDeleteRequest request);
}

