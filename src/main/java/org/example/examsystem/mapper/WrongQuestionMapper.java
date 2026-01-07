package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.vo.RandomWrongQuestionVO;
import org.example.examsystem.vo.WrongQuestionVO;

import java.util.List;

public interface WrongQuestionMapper extends BaseMapper<WrongQuestionVO> {

    IPage<WrongQuestionVO> getWrongQuestionsByType(Page<?> page, @Param("type")Integer type);

        /**
         * 查询学生的随机错题（不含答案）
         * @param studentId 学生ID
         * @param excludeQuestionIds 排除的题目ID列表（可为空）
         * @return 随机错题信息，无错题时返回null
         */
        RandomWrongQuestionVO getRandomWrongQuestion(@Param("studentId") Long studentId,
                                                     @Param("excludeQuestionIds") List<Long> excludeQuestionIds);
    }

