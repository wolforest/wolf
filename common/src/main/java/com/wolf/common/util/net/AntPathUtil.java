package com.wolf.common.util.net;

import org.springframework.util.AntPathMatcher;

/**
 * com.wolf.framework.util
 *
 * @author Wingle
 * @since 2020/2/26 12:14 下午
 **/
public class AntPathUtil {
    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    public static boolean match(String pattern, String path) {
        return MATCHER.match(pattern, path);
    }
}
