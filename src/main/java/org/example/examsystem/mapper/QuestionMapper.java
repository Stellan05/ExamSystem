package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.Question;
import org.example.examsystem.vo.QuestionSimpleInfoVO;

import java.util.List;

public interface QuestionMapper extends BaseMapper<Question> {
    List<QuestionSimpleInfoVO> getQuestions(@Param("examId") Long examId);

    @Insert("INSERT INTO exam_question(question_id, exam_id, score, create_time, update_time) VALUES(#{questionId}, #{examId}, #{score}, NOW(), NOW())")
    void associateQuestionWithExam(@Param("questionId") Long questionId,
                                   @Param("examId") Long examId,
                                   @Param("score") Integer score);
}

