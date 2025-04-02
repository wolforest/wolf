package com.wolf.mqclient.vendor.rocketmq5;

import com.wolf.mqclient.config.MQConsumerConfig;
import com.wolf.mqclient.config.MQProducerConfig;
import com.wolf.mqclient.config.MQTransactionProducerConfig;
import com.wolf.mqclient.vendor.rocketmq5.acl.SessionCredentialProviderFactory;
import com.wolf.mqclient.vendor.rocketmq5.consumer.Rocket5ConsumerListener;
import com.wolf.mqclient.vendor.rocketmq5.converter.TagFilterExpressionConverter;
import com.wolf.mqclient.vendor.rocketmq5.transaction.Rocket5TransactionChecker;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.SessionCredentialsProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.apache.rocketmq.client.apis.producer.Producer;

/**
 * @author weixing
 * @since 2023/6/9 16:27
 */
@Slf4j
public class Rocket5Factory {
    public static Producer createProducer(MQProducerConfig config) throws ClientException {
        SessionCredentialsProvider sessionCredentialsProvider = SessionCredentialProviderFactory.create(config.getVendorConfig());

        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder()
            .setEndpoints(config.getVendorConfig().getEndpoints());

        if (null != sessionCredentialsProvider) {
            builder.setCredentialProvider(sessionCredentialsProvider);
        }

        ClientConfiguration clientConfiguration = builder.build();

        ClientServiceProvider provider = Rocket5ClientManager.getInstance().getClientServiceProvider();
        Producer producer = provider.newProducerBuilder()
            .setClientConfiguration(clientConfiguration)
            // Set the topic name(s), which is optional but recommended. It makes producer could prefetch the topic
            // route before message publishing.
            //.setTopics("DEFAULT_TOPIC")
            .build();

        return producer;
    }

    public static Producer createTransactionProducer(MQTransactionProducerConfig config) throws ClientException {
        SessionCredentialsProvider sessionCredentialsProvider = SessionCredentialProviderFactory.create(config.getVendorConfig());

        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder()
            .setEndpoints(config.getVendorConfig().getEndpoints());

        if (null != sessionCredentialsProvider) {
            builder.setCredentialProvider(sessionCredentialsProvider);
        }

        ClientConfiguration clientConfiguration = builder.build();

        ClientServiceProvider provider = Rocket5ClientManager.getInstance().getClientServiceProvider();
        Producer producer = provider.newProducerBuilder()
            .setClientConfiguration(clientConfiguration)
            // Set the topic name(s), which is optional but recommended. It makes producer could prefetch the topic
            // route before message publishing.
            .setTopics(config.getTopic())
            .setTransactionChecker(new Rocket5TransactionChecker(config))
            .build();

        return producer;
    }

    public static PushConsumer createPushConsumer(MQConsumerConfig config) throws ClientException {
        SessionCredentialsProvider sessionCredentialsProvider = SessionCredentialProviderFactory.create(config.getVendorConfig());
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder()
            .setEndpoints(config.getVendorConfig().getEndpoints());

        if (null != sessionCredentialsProvider) {
            builder.setCredentialProvider(sessionCredentialsProvider);
        }

        ClientConfiguration clientConfiguration = builder.build();

        Rocket5ConsumerListener listener = new Rocket5ConsumerListener(config.getConsumer());

        ClientServiceProvider provider = Rocket5ClientManager.getInstance().getClientServiceProvider();
        PushConsumer consumer;

        Map<String, FilterExpression> subscriptions = new HashMap<>();
        for (String topic : config.getTopics()) {
            subscriptions.put(topic, TagFilterExpressionConverter.to(config, topic));
        }

        consumer = provider.newPushConsumerBuilder()
            .setClientConfiguration(clientConfiguration)
            .setConsumerGroup(config.getGroup())
            .setSubscriptionExpressions(subscriptions)
            .setMessageListener(listener)
            .setConsumptionThreadCount(config.getGroupConfig().getMaxThreadNum())
            .build();

        return consumer;
    }
}
