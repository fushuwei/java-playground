package io.github.fushuwei.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 配置管理服务启动类
 *
 * @author example
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ConfigManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigManagerApplication.class, args);
    }
}
