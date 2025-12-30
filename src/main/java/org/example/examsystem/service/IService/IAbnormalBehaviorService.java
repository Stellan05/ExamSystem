package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.examsystem.dto.AbnormalBehaviorDTO;
import org.example.examsystem.entity.ExamAbnormalBehavior;
import org.example.examsystem.vo.AbnormalInfoVO;

import java.util.List;

public interface IAbnormalBehaviorService extends IService<ExamAbnormalBehavior> {
    int reportBehavior(Long userId, Long examId, List<AbnormalBehaviorDTO> dto);
    List<AbnormalInfoVO> getAllBehavior();
}
