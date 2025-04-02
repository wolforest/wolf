package com.wolf.mqclient.config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weixing
 * @since 2022/11/3 16:06
 */
@Data
@NoArgsConstructor
public class MQConsumerGroupConfig {
    protected boolean enabled;
    /**
     * support subscribe multi topics and tags
     */
    protected boolean proxyMode;
    protected String group;

    protected int consumerNum = 1;
    protected int minThreadNum = MQDefaultConst.DEFAULT_CONSUMER_THREAD_MIN;
    protected int maxThreadNum = MQDefaultConst.DEFAULT_CONSUMER_THREAD_MIN;

    protected boolean consumeOrderly = false;
}
