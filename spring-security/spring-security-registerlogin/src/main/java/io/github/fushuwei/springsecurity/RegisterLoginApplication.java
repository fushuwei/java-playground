package io.github.fushuwei.springsecurity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("io.github.fushuwei.springsecurity.mapper")
public class RegisterLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterLoginApplication.class, args);
    }
}
