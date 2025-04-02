package cn.coderule.mqclient.starter;

import cn.coderule.mqclient.config.MQConfig;
import cn.coderule.mqclient.config.MQVendorConfig;
import cn.coderule.mqclient.MQTemplate;
import cn.coderule.mqclient.core.consumer.ConsumerManager;
import cn.coderule.mqclient.core.producer.ProducerManager;
import cn.coderule.mqclient.core.transaction.TransactionProducerManager;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Resource;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author weixing
 * @since 2022/10/11 20:43
 */
@Slf4j
@Component
public class MQStarter implements InitializingBean, ApplicationListener<ApplicationContextEvent>, Ordered {
    private final AtomicBoolean started = new AtomicBoolean(false);
    /**
     *
     */
    @Resource
    private MQConfig mqConfig;
    /**
     *
     */
    @Getter
    private ConsumerManager consumerManager;
    /**
     *
     */
    @Getter
    private ProducerManager producerManager;
    /**
     *
     */
    @Getter
    private TransactionProducerManager transactionProducerManager;

    @Bean("mqTemplate")
    public MQTemplate mqTemplate() {
        Assert.notNull(producerManager, "producerManager is not initialized");
        Assert.notNull(transactionProducerManager, "transactionProducerManager is not initialized");
        Assert.notNull(consumerManager, "consumerManager is not initialized");

        return MQTemplate.init(mqConfig, producerManager, transactionProducerManager, consumerManager);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.producerManager = new ProducerManager(mqConfig);
        this.transactionProducerManager = new TransactionProducerManager(mqConfig);
        this.consumerManager = new ConsumerManager(mqConfig);
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            if (event.getApplicationContext().getParent() != null) {
                return;
            }
            start();
        } else if (event instanceof ContextClosedEvent) {
            stop();
        }
    }

    /**
     * start it before dubbo service.
     *
     * @return int
     */
    @Override
    public int getOrder() {
        return 0;
    }

    public void start() {
        if (!mqConfig.validate()) {
            return;
        }

        if (!started.compareAndSet(false, true)) {
            return;
        }

        log.info("[MQ] MQStarter is starting...");

        // Producer should be start first, consumer may depend on the producer.
        startProducer();
//        startTransactionProducer();
//        startConsumer();

        log.info("[MQ] MQStarter has been started.");
    }

    public void stop() {
        if (!started.get()) {
            return;
        }

        log.info("[MQ] MQStarter is going to shutdown...");

        // prioritize closing the consumer as much as possible, consumer may depend on the producer.
        consumerManager.shutdownAll();
        producerManager.shutdownAll();
        transactionProducerManager.shutdownAll();

        started.compareAndSet(true, false);

        log.info("[MQ] MQStarter has been shutdown.");
    }

    public int getPhase() {
        return 0;
        //return Integer.MAX_VALUE - 1;
        //return 2147483647 - 1;
    }

    private void startProducer() {
        for (Map.Entry<String, MQVendorConfig> entry : mqConfig.getVendors().entrySet()) {
            producerManager.start(entry.getValue());
        }
    }

//    private void startTransactionProducer() {
//        Map<String, Object> transactionMap =  ContextUtil.getBeansWithAnnotation(MQTransaction.class);
//        if (transactionMap.isEmpty()) {
//            return;
//        }
//
//        transactionMap.forEach((name, transactionProducer) -> {
//            // It's deprecated after we implement the "begin/commit/rollback" transaction mode for RocketMQ4
//            // It will be removed after RocketMQ5 is stable.
//            if (transactionProducer instanceof TransactionProducer) {
//                transactionProducerManager.registerWithAnnotation(name, (TransactionProducer) transactionProducer);
//            } else if (transactionProducer instanceof TransactionChecker) {
//                transactionProducerManager.registerWithAnnotation(name, (TransactionChecker) transactionProducer);
//            }
//        });
//
//        transactionProducerManager.start();
//    }
//
//    private void startConsumer() {
//        if (MapUtil.isEmpty(mqConfig.getConsumerGroups())) {
//            return;
//        }
//
//        Map<String, Object> consumerMap = ContextUtil.getBeansWithAnnotation(MQConsumer.class);
//        if (consumerMap.isEmpty()) {
//            return;
//        }
//
//        consumerMap.forEach((name, consumer) -> {
//            consumerManager.startWithAnnotation(name, (Consumer) consumer);
//        });
//
//        // start multi topic subscription consumer
//        consumerManager.startConsumerProxies();
//    }

}
