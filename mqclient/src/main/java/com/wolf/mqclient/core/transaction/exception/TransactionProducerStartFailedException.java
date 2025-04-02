package com.wolf.mqclient.core.transaction.exception;

import com.wolf.common.lang.exception.SystemException;
import lombok.Getter;

@Getter
public class TransactionProducerStartFailedException extends SystemException {
    public TransactionProducerStartFailedException(String message) {
        super(110, message);
    }

    public TransactionProducerStartFailedException(long code, String message) {
        super(code, message);
    }
}
