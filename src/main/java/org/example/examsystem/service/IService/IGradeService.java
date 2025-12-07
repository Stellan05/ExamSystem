package org.example.examsystem.service.IService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.examsystem.entity.TesterExam;
import org.example.examsystem.vo.GradeInfoVO;

public interface IGradeService extends IService<TesterExam> {
    Page<GradeInfoVO> getGrades(Long examId, Integer page, Integer pageSize);
}
