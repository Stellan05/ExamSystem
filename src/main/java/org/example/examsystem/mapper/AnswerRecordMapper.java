package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.AnswerRecord;
import org.example.examsystem.vo.QuestionDetailVO;

import java.util.List;

public interface AnswerRecordMapper extends BaseMapper<AnswerRecord> {
    List<QuestionDetailVO> getQuestionDetails(@Param("examId") Long examId, @Param("userId") Long userId);
}
