package org.example.examsystem.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.example.examsystem.entity.User;
import org.example.examsystem.mapper.UserMapper;
import org.example.examsystem.service.IService.EmployeeService;
import org.example.examsystem.utils.JwtUtils;
import org.example.examsystem.vo.EmployeeLoginVO;
import org.example.examsystem.vo.Result;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserMapper userMapper;

    @Override
    public Result login(String username, String password) {
        User user = userMapper.selectOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getEmail, username)
                        .eq(User::getPassword, password)
                        .eq(User::getIsDeleted, 0)
        );
        if (user == null) {
            return Result.info(401, "用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        claims.put("username", user.getEmail());
        String token = JwtUtils.generateJwt(claims);

        EmployeeLoginVO vo = new EmployeeLoginVO();
        vo.setId(user.getId());
        vo.setName(user.getRealName());
        vo.setUserName(user.getEmail());
        vo.setToken(token);

        return Result.ok(vo);
    }
}

