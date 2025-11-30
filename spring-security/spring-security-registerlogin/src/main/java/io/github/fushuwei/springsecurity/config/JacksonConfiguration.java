package io.github.fushuwei.springsecurity.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Configuration
public class JacksonConfiguration {

    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return builder -> {
            // 统一处理 Long 类型，在 JSON 中序列化为字符串，防止前端精度丢失
            // 前端 JavaScript 的 Number 类型无法精确表示超过 Number.MAX_SAFE_INTEGER (即 2^53 - 1) 的大整数，而 Java 的 Long 类型远大于这个值
            builder.addModule(new SimpleModule().addSerializer(Long.class, ToStringSerializer.instance));
        };
    }
}
