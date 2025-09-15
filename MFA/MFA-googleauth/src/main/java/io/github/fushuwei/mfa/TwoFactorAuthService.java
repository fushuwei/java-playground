package io.github.fushuwei.mfa;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

public class TwoFactorAuthService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateQrUrl(String username) {
        // 生成秘钥
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        // 存储 key.getKey() 到数据库，和用户绑定
        String secret = key.getKey();
        saveSecretToDB(username, secret);

        // 生成二维码 URL (otpauth 协议)
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("fushuwei.github.io", username, key);
    }

    private void saveSecretToDB(String username, String secret) {
        // TODO: 持久化到数据库
        System.out.println("Save username to DB: " + username);
        System.out.println("Save secret to DB: " + secret);
    }
}

