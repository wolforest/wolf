package com.wolf.mqclient.core.transaction.exception;

import com.wolf.common.lang.exception.SystemException;
import lombok.Getter;

@Getter
public class TransactionProducerBusyException extends SystemException {
    public TransactionProducerBusyException(String message) {
        super(110, message);
    }

    public TransactionProducerBusyException(long code, String message) {
        super(code, message);
    }
}
