package io.github.fushuwei.springsecurity.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author miemie
 * @since 2018-08-10
 */
@Configuration
@MapperScan("io.github.fushuwei.springsecurity.mapper")
public class MybatisPlusConfiguration {
}
