package io.github.fushuwei.springsecurity.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            // 统一处理 Long 类型，在 JSON 中序列化为字符串，防止前端精度丢失
            // 前端 JavaScript 的 Number 类型无法精确表示超过 Number.MAX_SAFE_INTEGER (即 2^53 - 1) 的大整数，而 Java 的 Long 类型远大于这个值。当后端返回一个很大的 Long 型 ID 时，前端会发生精度丢失。
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }
}
