package com.wolf.mqclient.vendor.rocketmq5.consumer;

import com.wolf.common.lang.exception.SystemException;
import com.wolf.common.util.lang.StringUtil;
import com.wolf.mqclient.config.MQConsumerConfig;
import com.wolf.mqclient.core.MQVendor;
import com.wolf.mqclient.core.consumer.VendorConsumer;
import com.wolf.mqclient.vendor.rocketmq5.Rocket5Factory;
import com.wolf.mqclient.vendor.rocketmq5.converter.TagFilterExpressionConverter;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
@Slf4j
public class Rocket5Consumer implements VendorConsumer {
    @Getter
    private final MQConsumerConfig config;

    private PushConsumer consumer;

    private String instanceName;

    private static final AtomicInteger CONSUMER_ID_SEQ = new AtomicInteger(1);

    public Rocket5Consumer(MQConsumerConfig config) {
        this.config = config;
    }

    @Override
    public String getVendorType() {
        return MQVendor.VENDOR_ROCKET_MQ_5;
    }

    @Override
    public String getVendorInstanceName() {
        return instanceName;
    }

    @Override
    public void start() {
        try {
            this.instanceName = StringUtil.joinWith(
                "-", config.getConsumer().getClass().getSimpleName(),
                config.getVendorConfig().getVendorId(), CONSUMER_ID_SEQ.getAndIncrement()
            );

            this.consumer = Rocket5Factory.createPushConsumer(config);

            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        } catch (Exception e) {
            log.error("[MQ] rocketmq5 start failed. consumerId={}, vendor={} group={}, topics={}, tags={} minThreadNum={}, maxThreadNum={} cause: {}",
                instanceName,
                config.getVendorConfig().getVendorId(),
                config.getGroup(),
                config.getTopics(),
                config.getTopicTags(),
                config.getGroupConfig().getMinThreadNum(),
                config.getGroupConfig().getMaxThreadNum(),
                e.getMessage(),
                e
            );

            throw new SystemException("rocketmq5 consumer start failed");
        }
    }

    @Override
    public void suspend() {
        try {
            for (String topic : config.getTopics()) {
                consumer.unsubscribe(topic);
            }
            log.info("[MQ] rocketmq5 consumer [{}] suspend successfully.", instanceName);
        } catch (Exception e) {
            log.error("[MQ] rocketmq5 consumer [{}] unsubscribe failed. ", instanceName, e);
            throw new SystemException("rocketmq5 consumer suspend failed");
        }
    }

    @Override
    public void resume() {
        try {
            for (String topic : config.getTopics()) {
                consumer.subscribe(topic, TagFilterExpressionConverter.to(config, topic));
            }
            log.info("[MQ] rocketmq5 consumer [{}] resume successfully.", instanceName);
        } catch (Exception e) {
            log.error("[MQ] rocketmq5 consumer [{}] subscribe failed. ", instanceName, e);
            throw new SystemException("rocketmq5 consumer resume failed");
        }
    }

    @Override
    public void shutdown() {
        log.info("[MQ] rocketmq5 consumer is going to shutdown. InstanceName={}", instanceName);
        try {
            consumer.close();
        } catch (Exception e) {
            log.error("[MQ] errors occurred while close rocketmq5 consumer. InstanceName={}", instanceName, e);
        }
    }
}
