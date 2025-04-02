package com.wolf.common.lang.exception.io;

import lombok.Getter;
import com.wolf.common.lang.exception.SystemException;

@Getter
public class WolfIOException extends SystemException {
    private static final String DEFAULT_MESSAGE = "IOException";
    public WolfIOException() {
        super(100601, DEFAULT_MESSAGE);
    }

    public WolfIOException(String message) {
        super(100601, message);
    }

    public WolfIOException(long code, String message) {
        super(code, message);
    }
}
