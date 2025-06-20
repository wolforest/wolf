package cn.coderule.common.lang.exception;

import lombok.Getter;

/**
 * BusinessException
 * code >= 10000
 */
@Getter
public class BusinessException extends BaseException {
    public BusinessException() {
        super(10000, "business error");
    }

    public BusinessException(String message) {
        super(10000, message, null);
    }

    public BusinessException(long code, String message) {
        super(code, message, null);
    }

    public BusinessException(String message, Throwable t) {
        super(10000, message, t);
    }

    public BusinessException(long code, String message, Throwable t) {
        super(code, message, t);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
