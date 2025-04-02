package cn.coderule.mqclient.core.producer.exception;

import cn.coderule.common.lang.exception.SystemException;
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
