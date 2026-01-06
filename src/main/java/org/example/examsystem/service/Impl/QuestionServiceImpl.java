package org.example.examsystem.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.examsystem.dto.CreateQuestionRequest;
import org.example.examsystem.dto.SetQuestionAnswerRequest;
import org.example.examsystem.entity.ExamQuestion;
import org.example.examsystem.entity.Question;
import org.example.examsystem.entity.QuestionAnswer;
import org.example.examsystem.entity.QuestionOption;
import org.example.examsystem.mapper.ExamQuestionMapper;
import org.example.examsystem.mapper.QuestionAnswerMapper;
import org.example.examsystem.mapper.QuestionMapper;
import org.example.examsystem.mapper.QuestionOptionMapper;
import org.example.examsystem.service.IService.IQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper questionOptionMapper;
    private final QuestionAnswerMapper questionAnswerMapper;
    private final ExamQuestionMapper examQuestionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createQuestion(CreateQuestionRequest request) {
        if (request.getCreatorId() == null) {
            throw new IllegalArgumentException("出题人ID不能为空");
        }
        if (!StringUtils.hasText(request.getQuestionType())) {
            throw new IllegalArgumentException("题型不能为空");
        }
        if (!StringUtils.hasText(request.getContent())) {
            throw new IllegalArgumentException("题干不能为空");
        }

        int questionType;
        try {
            questionType = Integer.parseInt(request.getQuestionType());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("题型必须是数字：1单选 2多选 3判断 4填空 5主观");
        }
        if (questionType < 1 || questionType > 5) {
            throw new IllegalArgumentException("题型必须是1-5之间的数字");
        }

        // 解析选项（仅选择/判断题需要）
        List<String> optionList = new ArrayList<>();
        if (questionType == 1 || questionType == 2 || questionType == 3) {
            if (!StringUtils.hasText(request.getOptions())) {
                throw new IllegalArgumentException("选择/判断题需要提供选项");
            }
            String[] parts = request.getOptions().split("[,\\n]");
            for (String p : parts) {
                if (StringUtils.hasText(p)) {
                    optionList.add(p.trim());
                }
            }
            if (optionList.isEmpty()) {
                throw new IllegalArgumentException("选项不能为空");
            }
        }

        // 写入题干
        Question question = new Question();
        question.setQuestionType(questionType);
        question.setContent(request.getContent());
        question.setCreatorId(request.getCreatorId());
        questionMapper.insert(question);

        // 写入选项
        if (!optionList.isEmpty()) {
            char key = 'A';
            for (String opt : optionList) {
                QuestionOption qo = new QuestionOption();
                qo.setQuestionId(question.getId());
                qo.setOptionKey(String.valueOf(key));
                qo.setOptionText(opt);
                questionOptionMapper.insert(qo);
                key++;
            }
        }

        return question.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setQuestionAnswer(Long questionId, SetQuestionAnswerRequest request) {
        if (questionId == null) {
            throw new IllegalArgumentException("题目ID不能为空");
        }
        if (!StringUtils.hasText(request.getCorrectAnswer())) {
            throw new IllegalArgumentException("标准答案不能为空");
        }

        Question question = questionMapper.selectById(questionId);
        if (question == null || (question.getIsDeleted() != null && question.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("题目不存在");
        }

        // 查询是否已有答案记录，存在则更新，否则插入
        QuestionAnswer qa = questionAnswerMapper.selectByQuestionId(questionId);
        if (qa == null) {
            qa = new QuestionAnswer();
            qa.setQuestionId(questionId);
        }
        qa.setCorrectAnswer(request.getCorrectAnswer());
        qa.setAnswerAnalysis(request.getAnswerAnalysis());

        if (qa.getId() == null) {
            questionAnswerMapper.insert(qa);
        } else {
            questionAnswerMapper.updateById(qa);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestion(Long questionId) {
        if (questionId == null) {
            throw new IllegalArgumentException("题目ID不能为空");
        }
        Question question = questionMapper.selectById(questionId);
        if (question == null || (question.getIsDeleted() != null && question.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("题目不存在");
        }
        questionMapper.deleteById(questionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associateQuestionWithExam(Long questionId, Long examId, Integer score) {
        if (questionId == null || examId == null) {
            throw new IllegalArgumentException("题目ID和考试ID不能为空");
        }
        if (score == null || score < 0) {
            score = 0;
        }

        ExamQuestion existing = examQuestionMapper.selectByExamIdAndQuestionId(examId, questionId);
        if (existing != null && (existing.getIsDeleted() == null || existing.getIsDeleted() == 0)) {
            existing.setScore(score.doubleValue());
            examQuestionMapper.updateById(existing);
            return;
        }

        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExamId(examId);
        examQuestion.setQuestionId(questionId);
        examQuestion.setScore(score.doubleValue());
        examQuestion.setIsDeleted(0);
        int insertResult = examQuestionMapper.insert(examQuestion);
        if (insertResult <= 0) {
            throw new RuntimeException("插入关联记录失败: examId=" + examId + ", questionId=" + questionId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuestionScore(Long examId, Long questionId, Integer score) {
        if (examId == null || questionId == null) {
            throw new IllegalArgumentException("考试ID和题目ID不能为空");
        }
        if (score == null || score < 0) {
            throw new IllegalArgumentException("分数不能为空且必须大于等于0");
        }

        ExamQuestion existing = examQuestionMapper.selectByExamIdAndQuestionId(examId, questionId);
        if (existing == null || (existing.getIsDeleted() != null && existing.getIsDeleted() == 1)) {
            throw new RuntimeException("题目未关联到该考试，请先关联题目到考试");
        }
        existing.setScore(score.doubleValue());
        examQuestionMapper.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeQuestionFromExam(Long examId, Long questionId) {
        if (examId == null || questionId == null) {
            throw new IllegalArgumentException("考试ID和题目ID不能为空");
        }
        ExamQuestion existing = examQuestionMapper.selectByExamIdAndQuestionId(examId, questionId);
        if (existing == null || (existing.getIsDeleted() != null && existing.getIsDeleted() == 1)) {
            // 这里按幂等处理：不存在就当成功
            return;
        }
        // 逻辑删除 exam_question 关联
        examQuestionMapper.deleteById(existing.getId());
    }
}

