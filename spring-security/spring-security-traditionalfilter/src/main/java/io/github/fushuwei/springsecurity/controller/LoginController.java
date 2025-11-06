package io.github.fushuwei.springsecurity.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LoginController {

    @GetMapping("/login")
    public String login(HttpSession session) {
        session.setAttribute("isLoggedIn", true);
        return "登录成功";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("isLoggedIn");
        return "退出登录";
    }
}
