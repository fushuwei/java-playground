package io.github.fushuwei.springsecurity.result;

import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author fushuwei
 */
@Getter
public enum ResultCode {

    SUCCESS(10000, "操作成功"),
    WARNING(50000, "警告信息"),
    CONFIRM(70000, "确认信息"),
    FAILURE(99999, "操作失败"),
    ;

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
