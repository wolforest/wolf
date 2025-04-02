package com.wolf.mqclient.core.producer.exception;

import com.wolf.common.lang.exception.SystemException;
import lombok.Getter;

@Getter
public class ProducerSendFailedException extends SystemException {
    public ProducerSendFailedException(String message) {
        super(110, message);
    }

    public ProducerSendFailedException(long code, String message) {
        super(code, message);
    }
}
