package org.example.examsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.ExamQuestion;

import java.util.List;

public interface ExamQuestionMapper extends BaseMapper<ExamQuestion> {
    List<Long> getSubjectiveQuestionIds(Long examId);

    /**
     * 根据考试ID和题目ID查询关联记录（未删除）
     */
    ExamQuestion selectByExamIdAndQuestionId(@Param("examId") Long examId, @Param("questionId") Long questionId);
}
