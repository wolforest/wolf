package com.wolf.mqclient.vendor.kafka.consumer;

import com.wolf.mqclient.config.MQConsumerConfig;
import com.wolf.mqclient.core.MQVendor;
import com.wolf.mqclient.core.consumer.ConsumeResult;
import com.wolf.mqclient.core.consumer.ConsumeStateEnum;
import com.wolf.mqclient.core.consumer.Consumer;
import com.wolf.mqclient.core.consumer.VendorConsumer;
import com.wolf.mqclient.core.message.Message;
import com.wolf.mqclient.vendor.kafka.KafkaFactory;
import com.wolf.mqclient.vendor.kafka.converter.MessageConverter;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;

/**
 * The Kafka Consumer implement
 *
 * @author: YIK
 * @since: 2022/2/24 7:01 PM
 */
@Slf4j
public class KafkaConsumer implements VendorConsumer {

    private final MQConsumerConfig config;

    private org.apache.kafka.clients.consumer.KafkaConsumer consumer;

    private KafkaConsumeLoop loop;

    public KafkaConsumer(MQConsumerConfig config) {
        this.config = config;
        this.consumer = KafkaFactory.createKafkaConsumer(config);
        this.loop = new KafkaConsumeLoop(config, consumer);
    }

    @Override
    public String getVendorType() {
        return MQVendor.VENDOR_KAFKA;
    }

    @Override
    public String getVendorInstanceName() {
        return "";
    }

    @Override
    public void start() {
        try {
            new Thread(loop, "kafka-consumer-thread").start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                loop.shutdown();
            }));
        } catch (Exception e) {

        }
    }

    @Override
    public void suspend() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void shutdown() {
        log.info("Kafka consumer is going to shutdown. Instance name: {}", "");
        try {
            loop.shutdown();
        } catch (Exception e) {
            log.error("errors occurred while close Kafka consumer.", e);
        }
    }

    @Slf4j
    public static class KafkaConsumeLoop implements Runnable {
        private final org.apache.kafka.clients.consumer.KafkaConsumer consumer;
        private final MQConsumerConfig config;
        private final List<String> topics;
        private final AtomicBoolean shutdown;
        private final CountDownLatch shutdownLatch;
        private final Consumer listener;

        public KafkaConsumeLoop(MQConsumerConfig config, org.apache.kafka.clients.consumer.KafkaConsumer consumer) {
            this.consumer = consumer;

            this.config = config;

            this.topics = config.getTopics();
            this.listener = config.getConsumer();

            this.shutdown = new AtomicBoolean(false);
            this.shutdownLatch = new CountDownLatch(1);
        }

        @Override
        public void run() {

            consumer.subscribe(topics, new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    log.info("Kafka partition revoked for [{}]", StringUtils.join(partitions, ","));
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                    log.info("Kafka onPartitionsAssigned for [{}]", StringUtils.join(partitions, ","));
                    for (String topic : config.getTopics()) {
                        partitions.stream()
                            .filter(topicPartition -> topicPartition.topic().equals(topic))
                            .forEach(topicPartition -> seek(topicPartition));
                    }
                }
            });


            try {
                while (!shutdown.get()) {
                    ConsumerRecords records = consumer.poll(Duration.ofSeconds(2));
                    if (records == null || records.isEmpty()) {
                        continue;
                    }
                    List<Message> messageList = MessageConverter.toConsume(records);
                    if (messageList.isEmpty()) {
                        continue;
                    }
                    ConsumeResult result = null;
                    try {
                        result = listener.consume(messageList);
                    } catch (Exception e) {
                        log.error("Process message error exception: {}", e);
                        throw e;
                    }

                    if (ConsumeStateEnum.SUCCESS == result.getState() && result.getLastRecord() != null) {
                        doAsyncCommit(result);
                    }


                }
            } catch (Exception e) {
                log.error("Kafka consumer loop Unexpected error,consumer thread will close,exception:{}", e);
            } finally {
                consumer.close();
                shutdownLatch.countDown();
            }
        }

        private void doAsyncCommit(ConsumeResult result) {
            String topic = result.getLastRecord().getTopic();
            int partition = result.getLastRecord().getMessageContext().getShardId();
            long offset = result.getLastRecord().getMessageContext().getOffset();
            long timestamp = result.getLastRecord().getMessageContext().getTimestamp();
            Map<TopicPartition, OffsetAndMetadata> offsets = Collections.singletonMap(new TopicPartition(topic, partition), new OffsetAndMetadata(offset, String.valueOf(timestamp)));

            consumer.commitAsync(offsets, (commitOffsets, exception) -> {
                if (exception == null) {
                    log.debug("Kafka commit success topic: {}, partition: {}, offset: {}, timestamp: {}", topic, partition, offset, timestamp);
                } else {
                    log.error("Kafka commit failed topic: {}, partition: {}, offset: {}, timestamp: {}, exception: {}", topic, partition, offset, timestamp, exception);
                }

            });
        }


        public void shutdown() {
            if (!shutdown.compareAndSet(false, true)) {
                return;
            }
            try {
                shutdownLatch.await();
            } catch (InterruptedException e) {
                log.error("shutdown kafka consumer error", e);
                Thread.currentThread().interrupt();
            }
        }

        private final void seek(TopicPartition topicPartition) {
            //ConsumerBuilder.CheckPoint checkPoint = consumerBuilder.getCheckPoint();
            MQConsumerConfig.CheckPoint checkPoint = config.getCheckPoint();
            long timestamp = checkPoint.getTimestamp();
            OffsetAndMetadata offsetAndMetadata = consumer.committed(topicPartition);
            if (!checkPoint.isForce() && null != offsetAndMetadata) {
                log.info("consumer committed offsetAndMetadata: {}", offsetAndMetadata);
                timestamp = Long.parseLong(offsetAndMetadata.metadata());
                timestamp = (timestamp / 1000) - 1;
            }
            setFetchOffsetByTimestamp(topicPartition, timestamp);
        }

        private void setFetchOffsetByTimestamp(TopicPartition topicPartition, long timeStamp) {
            log.info("start setFetchOffsetByTimestamp for {} by timeStamp {} ", topicPartition, timeStamp);
            Map<TopicPartition, OffsetAndTimestamp> remoteOffset = consumer.offsetsForTimes(Collections.singletonMap(topicPartition, timeStamp));
            OffsetAndTimestamp toSet = remoteOffset.get(topicPartition);
            if (null == toSet) {
                throw new RuntimeException("seek timestamp for topic [" + topicPartition + "] with timestamp [" + timeStamp + "] failed");
            }
            consumer.seek(topicPartition, toSet.offset());
            log.info("end setFetchOffsetByTimestamp for {} to timeStamp {}, offset {} success", topicPartition, timeStamp, toSet.offset());
        }
    }

}
