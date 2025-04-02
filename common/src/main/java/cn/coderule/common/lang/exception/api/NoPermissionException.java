package cn.coderule.common.lang.exception.api;

import cn.coderule.common.lang.exception.BusinessException;

/**
 * NoPermissionException
 *
 * @author YIK
 * @since 2022/3/17 5:57 PM
 */
public class NoPermissionException extends BusinessException {

    public NoPermissionException() {
        super(110403, "No permission");
    }

    public NoPermissionException(String message) {
        super(110403, message);
    }

}
