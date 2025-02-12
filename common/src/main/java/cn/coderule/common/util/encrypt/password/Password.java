package cn.coderule.common.util.encrypt.password;

import cn.coderule.common.lang.exception.SystemException;
import cn.coderule.common.util.encrypt.HashUtil;
import lombok.Data;
import lombok.NonNull;

/**
 * com.wolf.common.util.encrypt
 *
 * @author Wingle
 * @since 2019/11/23 6:26 下午
 **/
@Data
public class Password {

    public static String createSalt() {
        return Salt.create();
    }

    public static boolean match(@NonNull String requestPassword, @NonNull String salt, @NonNull String password) {
        return password.equals(encrypt(requestPassword, salt));
    }

    public static String encrypt(String userPassword, String salt) {
        if (null == userPassword || salt == null) {
            throw new IllegalArgumentException("password and salt can't be null");
        }

        String encryptedPassword = userPassword + salt;
        try {
            encryptedPassword = HashUtil.sha512(encryptedPassword).substring(0, 32);
        } catch (Exception e) {
            throw new SystemException("password encrypt failed");
        }

        return encryptedPassword;
    }
}
