package io.github.fushuwei.springsecurity;

import io.github.fushuwei.springsecurity.mapper.UserMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Data
@SpringBootTest
public class UserTest {

    private final UserMapper userMapper;

    @Test
    public void queryAllUsers() {
        userMapper.selectList(null);
    }
}
