package io.github.fushuwei.springsecurity.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.fushuwei.springsecurity.entity.UserDO;
import io.github.fushuwei.springsecurity.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsManager {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password("{bcrypt}" + user.getPassword())
            .build();
    }

    @Override
    public void createUser(UserDetails user) {
        UserDO dbUser = new UserDO();
        dbUser.setUsername(user.getUsername());
        dbUser.setPassword(user.getPassword());
        userMapper.insert(dbUser);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {
        userMapper.delete(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUsername, username));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
