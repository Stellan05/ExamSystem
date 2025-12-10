package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.dto.StudentReviewCount;
import org.example.examsystem.entity.AnswerRecord;
import org.example.examsystem.vo.QuestionDetailVO;
import org.example.examsystem.vo.TeacherReviewQuestionDetailVO;

import java.util.List;

public interface AnswerRecordMapper extends BaseMapper<AnswerRecord> {

    List<QuestionDetailVO> getQuestionDetails(@Param("examId") Long examId, @Param("userId") Long userId);

    TeacherReviewQuestionDetailVO getTeacherReviewDetail(
            @Param("examId") Long examId,
            @Param("questionId") Long questionId,
            @Param("studentId") Long studentId
    );

    Integer countSubjectiveQuestions(@Param("examId") Long examId);

    List<StudentReviewCount> countReviewedPerStudent(@Param("examId")Long examId);
}
