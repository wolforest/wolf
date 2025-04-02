package cn.coderule.mqclient.vendor.kafka.converter;

import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.core.message.MessageContext;
import cn.coderule.mqclient.core.producer.ProduceRequest;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author weixing
 * @since 2022/8/26 15:54
 */
@Slf4j
public class MessageConverter {
    public static ProducerRecord<String, byte[]> toKafka(ProduceRequest produceRequest) {
        Message message = produceRequest.getMessage();
        //TODO put headers
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>(message.getTopic(), message.getId(), message.getBody());
        return producerRecord;

    }

    public static List<Message> toConsume(ConsumerRecords<String, byte[]> records) {
        return StreamSupport.stream(records.spliterator(), false).map(MessageConverter::toConsume).collect(Collectors.toList());
    }

    public static Message toConsume(ConsumerRecord<String, byte[]> kafkaRecord) {
        Message message = new Message();
        try {
            message.setId(kafkaRecord.key());
            message.setTopic(kafkaRecord.topic());
            message.setBody(kafkaRecord.value());
            message.setProperties(new HashMap<>());//TODO convert record.headers to hashMap
            message.setTransactionId(null);//kafka do not support transaction message

            MessageContext context = message.getMessageContext();
            context.setMqId(kafkaRecord.key());
            context.setShardId(kafkaRecord.partition());
            context.setOffset(kafkaRecord.offset());
            context.setTimestamp(kafkaRecord.timestamp());//it's should be change to dts record.getSourceTimestamp by other
            //context.setReconsumeTimes();//Kafka do dot support
        } catch (Exception e) {
            log.error("convert consumerRecord to message error", e);
            throw e;
        }
        return message;
    }
}
