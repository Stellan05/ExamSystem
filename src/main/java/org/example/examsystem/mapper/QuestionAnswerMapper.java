package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.QuestionAnswer;

public interface QuestionAnswerMapper extends BaseMapper<QuestionAnswer> {
    /**
     * 根据题目ID查询答案（走 XML）
     */
    QuestionAnswer selectByQuestionId(@Param("questionId") Long questionId);
}

