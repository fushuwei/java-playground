package io.github.fushuwei.springsecurity.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.slf4j.MDC;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 响应结果
 *
 * @author fushuwei
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String HEADER_REQUEST_ID = "X-Request-ID";
    private static final String MDC_KEY_REQUEST_ID = "requestId";

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
    private T data;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 请求ID，用于链路追踪
     */
    private String requestId;

    /**
     * 响应类型：SUCCESS, WARNING, CONFIRM, FAILURE
     */
    private ResultType type;

    /**
     * 二次确认时的令牌，前端需要在确认时回传，防止重放攻击和确保操作的一致性
     */
    private String confirmToken;

    public Result() {
        this.timestamp = LocalDateTime.now();
        this.requestId = MDC.get(MDC_KEY_REQUEST_ID);
    }

    public Result(Integer code, String message, T data, ResultType type) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
        this.type = type;
    }

    public Result(Integer code, String message, T data, ResultType type, String confirmToken) {
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
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null, ResultType.SUCCESS);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, ResultType.SUCCESS);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data, ResultType.SUCCESS);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> failure() {
        return new Result<>(ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMessage(), null, ResultType.FAILURE);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> Result<T> failure(String message) {
        return new Result<>(ResultCode.FAILURE.getCode(), message, null, ResultType.FAILURE);
    }

    /**
     * 警告响应
     */
    public static <T> Result<T> warning(String message) {
        return new Result<>(ResultCode.WARNING.getCode(), message, null, ResultType.WARNING);
    }

    /**
     * 警告响应（带数据）
     */
    public static <T> Result<T> warning(String message, T data) {
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
    public static <T> Result<T> confirm(String message, String confirmToken, T data) {
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
}
