package cn.coderule.common.lang.exception.lang;

import cn.coderule.common.lang.exception.BusinessException;

/**
 * com.wolf.common.lang.exception.lang
 *
 * @author Wingle
 * @since 2021/11/7 下午10:13
 **/
public class IllegalArgumentException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "IllegalArgumentException";

    public IllegalArgumentException() {
        super(500, DEFAULT_MESSAGE);
    }

    public IllegalArgumentException(String message) {
        super(500, message);
    }
}
