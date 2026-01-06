package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.entity.Exam;
import org.example.examsystem.dto.CreateExamRequest;
import org.example.examsystem.dto.UpdateExamBasicInfoRequest;
import org.example.examsystem.vo.QuestionSimpleInfoVO;
import org.example.examsystem.vo.RankInfoVO;
import org.example.examsystem.vo.UserSimpleInfoVO;

import java.util.List;
import java.util.Map;

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
     * @return Map包含examId、examCode、questions，如果创建失败抛出异常
     */
    Map<String, Object> createExam(CreateExamRequest request);

    /**
     * 修改试卷/考试基本信息（仅创建者可修改）
     */
    void updateExamBasicInfo(Long examId, Long creatorId, UpdateExamBasicInfoRequest request);

    /**
     * 完成试卷编辑（验证试卷完整性）
     * - 检查题目数量（至少1道）
     * - 检查所有题目是否有标准答案（主观题可选）
     * - 检查所有题目是否有分数
     */
    void completeExamEdit(Long examId, Long creatorId);
}
