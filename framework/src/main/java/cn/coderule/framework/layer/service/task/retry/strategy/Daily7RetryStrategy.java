package cn.coderule.framework.layer.service.task.retry.strategy;

import cn.coderule.framework.layer.service.task.retry.RetryStrategyConst;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

@Data
public class Daily7RetryStrategy extends DefaultRetryStrategy {
    private String key = RetryStrategyConst.DAILY_7_RETRY_STRATEGY;
    private List<Long> delayArray = Arrays.asList(
            24 * 60 * 60L,       // 1d
            24 * 60 * 60L,       // 2d
            24 * 60 * 60L,       // 3d
            24 * 60 * 60L,       // 4d
            24 * 60 * 60L,       // 5d
            24 * 60 * 60L,       // 6d
            24 * 60 * 60L        // 7d
    );

}
