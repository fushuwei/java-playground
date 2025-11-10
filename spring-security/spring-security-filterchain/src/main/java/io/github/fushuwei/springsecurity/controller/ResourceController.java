package io.github.fushuwei.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @GetMapping("/users")
    public String getUsers() {
        return "张三，李四，王五";
    }

    @GetMapping("/roles")
    public String getRoles() {
        return "系统管理员，普通用户";
    }
}
