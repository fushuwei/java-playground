package io.github.fushuwei.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ResourceController {

    @GetMapping({"/resource1", "/resource/1"})
    public String resource1() {
        return "您正在访问资源服务器1";
    }

    @GetMapping({"/resource2", "/resource/2"})
    public String resource2() {
        return "您正在访问资源服务器2";
    }
}
