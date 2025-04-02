package com.wolf.mqclient.vendor.rocketmq;

import com.wolf.common.util.lang.StringUtil;
import com.wolf.mqclient.config.MQConsumerConfig;
import com.wolf.mqclient.config.MQProducerConfig;
import com.wolf.mqclient.config.MQTransactionProducerConfig;
import com.wolf.mqclient.vendor.rocketmq.acl.AclHookFactory;
import com.wolf.mqclient.vendor.rocketmq.consumer.RocketConsumerListener;
import com.wolf.mqclient.vendor.rocketmq.consumer.RocketConsumerListenerOrderly;
import com.wolf.mqclient.vendor.rocketmq.transaction.RocketTransactionListener;
import com.wolf.mqclient.vendor.rocketmq.transaction.TransactionMQProducerCustomized;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * com.wolf.mqclient.adapter.rocketmq
 *
 * @author Wingle
 * @since 2021/12/16 下午6:14
 **/
@Slf4j
public class RocketFactory {

    private static final AtomicInteger CONSUMER_ID_SEQ = new AtomicInteger(1);
    private static final AtomicInteger PRODUCER_ID_SEQ = new AtomicInteger(1);
    private static final AtomicInteger TRANSACTION_PRODUCER_ID_SEQ = new AtomicInteger(1);

    public static DefaultMQProducer createDefaultProducer(MQProducerConfig config) {
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL, "ERROR");
        String group = config.getGroup();
        AclClientRPCHook hook = AclHookFactory.create(config.getVendorConfig());

        DefaultMQProducer producer;
        if (hook == null) {
            producer = new DefaultMQProducer(group);
        } else {
            producer = new DefaultMQProducer(group, hook);
            producer.setAccessChannel(AccessChannel.CLOUD);
        }

        producer.setNamesrvAddr(config.getVendorConfig().getNameServer());

        String instanceName = StringUtil.joinWith("-", "producer", config.getVendorConfig().getVendorId(), PRODUCER_ID_SEQ.getAndIncrement());
        producer.setInstanceName(instanceName);
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);

        return producer;
    }

    public static TransactionMQProducerCustomized createTransactionProducer(MQTransactionProducerConfig config) {
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL, "ERROR");

        AclClientRPCHook hook = AclHookFactory.create(config.getVendorConfig());
        TransactionMQProducerCustomized producer;

        if (hook == null) {
            producer = new TransactionMQProducerCustomized(config.getGroup());
        } else {
            producer = new TransactionMQProducerCustomized(
                    config.getGroup(),
                    hook
            );
            producer.setAccessChannel(AccessChannel.CLOUD);
        }

        producer.setNamesrvAddr(config.getVendorConfig().getNameServer());

        String instanceName = StringUtil.joinWith("-", "trxn-producer", config.getVendorConfig().getVendorId(), TRANSACTION_PRODUCER_ID_SEQ.getAndIncrement());
        producer.setInstanceName(instanceName);

        producer.setTransactionListener(
            new RocketTransactionListener(config)
        );
        producer.setCheckThreadPoolMinSize(config.getMinCheckThreadNum());
        producer.setCheckThreadPoolMaxSize(config.getMaxCheckThreadNum());
        producer.setCheckRequestHoldMax(config.getMaxRequestHold());
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);

        return producer;
    }

    public static DefaultMQPushConsumer createPushConsumer(MQConsumerConfig config) {
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL, "ERROR");

        DefaultMQPushConsumer consumer;

        AclClientRPCHook hook = AclHookFactory.create(config.getVendorConfig());
        if (null == hook) {
            consumer = new DefaultMQPushConsumer(config.getGroup());
        } else {
            consumer = new DefaultMQPushConsumer(config.getGroup(), hook, new AllocateMessageQueueAveragely(), true, null);
            consumer.setAccessChannel(AccessChannel.CLOUD);
        }

        consumer.setNamesrvAddr(config.getVendorConfig().getNameServer());

        String instanceName = StringUtil.joinWith("-", config.getConsumer().getClass().getSimpleName(), config.getVendorConfig().getVendorId(), CONSUMER_ID_SEQ.getAndIncrement());
        consumer.setInstanceName(instanceName);

        consumer.setConsumeThreadMin(config.getGroupConfig().getMinThreadNum());
        consumer.setConsumeThreadMax(config.getGroupConfig().getMaxThreadNum());

        if (config.getGroupConfig().isConsumeOrderly()) {
            consumer.registerMessageListener(new RocketConsumerListenerOrderly(config.getConsumer()));
        } else {
            consumer.registerMessageListener(new RocketConsumerListener(config.getConsumer()));
        }

        return consumer;
    }
}
