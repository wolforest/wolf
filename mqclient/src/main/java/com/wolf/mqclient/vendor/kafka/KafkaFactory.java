package com.wolf.mqclient.vendor.kafka;

import com.wolf.mqclient.config.MQConsumerConfig;
import com.wolf.mqclient.config.MQProducerConfig;
import com.wolf.mqclient.vendor.kafka.consumer.ClusterSwitchListener;
import java.net.InetAddress;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Kafka Factory
 *
 * @author: YIK
 * @since: 2022/2/24 7:02 PM
 */
@Slf4j
public class KafkaFactory {

    private static final AtomicInteger CONSUMER_ID_SEQ = new AtomicInteger(1);
    private static final AtomicInteger PRODUCER_ID_SEQ = new AtomicInteger(1);

    @SneakyThrows
    public static KafkaProducer createKafkaProducer(MQProducerConfig producerConfig) {
        Properties config = new Properties();

        config.put("client.id", InetAddress.getLocalHost().getHostName() + "-kafka-" + PRODUCER_ID_SEQ.getAndIncrement());
        config.put("bootstrap.servers", producerConfig.getVendorConfig().getNameServer());
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        config.put("acks", "all");

        return new KafkaProducer(config);
    }

    @SneakyThrows
    public static KafkaConsumer createKafkaConsumer(MQConsumerConfig consumerConfig) {
        String servers = consumerConfig.getVendorConfig().getNameServer();
        final String groupId = consumerConfig.getGroup();

        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName() + "-kafka-" + CONSUMER_ID_SEQ.getAndIncrement());
        config.put("bootstrap.servers", servers);
        config.put("enable.auto.commit", false);
        config.put("max.partition.fetch.bytes", 4 * 1024 * 1024);
        config.put("max.poll.records", 1000);

        config.put("group.id", groupId);
        config.put("key.deserializer", StringDeserializer.class.getName());
        config.put("value.deserializer", ByteArrayDeserializer.class.getName());
        config.put("interceptor.classes", ClusterSwitchListener.class.getName());

        String username = consumerConfig.getVendorConfig().getUsername();
        String password = consumerConfig.getVendorConfig().getPassword();
        if (null != username && null != password) {
            config.put("sasl.mechanism", "PLAIN");
            config.put("security.protocol", "SASL_PLAINTEXT");
            String jaas = buildJaasConfig(groupId, username, password);
            config.setProperty("sasl.jaas.config", jaas);
        }

        org.apache.kafka.clients.consumer.KafkaConsumer realKafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer(config);

        return realKafkaConsumer;
    }

    public static String buildJaasConfig(String sid, String user, String password) {
        String jaasTemplate = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s-%s\" password=\"%s\";";
        return String.format(jaasTemplate, user, sid, password);
    }
}
