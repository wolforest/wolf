package com.wolf.mqclient.core.consumer;

import com.wolf.common.convention.container.Context;
import com.wolf.mqclient.config.MQConsumerConfig;
import com.wolf.mqclient.config.MQVendorConfig;
import lombok.Data;

/**
 * @author weixing
 * @since 2022/10/11 17:22
 */
@Data
public class ConsumerContext implements Context {
    private MQVendorConfig clientConfig;
    private MQConsumerConfig consumerConfig;
}
