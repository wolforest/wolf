package com.wolf.common.util.encrypt.password;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * com.wolf.common.util.encrypt
 *
 * @author Wingle
 * @since 2019/11/23 6:20 下午
 **/
public class Salt {
    public static String create() {
        return create(9);
    }

    public static String create(int length) {
        byte[] salt = new byte[length];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(salt).substring(0, 12);
    }
}
