package io.github.fushuwei.springsecurity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.fushuwei.springsecurity.mapper.UserMapper;
import io.github.fushuwei.springsecurity.model.dto.UserDTO;
import io.github.fushuwei.springsecurity.model.entity.UserDO;
import io.github.fushuwei.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final AuthenticationManager authenticationManager;

    @Override
    public String login(UserDTO userDTO) {
        // 获取登录用户名和密码
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        // 封装身份验证令牌
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // 发起认证
        Authentication authentication = authenticationManager.authenticate(token);

        // 判断认证是否通过
        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "用户 " + authentication.getName() + " 登录成功";
        }

        return null;
    }
}
