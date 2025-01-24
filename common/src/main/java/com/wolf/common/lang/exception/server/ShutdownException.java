package com.wolf.common.lang.exception.server;

import com.wolf.common.lang.exception.SystemException;

public class ShutdownException extends SystemException {

    public ShutdownException(String msg) {
        super("Shutdown failed: " + msg);
    }
}
