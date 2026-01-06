package org.example.examsystem.service.IService;

import org.example.examsystem.dto.CreateQuestionRequest;
import org.example.examsystem.dto.SetQuestionAnswerRequest;

public interface IQuestionService {

    /**
     * 创建题目（带选项）
     * @return 题目ID
     */
    Long createQuestion(CreateQuestionRequest request);

    /**
     * 设置题目的标准答案与解析
     */
    void setQuestionAnswer(Long questionId, SetQuestionAnswerRequest request);

    /**
     * 删除题目
     */
    void deleteQuestion(Long questionId);

    /**
     * 绑定题目到考试（写 exam_question）
     */
    void associateQuestionWithExam(Long questionId, Long examId, Integer score);

    /**
     * 修改题目在考试中的分数
     */
    void updateQuestionScore(Long examId, Long questionId, Integer score);

    /**
     * 从某场考试中移除题目（删除 exam_question 关联）
     */
    void removeQuestionFromExam(Long examId, Long questionId);
}