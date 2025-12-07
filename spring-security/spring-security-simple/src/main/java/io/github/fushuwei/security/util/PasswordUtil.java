package io.github.fushuwei.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具类
 * 提供密码加密、验证等安全相关功能
 *
 * @author fushuwei
 */
@Slf4j
public class PasswordUtil {

    /**
     * 密码编码器
     */
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 私有构造方法
     */
    private PasswordUtil() {
        throw new UnsupportedOperationException("工具类不能被实例化");
    }

    /**
     * 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("原始密码不能为空");
        }
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 验证密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length < 8) {
            length = 8; // 最小长度8位
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    /**
     * 生成简单随机密码（只包含字母和数字）
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateSimplePassword(int length) {
        if (length < 8) {
            length = 8;
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 密码强度等级（1-弱，2-中，3-强）
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            return 1; // 弱
        }

        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        int score = 0;
        if (hasLower) score++;
        if (hasUpper) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;

        if (score >= 3 && password.length() >= 10) {
            return 3; // 强
        } else if (score >= 2) {
            return 2; // 中
        } else {
            return 1; // 弱
        }
    }

    /**
     * 获取密码强度描述
     *
     * @param strength 强度等级
     * @return 强度描述
     */
    public static String getPasswordStrengthDesc(int strength) {
        return switch (strength) {
            case 1 -> "弱";
            case 2 -> "中";
            case 3 -> "强";
            default -> "未知";
        };
    }
}
