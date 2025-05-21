package cn.coderule.common.util.test;

import cn.coderule.common.util.lang.string.StringUtil;
import org.springframework.lang.Nullable;
import cn.coderule.common.lang.exception.lang.IllegalArgumentException;

import java.util.Collection;

/**
 * Assert util
 *
 * @author YIK
 * @since 2022/7/8 3:26 PM
 */
public abstract class Assert {

    public static void checkArgument(boolean bool) {
        checkArgument(bool, null);
    }

    public static void checkArgument(boolean bool, String msg) {
        if (bool) {
            return;
        }

        if (StringUtil.notBlank(msg)) {
            throw new IllegalArgumentException(msg);
        }

        throw new IllegalArgumentException();
    }

    public static void noBlankElements(@Nullable Collection<String> collection, String message) {
        if (collection != null) {
            for (String element : collection) {
                if (element == null || element.trim().isEmpty()) {
                    throw new IllegalArgumentException(message);
                }
            }
        }

    }

    public static void notBlank(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> T notNull(T object) throws IllegalArgumentException {
        return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static <T> T notNull(T object, String message) throws IllegalArgumentException {
        if (null == object) {
            throw new IllegalArgumentException(message);
        }

        return object;
    }
}
