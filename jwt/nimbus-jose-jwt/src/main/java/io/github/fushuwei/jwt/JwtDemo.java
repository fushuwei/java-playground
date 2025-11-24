package io.github.fushuwei.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Date;

import java.util.concurrent.TimeUnit;

/**
 * Nimbus JOSE + JWT 演示案例，包含：JWT 的生成、验证和续期（刷新）逻辑
 *
 * @author fushuwei
 */
public class JwtDemo {

    // 密钥：用于签名和验证 JWT 的密钥，必须是 256 位 (32 字节)
    private static final byte[] SECRET_KEY = "2HUJ8CsMBZbpBBouLaJTeofgt518Sr6N6rB+VP2+k453WlMPlhRoWP1EMBRpAMMjELHj5BSoFucBb/na2uFuiw==".getBytes();

    // 签发者
    private static final String ISSUER = "io.github.fushuwei.jwt";

    // 有效期：1小时
    private static final long EXPIRATION_TIME_MS = TimeUnit.HOURS.toMillis(1);

    // 刷新窗口：在有效期内，如果剩余时间小于此值，则可以刷新，这里设置为 10 分钟
    private static final long REFRESH_WINDOW_MS = TimeUnit.MINUTES.toMillis(10);

    /**
     * 使用 Nimbus JOSE + JWT 生成 Signed JWT Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return 生成的 JWT 字符串
     * @throws JOSEException 签名错误
     */
    public static String generateNimbusToken(String userId, String username) throws JOSEException {
        // 1. 创建 Claims Set (载荷)
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                // 注册声明
                .issuer(ISSUER) // iss: 签发者
                .subject(userId) // sub: 主题
                .issueTime(now) // iat: 签发时间
                .expirationTime(expiration) // exp: 过期时间
                .jwtID(java.util.UUID.randomUUID().toString()) // jti: JWT ID，用于防止重放攻击

                // 私有声明
                .claim("username", username)
                .claim("roles", new String[]{"USER", "ADMIN"})
                .build();

        // 2. 创建 JWS Header (头部)
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256); // 使用 HS256 算法

        // 3. 创建 Signed JWT 对象
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        // 4. 创建 Signer (签名器)
        MACSigner signer = new MACSigner(SECRET_KEY);

        // 5. 签名
        signedJWT.sign(signer);

        // 6. 序列化成紧凑的 JWT 字符串
        return signedJWT.serialize();
    }

    /**
     * 使用 Nimbus JOSE + JWT 验证 JWT Token 并解析 Claims
     *
     * @param token JWT 字符串
     * @return 解析后的 Claims Set 对象
     * @throws ParseException Token 结构解析错误
     * @throws JOSEException  签名验证失败
     * @throws Exception      其他错误 (如过期)
     */
    public static JWTClaimsSet validateNimbusToken(String token) throws ParseException, JOSEException, Exception {
        // 1. 解析 Token 字符串为 SignedJWT 对象
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 2. 创建 Verifier (验证器)
        JWSVerifier verifier = new MACVerifier(SECRET_KEY);

        // 3. 验证签名
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Nimbus Token 签名验证失败");
        }

        // 4. 获取 Claims Set
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        // 5. 验证注册声明 (如过期时间 exp 和签发者 iss)
        Date now = new Date();
        if (claimsSet.getExpirationTime().before(now)) {
            throw new Exception("Nimbus Token 已过期");
        }
        if (!claimsSet.getIssuer().equals(ISSUER)) {
            throw new Exception("Nimbus Token 签发者验证失败");
        }

        // 验证成功，返回 Claims Set
        return claimsSet;
    }

    /**
     * 使用 Nimbus JOSE + JWT 检查 Token 是否需要刷新，并生成新 Token
     *
     * @param token 旧的 JWT 字符串
     * @return 新的 JWT 字符串，如果不需要刷新则返回 null
     */
    public static String refreshNimbusToken(String token) {
        try {
            // 1. 解析 Token 字符串
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 2. 验证签名 (必须验证，防止伪造)
            JWSVerifier verifier = new MACVerifier(SECRET_KEY);
            if (!signedJWT.verify(verifier)) {
                throw new JOSEException("Nimbus Token 签名验证失败");
            }

            // 3. 获取 Claims Set
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            // 4. 检查是否在刷新窗口内
            Date expiration = claimsSet.getExpirationTime();
            long remainingTime = expiration.getTime() - System.currentTimeMillis();

            // 如果剩余时间小于刷新窗口，则需要刷新
            if (remainingTime < REFRESH_WINDOW_MS) {
                System.out.println("Nimbus Token 剩余时间: " + TimeUnit.MILLISECONDS.toMinutes(remainingTime) + " 分钟，需要刷新。");

                // 5. 提取必要信息，生成新 Token
                String userId = claimsSet.getSubject();
                String username = claimsSet.getStringClaim("username");

                // 生成新的 Token
                return generateNimbusToken(userId, username);
            } else {
                System.out.println("Nimbus Token 剩余时间: " + TimeUnit.MILLISECONDS.toMinutes(remainingTime) + " 分钟，无需刷新。");
                return null; // 无需刷新
            }

        } catch (Exception e) {
            // 签名错误、结构错误或解析错误，不予刷新
            System.err.println("Nimbus Token 刷新失败，Token 无效: " + e.getMessage());
            return null;
        }
    }

    /**
     * 辅助函数：生成一个即将过期的 Nimbus Token (模拟剩余 5 分钟)
     */
    private static String simulateNearExpiredNimbusToken(String userId, String username) throws JOSEException {
        Date now = new Date();
        // 模拟过期时间：当前时间 + 5 分钟 (小于 REFRESH_WINDOW_MS 的 10 分钟)
        Date nearExpiration = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(5));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(ISSUER)
                .subject(userId)
                .issueTime(now)
                .expirationTime(nearExpiration) // 使用临近过期时间
                .jwtID(java.util.UUID.randomUUID().toString())
                .claim("username", username)
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        MACSigner signer = new MACSigner(SECRET_KEY);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public static void main(String[] args) throws Exception {
        // 演示用户数据
        String userId = "1001";
        String username = "admin";

        // 1. Nimbus 生成 Token
        String nimbusToken = generateNimbusToken(userId, username);
        System.out.println("1. 生成的 Nimbus Token:\n" + nimbusToken);

        // 2. Nimbus 验证 Token
        try {
            JWTClaimsSet claimsSet = validateNimbusToken(nimbusToken);
            System.out.println("\n2. Nimbus Token 验证成功，解析 Claims:");
            System.out.println("   Subject (用户ID): " + claimsSet.getSubject());
            System.out.println("   Username: " + claimsSet.getStringClaim("username"));
            System.out.println("   Expiration (过期时间): " + claimsSet.getExpirationTime());
        } catch (Exception e) {
            System.err.println("\n2. Nimbus Token 验证失败: " + e.getMessage());
        }

        // 3. Nimbus 刷新 Token (模拟在有效期内但临近过期)
        String nearExpiredNimbusToken = simulateNearExpiredNimbusToken(userId, username);
        System.out.println("\n3. 模拟临近过期的 Nimbus Token:\n" + nearExpiredNimbusToken);

        String refreshedNimbusToken = refreshNimbusToken(nearExpiredNimbusToken);
        if (refreshedNimbusToken != null) {
            System.out.println("   Nimbus Token 刷新成功，新 Token:\n" + refreshedNimbusToken);
        } else {
            System.out.println("   Nimbus Token 无需刷新或刷新失败。");
        }
    }
}
