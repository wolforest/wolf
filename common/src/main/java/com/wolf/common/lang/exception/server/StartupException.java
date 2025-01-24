package com.wolf.common.lang.exception.server;

import com.wolf.common.lang.exception.SystemException;

public class StartupException extends SystemException {

    public StartupException(String msg) {
        super("Start failed: " + msg);
    }
}
