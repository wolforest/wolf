package com.wolf.common.lang.exception.api;

import lombok.Getter;
import com.wolf.common.lang.exception.SystemException;

@Getter
public class NullReturnedException extends SystemException {
    private static final String DEFAULT_MESSAGE = "Null returned exception";
    public NullReturnedException() {
        super(100501, DEFAULT_MESSAGE);
    }

    public NullReturnedException(String message) {
        super(100501, message);
    }

    public NullReturnedException(long code, String message) {
        super(code, message);
    }
}
