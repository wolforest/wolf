package com.wolf.common.lang.exception;

public class ExceptionUtil {

    public static String getName(Throwable e) {
        String className = e.getClass().getSimpleName();
        return className.replace("Exception", "");
    }
}
