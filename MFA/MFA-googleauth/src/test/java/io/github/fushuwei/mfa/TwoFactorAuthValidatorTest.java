package io.github.fushuwei.mfa;

import org.junit.jupiter.api.Test;

class TwoFactorAuthValidatorTest {

    @Test
    void testValidateCode() {
        TwoFactorAuthValidator validator = new TwoFactorAuthValidator();
        System.out.println(validator.validateCode("admin", 303563));
    }
}
