package com.wolf.framework.layer.service.task.retry.strategy;

import com.wolf.common.lang.exception.lang.IllegalArgumentException;
import com.wolf.framework.layer.service.task.retry.Delay;
import com.wolf.framework.layer.service.task.retry.RetryStrategyConst;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Data;

/**
 * com.wolf.framework.layer.task.retry.strategy
 *
 * @author Wingle
 * @since 2021/12/13 上午2:25
 **/
@Data
public class DefaultRetryStrategy implements RetryStrategy {
    private String key = RetryStrategyConst.DEFAULT_RETRY_STRATEGY;
    private Long endlessDelay;
    private TimeUnit unit = TimeUnit.SECONDS;

    private List<Long> delayArray = Arrays.asList(
            15L,                // 15s
            15L,                // 30s
            30L,                // 1m
            3 * 60L,            // 4m
            10 * 60L,           // 14m
            20 * 60L,           // 34m
            30 * 60L,           // 1h4m
            30 * 60L,           // 1h34m
            30 * 60L,           // 2h4m
            60 * 60L,           // 3h4m
            3 * 60 * 60L,       // 6h4m
            3 * 60 * 60L,       // 9h4m
            3 * 60 * 60L,       // 12h4m
            6 * 60 * 60L,       // 18h4m
            6 * 60 * 60L,        // 24h4m

            24 * 60 * 60L,       // 2d4m
            24 * 60 * 60L,       // 3d4m
            24 * 60 * 60L,       // 4d4m
            24 * 60 * 60L,       // 5d4m
            24 * 60 * 60L,       // 6d4m
            24 * 60 * 60L        // 7d4m
    );

    @Override
    public Delay nextTry(int times) {
        return nextTry(times, LocalDateTime.now());
    }

    @Override
    public Delay nextTry(int times, LocalDateTime since) {
        if (times < 0) {
            throw new IllegalArgumentException("retry times can't less than 0");
        }

        if (times >= delayArray.size()) {
            if (null == endlessDelay) {
                return Delay.NO_DELAY;
            }
            return new Delay(endlessDelay, since);
        }

        return new Delay(delayArray.get(times), since);
    }
}
