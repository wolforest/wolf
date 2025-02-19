package cn.coderule.mqclient.core.transaction.exception;

import cn.coderule.common.lang.exception.SystemException;
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
