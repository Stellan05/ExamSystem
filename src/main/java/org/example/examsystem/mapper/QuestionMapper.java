package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.dto.QuestionAnswerDTO;
import org.example.examsystem.entity.Question;
import org.example.examsystem.vo.QuestionSimpleInfoVO;

import java.util.List;

public interface QuestionMapper extends BaseMapper<Question> {
        List<QuestionSimpleInfoVO> getQuestions(@Param("examId")Long examId);

        List<QuestionAnswerDTO> selectQuestionsWithAnswer(@Param("examId") Long examId);
}
