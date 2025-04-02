package com.wolf.common.util.lang;

/**
 * BooleanUtil
 *
 * @author rocky
 * @since 2022/9/15 15:21
 **/
public class BooleanUtil {

    public static boolean isTrue(Boolean data) {
        return data != null && data;
    }

    public static boolean notTrue(Boolean data) {
        return data == null || !data;
    }
}
