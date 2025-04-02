package com.wolf.common.util.lang;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/7/13 9:15 上午
 **/
public class RandomUtil {
    public static String randomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }
}
