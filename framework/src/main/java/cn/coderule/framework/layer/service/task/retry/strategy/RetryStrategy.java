package cn.coderule.framework.layer.service.task.retry.strategy;

import cn.coderule.framework.layer.service.task.retry.Delay;
import java.time.LocalDateTime;

/**
 * com.wolf.framework.layer.task.retry.strategy
 *
 * @author Wingle
 * @since 2021/12/13 上午2:14
 **/
public interface RetryStrategy {
    String getKey();

    Delay nextTry(int times, LocalDateTime since);

    Delay nextTry(int times);
}
