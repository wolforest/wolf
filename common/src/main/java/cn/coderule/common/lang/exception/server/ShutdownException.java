package cn.coderule.common.lang.exception.server;

import cn.coderule.common.lang.exception.SystemException;

public class ShutdownException extends SystemException {

    public ShutdownException(String msg) {
        super("Shutdown failed: " + msg);
    }
}
