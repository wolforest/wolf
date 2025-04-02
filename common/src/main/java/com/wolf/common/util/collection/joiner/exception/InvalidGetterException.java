package com.wolf.common.util.collection.joiner.exception;

import lombok.Getter;
import com.wolf.common.lang.exception.SystemException;
import com.wolf.common.util.lang.StringUtil;

@Getter
public class InvalidGetterException extends SystemException {
    public InvalidGetterException(String message) {
        super(message);
    }

    public InvalidGetterException(int len) {
        super(StringUtil.join("Invalid getter for CollectionJoiner: too many or too less args len = ", len));
    }

    public InvalidGetterException() {
        super("Invalid getter for CollectionJoiner");
    }
}
