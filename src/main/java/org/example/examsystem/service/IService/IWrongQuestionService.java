package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.examsystem.vo.Result;
import org.example.examsystem.vo.WrongQuestionVO;

/**
 * 学生错题服务类接口
 */
public interface IWrongQuestionService extends IService<WrongQuestionVO> {

    Result getWrongQuestions(Long studentId);


    IPage<WrongQuestionVO> getWrongQuestionsByType(Integer questionType,Integer page,Integer pageSize);
}
