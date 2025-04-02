package cn.coderule.common.util.lang.collection.joiner.exception;

import lombok.Getter;
import cn.coderule.common.lang.exception.SystemException;

@Getter
public class InvalidSetterException extends SystemException {
    public InvalidSetterException(String message) {
        super(message);
    }

    public InvalidSetterException() {
        super("Invalid setter for CollectionJoiner");
    }
}
