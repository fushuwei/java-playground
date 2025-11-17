package io.github.fushuwei.springsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fushuwei.springsecurity.result.Result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ResultTest {

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void ok() {
        log.info("测试 Result.ok()\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.ok()));
    }

    @SneakyThrows
    @Test
    public void warn() {
        log.info("测试 Result.warn()\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.warn("用户被使用，不允许删除")));
    }

    @SneakyThrows
    @Test
    public void confirm() {
        log.info("测试 Result.confirm()\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.confirm("确定要清空回收站吗？一旦清空无法撤回", "token123")));
    }

    @SneakyThrows
    @Test
    public void fail() {
        log.info("测试 Result.fail()\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.fail("系统繁忙，请稍后重试")));
    }
}
