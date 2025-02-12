package cn.coderule.httpclient.exception;

import cn.coderule.common.lang.exception.SystemException;

public class HttpRequestFailException extends SystemException {
    public HttpRequestFailException(String msg) {
        super(500, "HttpClient request fail: " + msg);
    }
}
