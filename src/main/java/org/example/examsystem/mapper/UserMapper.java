package org.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.examsystem.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
