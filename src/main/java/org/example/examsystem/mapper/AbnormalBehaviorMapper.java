package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.examsystem.entity.ExamAbnormalBehavior;
import org.example.examsystem.vo.AbnormalInfoVO;

import java.util.List;

public interface AbnormalBehaviorMapper extends BaseMapper<ExamAbnormalBehavior> {
    List<AbnormalInfoVO> listAbnormalInfo(Long examId);
}
