package cn.coderule.mqclient.core.producer.exception;

import cn.coderule.common.lang.exception.SystemException;
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
