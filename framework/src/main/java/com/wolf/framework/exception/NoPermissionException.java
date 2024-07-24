package com.wolf.framework.exception;

import com.wolf.common.lang.exception.BusinessException;

/**
 * NoPermissionException
 *
 * @author: YIK
 * @since: 2022/3/17 5:57 PM
 */
public class NoPermissionException extends BusinessException {

    public NoPermissionException(String message) {
        super(110403, message);
    }

}