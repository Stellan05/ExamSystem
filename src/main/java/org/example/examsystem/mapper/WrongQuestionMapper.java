package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.examsystem.vo.WrongQuestionVO;

public interface WrongQuestionMapper extends BaseMapper<WrongQuestionVO> {

    IPage<WrongQuestionVO> getWrongQuestionsByType(Page<?> page, @Param("type")Integer type);
}
