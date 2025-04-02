package com.wolf.common.lang.exception.api;

import lombok.Getter;
import com.wolf.common.lang.exception.SystemException;

@Getter
public class FakeBusinessException extends SystemException {
    public FakeBusinessException(String message) {
        super(message);
    }

    public FakeBusinessException(long code, String message) {
        super(code, message);
    }
}
