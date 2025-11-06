package io.github.fushuwei.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "io.github.fushuwei.springsecurity.filter")
public class TraditionalFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TraditionalFilterApplication.class, args);
    }
}
