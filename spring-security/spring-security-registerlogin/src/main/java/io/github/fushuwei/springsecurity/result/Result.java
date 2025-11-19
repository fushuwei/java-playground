package io.github.fushuwei.springsecurity.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.MDC;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 响应结果
 *
 * @param <T> 响应数据类型
 * @author fushuwei
 */
@Data
@Accessors(fluent = true, chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private T data;

    /**
     * 响应类型：SUCCESS, WARNING, CONFIRM, FAILURE
     */
    private ResultType type;

    /**
     * 二次确认时的令牌，前端需要在确认时回传，防止重放攻击和确保操作的一致性
     */
    private String confirmToken;

    /**
     * 请求ID，用于链路追踪
     */
    private String requestId;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 构造方法
     */
    private Result() {
        this.requestId = MDC.get("requestId");  // X-Request-ID
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造方法
     */
    private Result(Integer code, String message, T data, ResultType type) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
        this.type = type;
    }

    /**
     * 构造方法
     */
    private Result(Integer code, String message, T data, ResultType type, String confirmToken) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
        this.type = type;
        this.confirmToken = confirmToken;
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> ok() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null, ResultType.SUCCESS);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> ok(String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, null, ResultType.SUCCESS);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, ResultType.SUCCESS);
    }

    /**
     * 成功响应（自定义消息和数据）
     */
    public static <T> Result<T> ok(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data, ResultType.SUCCESS);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> fail() {
        return new Result<>(ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMessage(), null, ResultType.FAILURE);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.FAILURE.getCode(), message, null, ResultType.FAILURE);
    }

    /**
     * 警告响应
     */
    public static <T> Result<T> warn(String message) {
        return new Result<>(ResultCode.WARNING.getCode(), message, null, ResultType.WARNING);
    }

    /**
     * 警告响应（带数据）
     */
    public static <T> Result<T> warn(T data, String message) {
        return new Result<>(ResultCode.WARNING.getCode(), message, data, ResultType.WARNING);
    }

    /**
     * 需要二次确认的响应
     */
    public static <T> Result<T> confirm(String message, String confirmToken) {
        return new Result<>(ResultCode.CONFIRM.getCode(), message, null, ResultType.CONFIRM, confirmToken);
    }

    /**
     * 需要二次确认的响应（带提示数据）
     */
    public static <T> Result<T> confirm(T data, String message, String confirmToken) {
        return new Result<>(ResultCode.CONFIRM.getCode(), message, data, ResultType.CONFIRM, confirmToken);
    }

    /**
     * 自定义状态码响应
     */
    public static <T> Result<T> of(Integer code, String message, T data, ResultType type) {
        return new Result<>(code, message, data, type);
    }

    /**
     * 自定义状态码响应
     */
    public static <T> Result<T> of(Integer code, String message, T data, ResultType type, String confirmToken) {
        return new Result<>(code, message, data, type, confirmToken);
    }

    /**
     * 判断请求是否成功
     */
    public Boolean isSuccess() {
        return Objects.equals(ResultType.SUCCESS, this.type) && Objects.equals(ResultCode.SUCCESS.getCode(), this.code);
    }
}
