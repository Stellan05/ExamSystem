package org.example.examsystem.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.examsystem.dto.BatchDeleteRequest;
import org.example.examsystem.dto.OperationLogQueryParams;
import org.example.examsystem.dto.OperationLogSearchParams;
import org.example.examsystem.entity.OperationLog;
import org.example.examsystem.mapper.OperationLogMapper;
import org.example.examsystem.service.IService.IOperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 操作日志服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    private final OperationLogMapper operationLogMapper;

    @Override
    public List<OperationLog> getOperationLogs(OperationLogQueryParams params) {
        if (params == null) {
            params = new OperationLogQueryParams();
        }

        return operationLogMapper.getOperationLogs(params);
    }

    @Override
    public OperationLog getOperationLogDetail(Long id) {
        OperationLog operationLog = operationLogMapper.selectById(id);
        if (operationLog == null || (operationLog.getIsDeleted() != null && operationLog.getIsDeleted() == 1)) {
            return null;
        }
        return operationLog;
    }

    @Override
    public List<OperationLog> searchOperationLogs(OperationLogSearchParams params) {
        if (params == null) {
            params = new OperationLogSearchParams();
        }

        return operationLogMapper.searchOperationLogs(params);
    }

    @Override
    public void exportOperationLogs(OperationLogQueryParams params, HttpServletResponse response) {
        try {
            List<OperationLog> logs = operationLogMapper.exportOperationLogs(params);

            // 设置响应头
            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=operation_logs_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv");

            // 写入CSV数据
            PrintWriter writer = response.getWriter();
            // 写入表头
            writer.println("ID,用户名,模块,操作类型,操作描述,请求方法,请求URL,IP地址,状态,操作时间");
            // 写入数据
            for (OperationLog log : logs) {
                writer.println(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        log.getId(),
                        escapeCsv(log.getUsername()),
                        escapeCsv(log.getModule()),
                        escapeCsv(log.getOperationType()),
                        escapeCsv(log.getDescription()),
                        escapeCsv(log.getRequestMethod()),
                        escapeCsv(log.getRequestUrl()),
                        escapeCsv(log.getIpAddress()),
                        log.getStatus() == 1 ? "成功" : "失败",
                        log.getCreateTime() != null ? log.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : ""
                ));
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("导出操作日志失败", e);
            throw new RuntimeException("导出操作日志失败", e);
        }
    }

    /**
     * CSV字段转义
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        // 如果包含逗号、引号或换行符，需要用引号包裹，并转义引号
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOperationLog(Long id) {
        OperationLog operationLog = operationLogMapper.selectById(id);
        if (operationLog == null || (operationLog.getIsDeleted() != null && operationLog.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("操作日志不存在");
        }

        operationLogMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteOperationLogs(BatchDeleteRequest request) {
        if (request == null || request.getIds() == null || request.getIds().isEmpty()) {
            throw new IllegalArgumentException("请选择要删除的操作日志");
        }

        List<Long> ids = request.getIds();
        for (Long id : ids) {
            operationLogMapper.deleteById(id);
        }
    }
}

