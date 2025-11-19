package io.github.fushuwei.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.fushuwei.springsecurity.mapper.UserMapper;
import io.github.fushuwei.springsecurity.model.converter.UserConverter;
import io.github.fushuwei.springsecurity.model.dto.UserDTO;
import io.github.fushuwei.springsecurity.model.entity.UserDO;
import io.github.fushuwei.springsecurity.model.vo.UserVO;
import io.github.fushuwei.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public void login(UserDTO userDTO) {
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
        }
    }

    @Override
    public List<UserVO> queryList(UserDTO userDto) {
        // 查询数据
        List<UserDO> userDoList = userMapper.selectList(
            Wrappers.<UserDO>lambdaQuery().eq(
                userDto.getUsername() != null && !userDto.getUsername().isEmpty(),
                UserDO::getUsername, userDto.getUsername()));

        // 将 DO 转成 VO
        return UserConverter.INSTANCE.doList2VoList(userDoList);
    }
}
