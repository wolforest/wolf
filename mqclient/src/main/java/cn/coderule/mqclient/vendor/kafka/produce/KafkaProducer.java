package cn.coderule.mqclient.vendor.kafka.produce;

import cn.coderule.common.lang.exception.SystemException;
import cn.coderule.mqclient.config.MQProducerConfig;
import cn.coderule.mqclient.core.MQVendor;
import cn.coderule.mqclient.core.message.MessageSendTypeEnum;
import cn.coderule.mqclient.core.producer.ProduceCallback;
import cn.coderule.mqclient.core.producer.ProduceRequest;
import cn.coderule.mqclient.core.producer.ProduceResult;
import cn.coderule.mqclient.core.producer.VendorProducer;
import cn.coderule.mqclient.core.producer.exception.ProducerSendFailedException;
import cn.coderule.mqclient.vendor.kafka.KafkaFactory;
import cn.coderule.mqclient.vendor.kafka.converter.MessageConverter;
import cn.coderule.mqclient.vendor.kafka.converter.result.ProduceResultConverter;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * The kafka mq producer implement
 *
 * @author: YIK
 * @since: 2022/2/25 1:46 PM
 */
@Slf4j
public class KafkaProducer implements VendorProducer {

    private final MQProducerConfig config;

    //It's thread-safe. Share instance
    private static org.apache.kafka.clients.producer.KafkaProducer producer;

    public KafkaProducer(MQProducerConfig config) {
        this.config = config;
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
    public synchronized void start() {
        if (producer == null) {
            producer = KafkaFactory.createKafkaProducer(config);
            log.info("Kafka producer start success");

            /*
            // here we do not need to add shutdown hook.
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("ShutdownHook Kafka producer is going to close");
                producer.close();
            }));*/
        }
    }

    @Override
    public synchronized void shutdown() {
        log.info("Kafka producer is going to close...");
        try {
            producer.close();
        } catch (Exception e) {
            log.error("errors occurred while close kafka producer.", e);
        }
    }

    @Override
    public ProduceResult send(ProduceRequest produceRequest) {
        MessageSendTypeEnum sendType = produceRequest.getSendType();
        if (null == sendType) {
            return syncSend(produceRequest);
        }

        switch (sendType) {
            case ONE_WAY:
                return oneWaySend(produceRequest);
            case SCHEDULE:
                return scheduledSend(produceRequest);
            case ORDER:
                return orderedSend(produceRequest);
            default:
                return syncSend(produceRequest);
        }
    }

    @Override
    public void send(ProduceRequest produceRequest, ProduceCallback callback) {
        asyncSend(produceRequest, callback);
    }

    public ProduceResult syncSend(ProduceRequest produceRequest) {
        ProducerRecord message = MessageConverter.toKafka(produceRequest);

        try {
            Future<RecordMetadata> future = producer.send(message);
            RecordMetadata metadata = future.get();
            return ProduceResultConverter.from(metadata, message.key().toString());
        } catch (Exception e) {
            log.error("kafka mq send message failed: ", e);
            throw new ProducerSendFailedException("kafka mq send message failed");
        }
    }

    public void asyncSend(ProduceRequest produceRequest, ProduceCallback callback) {
        ProducerRecord message = MessageConverter.toKafka(produceRequest);
        try {
            producer.send(message, (recordMetadata, e) -> {
                ProduceResult produceResult = null;
                if (e == null && recordMetadata != null) {
                    produceResult = ProduceResultConverter.from(recordMetadata, message.key().toString());
                }
                callback.onComplete(produceResult, e);
            });

        } catch (Exception e) {
            log.error("kafka mq send message failed: ", e);
            throw new ProducerSendFailedException("kafka mq send message failed");
        }
    }

    public ProduceResult scheduledSend(ProduceRequest produceRequest) {
        throw new SystemException("scheduledSend");
    }

    public ProduceResult oneWaySend(ProduceRequest produceRequest) {
        //TODO @YIK I will implement it by create a new producer that set "acks" property to 0
        throw new SystemException("oneWaySend");
    }

    public ProduceResult orderedSend(ProduceRequest produceRequest) {
        throw new SystemException("orderedSend");
    }
}
