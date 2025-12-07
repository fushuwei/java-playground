package io.github.fushuwei.security;

/**
 * 系统常量
 *
 * @author fushuwei
 */
public class GlobalConstants {

    /**
     * 系统名称
     */
    public static final String SYSTEM_NAME = "sca-skeleton";

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * 默认租户ID
     */
    public static final String DEFAULT_TENANT_ID = "default";

    /**
     * 管理员角色编码
     */
    public static final String ADMIN_ROLE_CODE = "ADMIN";

    /**
     * 普通用户角色编码
     */
    public static final String USER_ROLE_CODE = "USER";

    /**
     * 超级管理员角色编码
     */
    public static final String SUPER_ADMIN_ROLE_CODE = "SUPER_ADMIN";

    /**
     * 系统用户ID
     */
    public static final String SYSTEM_USER_ID = "system";

    /**
     * 匿名用户ID
     */
    public static final String ANONYMOUS_USER_ID = "anonymous";

    /**
     * JWT Token前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * JWT Token头名称
     */
    public static final String JWT_TOKEN_HEADER = "Authorization";

    /**
     * Redis键前缀：用户会话
     */
    public static final String REDIS_USER_SESSION_PREFIX = "user:session:";

    /**
     * Redis键前缀：用户权限
     */
    public static final String REDIS_USER_PERMISSION_PREFIX = "user:permission:";

    /**
     * Redis键前缀：验证码
     */
    public static final String REDIS_CAPTCHA_PREFIX = "captcha:";

    /**
     * Redis键前缀：限流
     */
    public static final String REDIS_RATE_LIMIT_PREFIX = "rate:limit:";

    /**
     * 默认密码加密盐值
     */
    public static final String DEFAULT_PASSWORD_SALT = "sca_skeleton_salt";

    /**
     * TOTP密钥长度
     */
    public static final int TOTP_SECRET_LENGTH = 32;

    /**
     * TOTP验证码有效期（秒）
     */
    public static final int TOTP_VALIDITY_PERIOD = 30;

    /**
     * 验证码长度
     */
    public static final int CAPTCHA_LENGTH = 6;

    /**
     * 密码最小长度
     */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * 用户名最小长度
     */
    public static final int USERNAME_MIN_LENGTH = 3;

    /**
     * 用户名最大长度
     */
    public static final int USERNAME_MAX_LENGTH = 50;

    /**
     * 邮箱最大长度
     */
    public static final int EMAIL_MAX_LENGTH = 100;

    /**
     * 手机号最大长度
     */
    public static final int PHONE_MAX_LENGTH = 20;

    /**
     * 描述最大长度
     */
    public static final int DESCRIPTION_MAX_LENGTH = 500;

    /**
     * 文件名最大长度
     */
    public static final int FILENAME_MAX_LENGTH = 255;

    /**
     * IP地址最大长度
     */
    public static final int IP_MAX_LENGTH = 45;

    /**
     * User-Agent最大长度
     */
    public static final int USER_AGENT_MAX_LENGTH = 500;

    private GlobalConstants() {
        // 工具类私有构造方法
    }
}
