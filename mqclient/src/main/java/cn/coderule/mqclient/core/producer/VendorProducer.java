package cn.coderule.mqclient.core.producer;

import cn.coderule.mqclient.core.MQVendor;

/**
 * @author weixing
 * @since 2022/10/11 17:18
 */
public interface VendorProducer extends MQVendor {

    void start();

    void shutdown();

    ProduceResult send(ProduceRequest produceRequest);

    void send(ProduceRequest produceRequest, ProduceCallback callback);
}
