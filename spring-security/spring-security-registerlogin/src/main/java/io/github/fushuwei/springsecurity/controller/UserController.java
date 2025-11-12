package io.github.fushuwei.springsecurity.controller;

import io.github.fushuwei.springsecurity.entity.UserDO;
import io.github.fushuwei.springsecurity.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/add")
    public String add(@RequestBody UserDO userDO) {
        UserDetails user = User.builder()
            .username(userDO.getUsername())
            .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userDO.getPassword()))
            .build();
        customUserDetailsService.createUser(user);
        return "添加用户成功！";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody UserDO userDO) {
        customUserDetailsService.deleteUser(userDO.getUsername());
        return "删除用户成功！";
    }
}
