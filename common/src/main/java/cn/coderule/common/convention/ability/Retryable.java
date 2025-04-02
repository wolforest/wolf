package cn.coderule.common.convention.ability;

import java.time.LocalDateTime;

/**
 * com.wolf.common.lang.ablity
 *
 * @author Wingle
 * @since 2020/11/6 10:53 上午
 **/
public interface Retryable {
    default String getStrategy() {
        return null;
    }

    default int getRetryTimes() {
        return -1;
    }

    default LocalDateTime getLastRetryTime() {
        return null;
    }

    void retry();

    void changeStrategy(String strategy);
}
