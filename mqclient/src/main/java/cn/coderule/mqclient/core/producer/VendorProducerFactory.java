package cn.coderule.mqclient.core.producer;

import cn.coderule.mqclient.config.MQProducerConfig;
import cn.coderule.mqclient.core.MQVendor;
import cn.coderule.mqclient.vendor.kafka.produce.KafkaProducer;
import cn.coderule.mqclient.vendor.mock.producer.MockProducer;
import cn.coderule.mqclient.vendor.rocketmq.producer.RocketProducer;
import cn.coderule.mqclient.vendor.rocketmq5.producer.Rocket5Producer;

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
