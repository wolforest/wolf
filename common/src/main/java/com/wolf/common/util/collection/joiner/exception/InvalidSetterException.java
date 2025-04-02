package com.wolf.common.util.collection.joiner.exception;

import lombok.Getter;
import com.wolf.common.lang.exception.SystemException;

@Getter
public class InvalidSetterException extends SystemException {
    public InvalidSetterException(String message) {
        super(message);
    }

    public InvalidSetterException() {
        super("Invalid setter for CollectionJoiner");
    }
}
