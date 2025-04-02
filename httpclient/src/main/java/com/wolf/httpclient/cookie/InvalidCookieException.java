package com.wolf.httpclient.cookie;

import com.wolf.common.lang.exception.SystemException;

public class InvalidCookieException extends SystemException {
    public InvalidCookieException() {
        super(500, "invalid cookie");
    }
}
