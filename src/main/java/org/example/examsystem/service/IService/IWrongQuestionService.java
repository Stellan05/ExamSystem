package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.examsystem.vo.QuestionAnswerVO;
import org.example.examsystem.vo.RandomWrongQuestionVO;
import org.example.examsystem.vo.WrongQuestionVO;

import java.util.List;

/**
 * 学生错题服务类接口
 */
public interface IWrongQuestionService extends IService<WrongQuestionVO> {

    /**
     * 按照错题时间查询错题列表
     * @param studentId 学生ID
     * @return 错题列表，如果为空返回null
     */
    List<WrongQuestionVO> getWrongQuestions(Long studentId);

    IPage<WrongQuestionVO> getWrongQuestionsByType(Integer questionType,Integer page,Integer pageSize);

    /**
     * 获取随机错题（不含答案）
     * @param studentId 学生ID
     * @param excludeQuestionIds 排除的题目ID列表（可为空）
     * @return 随机错题信息，无错题时返回null
     */
    RandomWrongQuestionVO getRandomWrongQuestion(Long studentId, List<Long> excludeQuestionIds);

    QuestionAnswerVO getQuestionAnswer(Long questionId);
}
