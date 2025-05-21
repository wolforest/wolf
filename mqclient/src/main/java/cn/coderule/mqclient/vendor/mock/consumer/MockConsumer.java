package cn.coderule.mqclient.vendor.mock.consumer;

import cn.coderule.common.lang.exception.SystemException;
import cn.coderule.common.util.lang.string.StringUtil;
import cn.coderule.mqclient.config.MQConsumerConfig;
import cn.coderule.mqclient.core.MQVendor;
import cn.coderule.mqclient.core.consumer.VendorConsumer;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
@Slf4j
public class MockConsumer implements VendorConsumer {
    @Getter
    private final MQConsumerConfig config;

    private String instanceName;

    private static final AtomicInteger CONSUMER_ID_SEQ = new AtomicInteger(1);

    public MockConsumer(MQConsumerConfig config) {
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

            //this.consumer = Rocket5Factory.createPushConsumer(config);

            //Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        } catch (Exception e) {
            log.error("[MQ] mockmq start failed. consumerId={}, vendor={} group={}, topics={}, tags={} minThreadNum={}, maxThreadNum={} cause: {}",
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

            throw new SystemException("mockmq consumer start failed");
        }
    }

    @Override
    public void suspend() {
        log.info("[MQ] mockmq consumer [{}] suspend successfully.", instanceName);
    }

    @Override
    public void resume() {
        log.info("[MQ] mockmq consumer [{}] resume successfully.", instanceName);
    }

    @Override
    public void shutdown() {
        log.info("[MQ] mockmq consumer is going to shutdown. InstanceName={}", instanceName);
    }
}
