package com.wolf.httpclient.exception;

import com.wolf.common.lang.exception.SystemException;

public class HttpRequestFailException extends SystemException {
    public HttpRequestFailException(String msg) {
        super(500, "HttpClient request fail: " + msg);
    }
}
