package io.github.fushuwei.springsecurity.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.fushuwei.springsecurity.model.entity.UserDO;
import io.github.fushuwei.springsecurity.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .build();
    }
}
