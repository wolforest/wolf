package cn.coderule.common.lang.exception.api;

import cn.coderule.common.lang.exception.BusinessException;

public class NotLoggedInException extends BusinessException {

    public NotLoggedInException() {
        super(110500, "Please login first");
    }

    public NotLoggedInException(String message) {
        super(110500, message);
    }

}
