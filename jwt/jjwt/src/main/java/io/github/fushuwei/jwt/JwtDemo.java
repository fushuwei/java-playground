package io.github.fushuwei.jwt;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.util.Date;

import java.util.concurrent.TimeUnit;

/**
 * jjwt 演示案例，包含：JWT 的生成、验证和续期（刷新）逻辑
 *
 * @author fushuwei
 */
public class JwtDemo {

    // 密钥：用于签名和验证 JWT 的密钥，必须保密且足够长，推荐使用 Jwts.SIG.HS256
    private static final SecretKey JJWT_SECRET_KEY = Jwts.SIG.HS256.key().build();

    // 签发者
    private static final String ISSUER = "io.github.fushuwei.jwt";

    // 有效期：1小时
    private static final long EXPIRATION_TIME_MS = TimeUnit.HOURS.toMillis(1);

    // 刷新窗口：在有效期内，如果剩余时间小于此值，则可以刷新，这里设置为 10 分钟
    private static final long REFRESH_WINDOW_MS = TimeUnit.MINUTES.toMillis(10);

    /**
     * 使用 JJWT 生成 JWT Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return 生成的 JWT 字符串
     */
    public static String generateJjwtToken(String userId, String username) {
        // 当前时间
        Date now = new Date();
        // 过期时间
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);

        // 创建 JWT Builder
        JwtBuilder builder = Jwts.builder()
                // 1. Header (头部): 默认包含 "typ":"JWT", "alg":"HS256"
                // 2. Payload (载荷): 包含 Claims (声明)

                // 注册声明 (Registered Claims)
                .issuer(ISSUER) // iss: 签发者
                .subject(userId) // sub: 主题 (通常是用户ID)
                .issuedAt(now) // iat: 签发时间
                .expiration(expiration) // exp: 过期时间

                // 公共声明 (Public Claims) 或 私有声明 (Private Claims)
                .claim("username", username) // 自定义声明：用户名
                .claim("roles", new String[]{"USER", "ADMIN"}) // 自定义声明：角色列表

                // 3. Signature (签名): 使用密钥和算法进行签名
                .signWith(JJWT_SECRET_KEY, Jwts.SIG.HS256); // 使用 HS256 算法和密钥签名

        // 压缩并序列化成紧凑的 JWT 字符串
        return builder.compact();
    }

    /**
     * 使用 JJWT 验证 JWT Token 并解析 Claims
     *
     * @param token JWT 字符串
     * @return 解析后的 Claims 对象
     */
    public static Claims validateJjwtToken(String token) {
        // 创建 JWT 解析器
        JwtParser parser = Jwts.parser()
                .verifyWith(JJWT_SECRET_KEY)  // 设置用于验证签名的密钥
                .requireIssuer(ISSUER)  // 要求签发者必须是指定的值
                .build();

        // 解析并验证 JWT
        Jws<Claims> jws = parser.parseSignedClaims(token);

        // 验证成功，返回 Claims (载荷)
        return jws.getPayload();
    }

    /**
     * 使用 JJWT 检查 Token 是否需要刷新，并生成新 Token
     *
     * @param token 旧的 JWT 字符串
     * @return 新的 JWT 字符串，如果不需要刷新则返回 null
     */
    public static String refreshJjwtToken(String token) {
        // 1. 验证 Token 是否有效
        JwtParser parser = Jwts.parser()
                .verifyWith(JJWT_SECRET_KEY)  // 设置用于验证签名的密钥
                .requireIssuer(ISSUER)  // 要求签发者必须是指定的值
                .build();

        // 仅解析，不验证过期时间
        Claims claims = parser.parseSignedClaims(token).getPayload();

        // 2. 检查是否在刷新窗口内
        long remainingTime = claims.getExpiration().getTime() - System.currentTimeMillis();

        // 如果剩余时间小于刷新窗口，则需要刷新
        if (remainingTime < REFRESH_WINDOW_MS) {
            System.out.println("JJWT Token 剩余时间: " + TimeUnit.MILLISECONDS.toMinutes(remainingTime) + " 分钟，需要刷新。");

            // 3. 提取必要信息，生成新 Token
            String userId = claims.getSubject();
            String username = claims.get("username", String.class);

            // 生成新的 Token
            return generateJjwtToken(userId, username);
        } else {
            System.out.println("JJWT Token 剩余时间: " + TimeUnit.MILLISECONDS.toMinutes(remainingTime) + " 分钟，无需刷新。");
            return null;
        }
    }

    /**
     * 辅助函数：生成一个即将过期的 JJWT Token (模拟剩余 5 分钟)
     */
    private static String simulateNearExpiredJjwtToken(String userId, String username) {
        Date now = new Date();
        // 模拟过期时间：当前时间 + 5 分钟 (小于 REFRESH_WINDOW_MS 的 10 分钟)
        Date expiration = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(5));

        // 创建 JWT Builder
        JwtBuilder builder = Jwts.builder()
                // 1. Header (头部): 默认包含 "typ":"JWT", "alg":"HS256"
                // 2. Payload (载荷): 包含 Claims (声明)

                // 注册声明 (Registered Claims)
                .issuer(ISSUER) // iss: 签发者
                .subject(userId) // sub: 主题 (通常是用户ID)
                .issuedAt(now) // iat: 签发时间
                .expiration(expiration) // exp: 过期时间

                // 公共声明 (Public Claims) 或 私有声明 (Private Claims)
                .claim("username", username) // 自定义声明：用户名
                .claim("roles", new String[]{"USER", "ADMIN"}) // 自定义声明：角色列表

                // 3. Signature (签名): 使用密钥和算法进行签名
                .signWith(JJWT_SECRET_KEY, Jwts.SIG.HS256); // 使用 HS256 算法和密钥签名

        // 压缩并序列化成紧凑的 JWT 字符串
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
        // 演示用户数据
        String userId = "1001";
        String username = "admin";

        // 1. JJWT 生成 Token
        String jjwtToken = generateJjwtToken(userId, username);
        System.out.println("1. 生成的 JJWT Token:\n" + jjwtToken);

        // 2. JJWT 验证 Token
        try {
            Claims claims = validateJjwtToken(jjwtToken);
            System.out.println("\n2. JJWT Token 验证成功，解析 Claims:");
            System.out.println("   Subject (用户ID): " + claims.getSubject());
            System.out.println("   Username: " + claims.get("username"));
            System.out.println("   Expiration (过期时间): " + claims.getExpiration());
        } catch (Exception e) {
            System.err.println("\n2. JJWT Token 验证失败: " + e.getMessage());
        }

        // 3. JJWT 刷新 Token (模拟在有效期内但临近过期的 Token)
        String nearExpiredJjwtToken = simulateNearExpiredJjwtToken(userId, username);
        System.out.println("\n3. 模拟临近过期的 JJWT Token:\n" + nearExpiredJjwtToken);

        String refreshedJjwtToken = refreshJjwtToken(nearExpiredJjwtToken);
        if (refreshedJjwtToken != null) {
            System.out.println("   JJWT Token 刷新成功，新 Token:\n" + refreshedJjwtToken);
        } else {
            System.out.println("   JJWT Token 无需刷新或刷新失败。");
        }
    }
}
