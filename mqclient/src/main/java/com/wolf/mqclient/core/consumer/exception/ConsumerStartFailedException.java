package com.wolf.mqclient.core.consumer.exception;

import com.wolf.common.lang.exception.SystemException;
import lombok.Getter;

@Getter
public class ConsumerStartFailedException extends SystemException {
    public ConsumerStartFailedException(String message) {
        super(110, message);
    }

    public ConsumerStartFailedException(long code, String message) {
        super(code, message);
    }
}
