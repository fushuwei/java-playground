package io.github.fushuwei.springsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fushuwei.springsecurity.result.Result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@SpringBootTest
public class ResultTest {

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void ok() {
        log.info("测试 Result.ok()\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.ok()));
    }

    @SneakyThrows
    @Test
    public void warn() {
        log.info("测试 Result.warn()\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.warn("用户被使用，不允许删除")));
    }

    @SneakyThrows
    @Test
    public void confirm() {
        log.info("测试 Result.confirm()\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.confirm("确定要清空回收站吗？一旦清空无法撤回", "token123")));
    }

    @SneakyThrows
    @Test
    public void fail() {
        log.info("测试 Result.fail()\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.fail("系统繁忙，请稍后重试")));
    }

    @Test
    public void timestamp2Date() {
        long timestamp = System.currentTimeMillis();
        log.info("时间戳: 「{}」", timestamp);

        Instant instant = Instant.ofEpochMilli(timestamp);
        log.info("UTC 时间: {}", instant);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        log.info("当前服务器时区时间: {}", localDateTime);

        String formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("格式化日期字符串: {}", formattedDate);

        LocalDateTime shanghaiLocalDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Shanghai"));
        log.info("上海时间: {}", shanghaiLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LocalDateTime moscowLocalDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Moscow"));
        log.info("莫斯科时间: {}", moscowLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LocalDateTime newyorkLocalDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("America/New_York"));
        log.info("纽约时间: {}", newyorkLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
