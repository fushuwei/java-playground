package io.github.fushuwei.springsecurity.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.fushuwei.springsecurity.entity.User;
import io.github.fushuwei.springsecurity.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password("{bcrypt}" + user.getPassword())
            .build();
    }
}
