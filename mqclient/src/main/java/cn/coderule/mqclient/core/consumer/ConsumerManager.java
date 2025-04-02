package cn.coderule.mqclient.core.consumer;

import cn.coderule.common.util.lang.collection.MapUtil;
import cn.coderule.mqclient.annotation.MQConsumer;
import cn.coderule.mqclient.config.MQConfig;
import cn.coderule.mqclient.config.MQConsumerConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weixing
 * @since 2022/10/11 17:15
 */
@Slf4j
public class ConsumerManager {

    private final MQConfig mqConfig;

    private final VendorConsumerFactory vendorConsumerFactory;
    @Getter
    private final Map<String/*ConsumerClassName*/, Consumer> consumers = new HashMap<>();
    @Getter
    private final Map<Consumer, VendorConsumer> vendorConsumers = new HashMap<>();

    private static final AtomicInteger CONSUMER_PROXY_ID_SEQ = new AtomicInteger(1);

    public ConsumerManager(MQConfig mqConfig) {
        this.mqConfig = mqConfig;
        this.vendorConsumerFactory = new VendorConsumerFactory();
    }

    public void startWithAnnotation(String name, Consumer consumer) {
        MQConsumer annotation = getConsumerAnnotation(consumer);
        if (null == annotation) {
            log.error("[MQ] consumer can not start, because the MQConsumer annotation not configured on class. name={}", name);
            return;
        }

        MQConsumerConfig config = mqConfig.getConsumerConfig(annotation);
        config.setConsumer(consumer);
        config.setName(name);

        start(config);
    }

    public void start(MQConsumerConfig config) {
        if (!checkConfig(config)) {
            return;
        }

        if (!config.getGroupConfig().isProxyMode()) {
            registerSubscription(config);
            startConsumer(config);
        } else {
            SubscriptionRegistry.registerForProxyMode(config);
        }
    }

    public void startConsumerProxies() {
        SubscriptionRegistry.getProxiedSubscriptionMap().forEach((group, subscriptions) -> {
            MQConsumerConfig config = mqConfig.getConsumerConfig(group, subscriptions);

            ConsumerProxy consumer = new ConsumerProxy(config.getGroup());
            config.setConsumer(consumer);

            String name = "ConsumerProxy-" + CONSUMER_PROXY_ID_SEQ.getAndIncrement();
            config.setName(name);

            startConsumer(config);
        });
    }

    private void startConsumer(MQConsumerConfig config) {
        String name = config.getName();
        for (int i = 1; i <= config.getGroupConfig().getConsumerNum(); i++) {
            VendorConsumer consumer = vendorConsumerFactory.create(config);
            consumer.start();
            putConsumer(name, config.getConsumer());
            vendorConsumers.put(config.getConsumer(), consumer);
            log.info("[MQ] consumer has been started. consumerId={}, proxyMode={} vendor={} group={}, topics={}, tags={} minThreadNum={}, maxThreadNum={}",
                consumer.getVendorInstanceName(),
                config.getGroupConfig().isProxyMode(),
                config.getVendorConfig().getVendorId(),
                config.getGroup(),
                config.getTopics(),
                config.getTopicTags(),
                config.getGroupConfig().getMinThreadNum(),
                config.getGroupConfig().getMaxThreadNum()
            );
        }
    }

    private boolean checkConfig(MQConsumerConfig config) {
        String name = config.getName();
        if (!config.getVendorConfig().isEnabled()) {
            log.info("[MQ] consumer can not start, because the vendor is disabled. name={} vendorId={}", name, config.getVendorConfig().getVendorId());
            return false;
        }

        if (!config.getGroupConfig().isEnabled()) {
            log.info("[MQ] consumer can not start, because the group is disabled. name={} group={}", name, config.getGroup());
            return false;
        }
        return true;
    }

    private void registerSubscription(MQConsumerConfig config) {
        SubscriptionRegistry.register(
            config.getGroup(),
            config.getTopics().toArray(String[]::new),
            config.getTopicTags().values().stream().flatMap(Set::stream).distinct().toArray(String[]::new)
        );
    }

    public void restart(String name) {
        shutdown(name);
        Consumer consumer = consumers.get(name);
        // todolist check consumer have annotation
        // todolist support proxy mode consumer
        startWithAnnotation(name, consumer);
    }

    public void suspend(String name) {
        Consumer consumer = consumers.get(name);
        if (null == consumer) {
            return;
        }

        VendorConsumer client = vendorConsumers.get(consumer);
        if (null == client) {
            return;
        }

        client.suspend();
    }

    public void resume(String name) {
        Consumer consumer = consumers.get(name);
        if (null == consumer) {
            return;
        }

        VendorConsumer client = vendorConsumers.get(consumer);
        if (null == client) {
            return;
        }

        client.resume();
    }

    public void shutdown(String name) {
        Consumer consumer = consumers.get(name);
        if (null == consumer) {
            return;
        }

        VendorConsumer client = vendorConsumers.get(consumer);
        if (null == client) {
            return;
        }

        client.shutdown();
    }

    public void shutdownAll() {
        if (MapUtil.isEmpty(vendorConsumers)) {
            return;
        }

        vendorConsumers.values().forEach(VendorConsumer::shutdown);
    }

    public Consumer getConsumer(String name) {
        return consumers.get(name);
    }

    public void putConsumer(String name, Consumer consumer) {
        String fixedName = name;
        if (consumers.containsKey(name)) {
            fixedName = name + "-" + consumers.keySet().stream().filter(key -> key.startsWith(name)).count();
        }
        consumers.put(fixedName, consumer);
    }

    public MQConsumer getConsumerAnnotation(Consumer consumer) {
        //Class<?> targetClass = AopUtils.getTargetClass(consumer);
        Class<?> targetClass = consumer.getClass();
        return targetClass.getAnnotation(MQConsumer.class);
    }
}
