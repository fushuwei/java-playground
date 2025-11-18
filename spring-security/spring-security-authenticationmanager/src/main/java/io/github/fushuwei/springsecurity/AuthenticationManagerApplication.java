package io.github.fushuwei.springsecurity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("io.github.fushuwei.springsecurity.mapper")
@SpringBootApplication
public class AuthenticationManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationManagerApplication.class, args);
    }
}
