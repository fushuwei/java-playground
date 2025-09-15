package io.github.fushuwei.mfa;

import com.warrenstrange.googleauth.GoogleAuthenticator;

public class TwoFactorAuthValidator {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public boolean validateCode(String username, int code) {
        // 从数据库取出用户绑定的 secret
        String secret = getSecretFromDB(username);

        return gAuth.authorize(secret, code);
    }

    private String getSecretFromDB(String username) {
        // TODO: 从数据库获取用户 secret
        return "YPQCJZGPM7LW2TL7U6MFKDJ7NZ2HAF3E";
    }
}

