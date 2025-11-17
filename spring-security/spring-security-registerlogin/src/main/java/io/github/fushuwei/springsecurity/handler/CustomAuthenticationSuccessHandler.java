package io.github.fushuwei.springsecurity.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fushuwei.springsecurity.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        out.write(objectMapper.writeValueAsString(Result.ok(user.getUsername() + " 登录成功")));
    }
}
