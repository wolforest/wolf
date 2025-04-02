package cn.coderule.httpclient.cookie;

import cn.coderule.common.lang.exception.SystemException;

public class InvalidCookieException extends SystemException {
    public InvalidCookieException() {
        super(500, "invalid cookie");
    }
}
