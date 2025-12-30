package org.example.examsystem.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.examsystem.dto.AbnormalBehaviorDTO;
import org.example.examsystem.entity.ExamAbnormalBehavior;
import org.example.examsystem.mapper.AbnormalBehaviorMapper;
import org.example.examsystem.service.IService.IAbnormalBehaviorService;
import org.example.examsystem.vo.AbnormalInfoVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AbnormalBehaviorServiceImpl extends ServiceImpl<AbnormalBehaviorMapper, ExamAbnormalBehavior> implements IAbnormalBehaviorService {
    /**
     * 插入 异常记录
     * @param userId 用户ID
     * @param examId 考试ID
     * @param list dto
     */
    @Override
    public int reportBehavior(Long userId, Long examId, List<AbnormalBehaviorDTO> list) {
        if(list==null|| list.isEmpty()){
            return 0;
        }
        List<ExamAbnormalBehavior> entities = new ArrayList<>(list.size());
        for (AbnormalBehaviorDTO dto : list) {
            ExamAbnormalBehavior behavior = new ExamAbnormalBehavior();
            behavior.setExamId(examId);
            behavior.setUserId(userId);
            behavior.setBehaviorType(dto.getBehaviorType());
            behavior.setRemark(dto.getRemark());
            behavior.setOccurTime(dto.getOccurTime());
            entities.add(behavior);
        }

        boolean success = this.saveBatch(entities);
        return success ? entities.size() : 0;
    }


}
