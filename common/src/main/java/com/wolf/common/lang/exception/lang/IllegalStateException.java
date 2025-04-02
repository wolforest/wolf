package com.wolf.common.lang.exception.lang;

import com.wolf.common.lang.exception.BusinessException;

/**
 * com.wolf.common.lang.exception.lang
 *
 * @author Wingle
 * @since 2021/11/7 下午10:13
 **/
public class IllegalStateException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "IllegalStateException";

    public IllegalStateException() {
        super(500, DEFAULT_MESSAGE);
    }

    public IllegalStateException(String message) {
        super(500, message);
    }
}
