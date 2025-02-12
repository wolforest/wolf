package cn.coderule.mqclient.core.producer.exception;

import cn.coderule.common.lang.exception.SystemException;
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
