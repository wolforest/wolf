package com.wolf.mqclient.config.exception;

import com.wolf.common.lang.exception.SystemException;

/**
 * @author weixing
 * @since 2022/10/31 18:29
 */
public class MQConfigException extends SystemException {
    public MQConfigException(String message) {
        super(110, message);
    }

    public MQConfigException(long code, String message) {
        super(code, message);
    }
}
