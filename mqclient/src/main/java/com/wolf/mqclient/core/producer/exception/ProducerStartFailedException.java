package com.wolf.mqclient.core.producer.exception;

import com.wolf.common.lang.exception.SystemException;
import lombok.Getter;

@Getter
public class ProducerStartFailedException extends SystemException {
    public ProducerStartFailedException(String message) {
        super(110, message);
    }

    public ProducerStartFailedException(long code, String message) {
        super(code, message);
    }
}
