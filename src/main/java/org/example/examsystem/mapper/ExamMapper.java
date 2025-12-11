package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.vo.CreatorExamInfoVO;

import java.util.List;

public interface ExamMapper extends BaseMapper<Exam> {
    /**
     * 根据creatorId查询考试的详细信息，包括参与者和题目
     * @param creatorId 查询ID
     * @return 考试详细信息VO
     */
    List<CreatorExamInfoVO> getExamListByCreatorId(@Param("creatorId") Long creatorId);}
