package cn.coderule.common.lang.exception.lang;

import cn.coderule.common.lang.exception.SystemException;

/**
 * com.wolf.common.lang.exception.enums
 *
 * @author Wingle
 * @since 2019/9/29 11:26 PM
 **/
public class UnsupportedEnumCodeException extends SystemException {
    public UnsupportedEnumCodeException(String message) {
        super(message);
    }

    public UnsupportedEnumCodeException(int code, String message) {
        super(code, message);
    }
}
