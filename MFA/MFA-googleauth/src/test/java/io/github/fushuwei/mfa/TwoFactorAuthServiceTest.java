package io.github.fushuwei.mfa;

import org.junit.jupiter.api.Test;

class TwoFactorAuthServiceTest {

    @Test
    void testGenerateQrCode() {
        TwoFactorAuthService service = new TwoFactorAuthService();
        System.out.println(service.generateQrUrl("admin"));
    }
}
