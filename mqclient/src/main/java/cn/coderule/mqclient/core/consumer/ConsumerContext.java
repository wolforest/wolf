package cn.coderule.mqclient.core.consumer;

import cn.coderule.common.convention.container.Context;
import cn.coderule.mqclient.config.MQConsumerConfig;
import cn.coderule.mqclient.config.MQVendorConfig;
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
