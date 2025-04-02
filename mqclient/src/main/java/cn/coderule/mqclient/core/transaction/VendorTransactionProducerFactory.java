package cn.coderule.mqclient.core.transaction;

import cn.coderule.common.lang.exception.lang.IllegalArgumentException;
import cn.coderule.mqclient.config.MQTransactionProducerConfig;
import cn.coderule.mqclient.core.MQVendor;
import cn.coderule.mqclient.vendor.mock.transaction.MockTransactionProducer;
import cn.coderule.mqclient.vendor.rocketmq.transaction.RocketTransactionProducer;
import cn.coderule.mqclient.vendor.rocketmq5.transaction.Rocket5TransactionProducer;

/**
 * com.wolf.mqclient.producer.factory
 *
 * @author Wingle
 * @since 2021/12/18 下午12:10
 **/
public class VendorTransactionProducerFactory {
    public VendorTransactionProducer create(MQTransactionProducerConfig config) {
        switch (config.getVendorConfig().getVendorType()) {
            case MQVendor.VENDOR_MOCK:
                return new MockTransactionProducer(config);
            case MQVendor.VENDOR_ROCKET_MQ:
                return new RocketTransactionProducer(config);
            case MQVendor.VENDOR_ROCKET_MQ_5:
                return new Rocket5TransactionProducer(config);
            case MQVendor.VENDOR_KAFKA:
            default:
                throw new IllegalArgumentException("transaction producer only support for rocketmq engine");
        }
    }
}
