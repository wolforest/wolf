package com.wolf.common.lang.exception.lang;

import com.wolf.common.lang.exception.SystemException;
import com.wolf.common.util.lang.StringUtil;

/**
 * com.wolf.common.lang.exception.lang
 *
 * @author Wingle
 * @since 2021/11/7 下午10:13
 **/
public class NullPointerException extends SystemException {
    private static final String DEFAULT_MESSAGE = "NullPointerException";

    public NullPointerException() {
        super(500, DEFAULT_MESSAGE);
    }

    public NullPointerException(String className) {
        super(500, StringUtil.join("NullPointerException: ", className));
    }
}
