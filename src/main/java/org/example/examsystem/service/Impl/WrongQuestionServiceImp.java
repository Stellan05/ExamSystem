package org.example.examsystem.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.examsystem.mapper.WrongQuestionMapper;
import org.example.examsystem.service.IService.IWrongQuestionService;
import org.example.examsystem.vo.WrongQuestionVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WrongQuestionServiceImp extends ServiceImpl<WrongQuestionMapper, WrongQuestionVO> implements IWrongQuestionService {

    private final WrongQuestionMapper wrongQuestionMapper;

    /**
     * 按照错题时间查询错题列表
     * @param studentId 学生Id
     * @return 错题列表，如果为空返回null
     */
    @Override
    public List<WrongQuestionVO> getWrongQuestions(Long studentId) {
        List<WrongQuestionVO> wrongQuestions = query()
                .eq("student_id", studentId)
                .orderByAsc("create_time")
                .list();
        if(wrongQuestions == null || wrongQuestions.isEmpty()){
            return null;
        }
        return wrongQuestions;
    }

    /**
     * 根据错题类型查询错题
     * @param questionType 错题类型
     * @param page 当前页
     * @param pageSize 每页数量
     * @return VO
     */
    @Override
    public IPage<WrongQuestionVO> getWrongQuestionsByType(Integer questionType, Integer page, Integer pageSize) {
        Page<WrongQuestionVO> voPage = new Page<>(page,pageSize);
        return wrongQuestionMapper.getWrongQuestionsByType(voPage,questionType);
    }
}
