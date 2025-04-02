package com.wolf.common.sm.exception;

import com.wolf.common.lang.exception.SystemException;
import com.wolf.common.ds.string.Str;

/**
 * com.wolf.common.sm
 *
 * @author Wingle
 * @since 2019/12/16 11:29 下午
 **/
public class StateNotFoundException extends SystemException {
    public StateNotFoundException(String source, String event) {
        super(1100, Str.join(
                "Can't find State Map: {",
                "source: " , source,
                "; event:", event,
                "}"
        ));
    }
}
