package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.dto.CreateExamRequest;
import org.example.examsystem.vo.QuestionSimpleInfoVO;
import org.example.examsystem.vo.RankInfoVO;
import org.example.examsystem.vo.UserSimpleInfoVO;
import org.example.examsystem.vo.Result;

import java.util.List;

/**
 * 试卷类接口
 */
public interface IExamService extends IService<Exam>{

    Long getAllCount(Long examId);

    RankInfoVO getMyRank(Long examId, Long userId);

    Page<?> getCreatorExams(Long userId,Integer page, Integer pageSize);

    Page<?> getTesterExams(Long userId,Integer page, Integer pageSize);

    Page<UserSimpleInfoVO> getAllTesters(Long userId, Integer page, Integer pageSize);

    List<QuestionSimpleInfoVO> getQuestions(@Param("examId")Long examId);

    /**
     * 创建考试（含题目绑定）
     */
    Result createExam(CreateExamRequest request);
}
