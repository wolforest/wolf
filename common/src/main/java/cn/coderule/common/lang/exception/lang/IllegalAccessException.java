package cn.coderule.common.lang.exception.lang;

import cn.coderule.common.lang.exception.SystemException;

/**
 * com.wolf.common.lang.exception.lang
 *
 * @author Wingle
 * @since 2021/11/7 下午10:13
 **/
public class IllegalAccessException extends SystemException {
    private static final String DEFAULT_MESSAGE = "IllegalArgumentException";

    public IllegalAccessException() {
        super(500, DEFAULT_MESSAGE);
    }

    public IllegalAccessException(String message) {
        super(500, message);
    }
}
