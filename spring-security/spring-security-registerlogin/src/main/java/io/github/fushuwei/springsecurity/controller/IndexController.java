package io.github.fushuwei.springsecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    public String index() {
        for (int i = 0; i < 10; i++) {
            log.info("Hello World {}", i);
        }
        return "Hello World";
    }
}
