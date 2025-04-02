package cn.coderule.framework.layer.service.task.retry;

import cn.coderule.common.convention.ability.Retryable;
import cn.coderule.framework.layer.service.task.retry.strategy.RetryStrategy;
import cn.coderule.framework.layer.service.task.retry.strategy.StrategyBuilder;
import java.time.LocalDateTime;
import lombok.NonNull;

/**
 * com.wolf.framework.layer.task.retry
 *
 * @author Wingle
 * @since 2021/12/13 上午2:03
 **/
public class Retry {
    private static final RetryStrategyMap strategyMap = new RetryStrategyMap();

    public static void addStrategy(@NonNull RetryStrategy strategy) {
        strategyMap.add(strategy);
    }

    public static StrategyBuilder addStrategy() {
        return new StrategyBuilder(strategyMap);
    }

    public static RetryStrategy getStrategy(String key) {
        return strategyMap.getInstance(key);
    }

    public static Delay nextTry(String strategy, int times) {
        return nextTry(strategy,  times, null);
    }

    public static Delay nextTry(String key, int times, LocalDateTime since) {
        RetryStrategy strategy = strategyMap.getInstance(key);
        if (strategy == null) {
            return Delay.NO_DELAY;
        }

        return strategy.nextTry(times, since);
    }

    public static Delay nextTry(Retryable retryable) {
        RetryStrategy strategy = strategyMap.getInstance(retryable.getStrategy());
        if (strategy == null) {
            return Delay.NO_DELAY;
        }
        Delay delay = strategy.nextTry(retryable.getRetryTimes());
        retryable.retry();
        return delay;
    }
}
