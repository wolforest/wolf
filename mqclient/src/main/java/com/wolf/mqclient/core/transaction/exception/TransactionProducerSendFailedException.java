package com.wolf.mqclient.core.transaction.exception;

import com.wolf.common.lang.exception.SystemException;
import lombok.Getter;

@Getter
public class TransactionProducerSendFailedException extends SystemException {
    public TransactionProducerSendFailedException(String message) {
        super(110, message);
    }

    public TransactionProducerSendFailedException(long code, String message) {
        super(code, message);
    }
}
