package com.wolf.mqclient.core.producer.exception;

import com.wolf.common.lang.exception.SystemException;
import lombok.Getter;

@Getter
public class ProducerBusyException extends SystemException {
    public ProducerBusyException(String message) {
        super(110, message);
    }

    public ProducerBusyException(long code, String message) {
        super(code, message);
    }
}
