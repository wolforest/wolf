package com.wolf.common.lang.exception.api;

import com.wolf.common.lang.exception.BusinessException;

public class NotLoggedInException extends BusinessException {

    public NotLoggedInException() {
        super(110500, "Please login first");
    }

    public NotLoggedInException(String message) {
        super(110500, message);
    }

}
