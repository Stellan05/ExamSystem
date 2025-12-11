package org.example.examsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.examsystem.entity.ExamQuestion;

import java.util.List;

public interface ExamQuestionMapper extends BaseMapper<ExamQuestion> {
    List<Long> getSubjectiveQuestionIds(Long examId);
}
