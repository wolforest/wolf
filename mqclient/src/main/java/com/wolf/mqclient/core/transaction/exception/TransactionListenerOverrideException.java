package com.wolf.mqclient.core.transaction.exception;

import com.wolf.common.lang.exception.SystemException;

/**
 * @author weixing
 * @since 2022/12/12 15:52
 */
public class TransactionListenerOverrideException extends SystemException {
    public TransactionListenerOverrideException(String message) {
        super(110, message);
    }

    public TransactionListenerOverrideException(long code, String message) {
        super(code, message);
    }
}
