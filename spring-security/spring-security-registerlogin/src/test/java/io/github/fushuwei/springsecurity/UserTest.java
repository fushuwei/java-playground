package io.github.fushuwei.springsecurity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.fushuwei.springsecurity.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void queryAllUsers() {
        userMapper.selectList(new QueryWrapper<>());
    }
}
