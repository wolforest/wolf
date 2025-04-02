package com.wolf.mqclient.core.producer;

import com.wolf.mqclient.config.MQProducerConfig;
import com.wolf.mqclient.core.MQVendor;
import com.wolf.mqclient.vendor.kafka.produce.KafkaProducer;
import com.wolf.mqclient.vendor.mock.producer.MockProducer;
import com.wolf.mqclient.vendor.rocketmq.producer.RocketProducer;
import com.wolf.mqclient.vendor.rocketmq5.producer.Rocket5Producer;

/**
 * @author weixing
 * @since 2022/10/11 20:36
 */
public class VendorProducerFactory {
    public VendorProducer create(MQProducerConfig config) {
        switch (config.getVendorConfig().getVendorType()) {
            case MQVendor.VENDOR_MOCK:
                return new MockProducer(config);
            case MQVendor.VENDOR_KAFKA:
                return new KafkaProducer(config);
            case MQVendor.VENDOR_ROCKET_MQ_5:
                return new Rocket5Producer(config);
            case MQVendor.VENDOR_ROCKET_MQ:
            default:
                return new RocketProducer(config);
        }
    }
}
