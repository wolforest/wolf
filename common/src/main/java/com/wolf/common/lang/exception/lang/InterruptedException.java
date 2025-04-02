package com.wolf.common.lang.exception.lang;

import com.wolf.common.lang.exception.SystemException;

/**
 * com.wolf.common.lang.exception.lang
 *
 * @author Wingle
 * @since 2021/11/7 下午10:13
 **/
public class InterruptedException extends SystemException {
    private static final String DEFAULT_MESSAGE = "InterruptedException";

    public InterruptedException() {
        super(500, DEFAULT_MESSAGE);
    }

    public InterruptedException(String message) {
        super(500, message);
    }
}
