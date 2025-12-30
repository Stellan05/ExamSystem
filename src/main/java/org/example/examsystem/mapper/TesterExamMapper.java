package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.vo.*;

import java.util.List;
import java.util.Map;

public interface TesterExamMapper extends BaseMapper<TesterExam> {

    Page<UserSimpleInfoVO> getAllTestersByPage(Page<?> page,@Param("examId") Long examId);

    Page<TesterExamInfoVO> getExamsByTesterId(Page<?> page, @Param("userId") Long userId);

    Page<CreatorExamInfoVO> getExamsByCreatorId(Page<?> page, @Param("userId") Long userId);

    RankInfoVO getRank(@Param("examId") Long examId, @Param("studentId") Long studentId);

    Page<GradeInfoVO> getGrades(Page<?> page, @Param("examId") Long examId);

    // 基本统计
    Map<String, Object> getBasicStats(@Param("examId") Long examId);

    // 分数段统计
    List<Map<String, Object>> getScoreRanges(
            @Param("examId") Long examId,
            @Param("ranges") List<Map<String, Integer>> ranges
    );

}
