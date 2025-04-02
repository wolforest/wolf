package cn.coderule.mqclient.core.consumer;

import cn.coderule.mqclient.config.MQConsumerConfig;
import cn.coderule.mqclient.core.MQVendor;
import cn.coderule.mqclient.vendor.kafka.consumer.KafkaConsumer;
import cn.coderule.mqclient.vendor.mock.consumer.MockConsumer;
import cn.coderule.mqclient.vendor.rocketmq.consumer.RocketConsumer;
import cn.coderule.mqclient.vendor.rocketmq5.consumer.Rocket5Consumer;

/**
 * @author weixing
 * @since 2022/10/12 17:22
 */
public class VendorConsumerFactory {
    public VendorConsumer create(MQConsumerConfig config) {
        String clientType = config.getVendorConfig().getVendorType();

        switch (clientType) {
            case MQVendor.VENDOR_MOCK:
                return new MockConsumer(config);
            case MQVendor.VENDOR_KAFKA:
                return new KafkaConsumer(config);
            case MQVendor.VENDOR_ROCKET_MQ_5:
                return new Rocket5Consumer(config);
            case MQVendor.VENDOR_ROCKET_MQ:
            default:
                return new RocketConsumer(config);
        }
    }
}
