package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.vo.GradeInfoVO;
import org.example.examsystem.vo.RankInfoVO;
import org.example.examsystem.vo.UserSimpleInfoVO;

import java.util.List;

public interface TesterExamMapper extends BaseMapper<TesterExam> {

    Page<UserSimpleInfoVO> getAllTestersByPage(Page<?> page,@Param("examId") Long examId);

    Page<Exam> getExamsByTesterId(Page<?> page,@Param("userId") Long userId);

    Page<Exam> getExamsByCreatorId(Page<?> page,@Param("userId") Long userId);

    RankInfoVO getRank(@Param("examId") Long examId, @Param("studentId") Long studentId);

    Page<GradeInfoVO> getGrades(Page<?> page, @Param("examId") Long examId);
}
