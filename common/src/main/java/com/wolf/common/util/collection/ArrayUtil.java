package com.wolf.common.util.collection;

import com.wolf.common.util.lang.BeanUtil;
import org.apache.commons.lang3.ArrayUtils;

/**
 * com.wolf.common.util.collection
 *
 * @author Wingle
 * @since 2020/2/18 7:31 上午
 **/
public class ArrayUtil {

    public static boolean isEmpty(boolean[] tArray) {
        return ArrayUtils.isEmpty(tArray);
    }

    public static boolean isEmpty(byte[] tArray) {
        return ArrayUtils.isEmpty(tArray);
    }

    public static boolean isEmpty(char[] tArray) {
        return ArrayUtils.isEmpty(tArray);
    }

    public static boolean isEmpty(double[] tArray) {
        return ArrayUtils.isEmpty(tArray);
    }

    public static boolean isEmpty(float[] tArray) {
        return ArrayUtils.isEmpty(tArray);
    }

    public static boolean isEmpty(int[] tArray) {
        return ArrayUtils.isEmpty(tArray);
    }

    public static boolean isEmpty(Object[] tArray) {
        return ArrayUtils.isEmpty(tArray);
    }

    public static boolean isEmpty(short[] tArray) {
        return ArrayUtils.isEmpty(tArray);
    }

    public static <T> boolean notEmpty(T[] tArray) {
        return ! isEmpty(tArray);
    }

    @SafeVarargs
    public static <T> boolean inArray(T t, T... array) {
        if (isEmpty(array)) {
            return false;
        }

        for (T tItem : array) {
            if (BeanUtil.equals(t, tItem)) {
                return true;
            }
        }

        return false;
    }

    @SafeVarargs
    public static <T> boolean notInArray(T t, T... array) {
        return !inArray(t, array);
    }
}
