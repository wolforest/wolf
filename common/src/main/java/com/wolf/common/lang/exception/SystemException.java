package com.wolf.common.lang.exception;

import lombok.Getter;

/**
 * SystemException
 *  code < 10000
 */
@Getter
public class SystemException extends BaseException {
    public SystemException() {
        super(500, "system exception");
    }

    public SystemException(String message) {
        super(500, message);
    }

    public SystemException(long code, String message) {
        super(code, message);
    }
}
