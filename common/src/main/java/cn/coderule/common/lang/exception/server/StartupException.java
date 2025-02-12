package cn.coderule.common.lang.exception.server;

import cn.coderule.common.lang.exception.SystemException;

public class StartupException extends SystemException {

    public StartupException(String msg) {
        super("Start failed: " + msg);
    }
}
