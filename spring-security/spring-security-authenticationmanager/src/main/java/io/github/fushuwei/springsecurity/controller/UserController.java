package io.github.fushuwei.springsecurity.controller;

import io.github.fushuwei.springsecurity.model.dto.UserDTO;
import io.github.fushuwei.springsecurity.result.Result;
import io.github.fushuwei.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody UserDTO userDto) throws Exception {
        return Result.ok(userService.login(userDto));
    }

    @GetMapping("/list")
    public String list() {
        return "list";
    }
}
