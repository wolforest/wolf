package cn.coderule.mqclient.core.consumer.exception;

import cn.coderule.common.lang.exception.SystemException;
import lombok.Getter;

@Getter
public class ConsumerStartFailedException extends SystemException {
    public ConsumerStartFailedException(String message) {
        super(110, message);
    }

    public ConsumerStartFailedException(long code, String message) {
        super(code, message);
    }
}
