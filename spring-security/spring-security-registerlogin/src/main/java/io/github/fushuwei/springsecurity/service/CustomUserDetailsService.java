package io.github.fushuwei.springsecurity.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.fushuwei.springsecurity.entity.UserDO;
import io.github.fushuwei.springsecurity.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsManager, UserDetailsPasswordService {

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

    /**
     * updatePassword 方法不是由你的应用代码显式调用的。它是由 Spring Security 框架在特定时机自动调用的，
     * 它的主要目的是为了自动升级用户密码的编码方式，例如从明文或不安全的旧哈希算法（如 MD5）升级到更安全的算法（如 bcrypt），
     * 当用户成功登录认证后，如果配置了密码升级策略（如当前方法便是配置了升级策略），Spring Security 会自动调用该方法。
     * <p>
     * data.sql 脚本中初始化了一个 test 用户，密码是 {noop} 明文存储，使用该用户登录看看数据库中密码字段值有什么变化
     *
     * @param newPassword 由 Spring Security 框架自动生成安全加密算法对应的密码，不需要手动进行生成密文
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        userMapper.update(Wrappers.<UserDO>lambdaUpdate()
            .eq(UserDO::getUsername, user.getUsername())
            .set(UserDO::getPassword, newPassword));
        return user;
    }
}
