package com.wolf.common.util.lang;

import lombok.extern.slf4j.Slf4j;
import com.wolf.common.lang.exception.lang.InterruptedException;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2022/1/25 上午8:06
 **/
@Slf4j
public class ThreadUtil {
    public static void sleep(long millis) {
        ThreadUtil.sleep(millis, null);
    }

    public static void sleep(long millis, Integer nanos) {
        try {
            if (nanos != null) {
                Thread.sleep(millis, nanos);
            } else {
                Thread.sleep(millis);
            }
        } catch (Exception e) {
            log.error("thread.sleep interrupted", e);
            throw new InterruptedException();
        }
    }
}
