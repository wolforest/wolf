package cn.coderule.mqclient.vendor.rocketmq.producer;

import cn.coderule.common.util.lang.time.DateUtil;
import cn.coderule.mqclient.config.MQProducerConfig;
import cn.coderule.mqclient.core.MQVendor;
import cn.coderule.mqclient.core.message.MessageSendTypeEnum;
import cn.coderule.mqclient.core.producer.ProduceCallback;
import cn.coderule.mqclient.core.producer.ProduceRequest;
import cn.coderule.mqclient.core.producer.ProduceResult;
import cn.coderule.mqclient.core.producer.VendorProducer;
import cn.coderule.mqclient.core.producer.exception.ProducerSendFailedException;
import cn.coderule.mqclient.core.producer.exception.ProducerStartFailedException;
import cn.coderule.mqclient.vendor.rocketmq.converter.MessageConverter;
import cn.coderule.mqclient.vendor.rocketmq.converter.result.ProduceResultConverter;
import cn.coderule.mqclient.vendor.rocketmq.RocketFactory;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * com.wolf.mqclient.adapter.rocketmq.producer
 *
 * @author Wingle
 * @since 2021/12/13 下午9:46
 **/
@Slf4j
public class RocketProducer implements VendorProducer {

    private static final String MESSAGE_DELAY_KEY = "__STARTDELIVERTIME";

    //@Getter
    private final MQProducerConfig config;

    //@Getter
    private DefaultMQProducer producer;

    public RocketProducer(MQProducerConfig config) {
        this.config = config;
        this.producer = RocketFactory.createDefaultProducer(config);
    }

    @Override
    public String getVendorType() {
        return MQVendor.VENDOR_ROCKET_MQ;
    }

    @Override
    public String getVendorInstanceName() {
        return producer.getInstanceName();
    }

    @Override
    public void start() {
        try {
            producer.start();
            /*
            // here we do not need to add shutdown hook.
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("ShutdownHook RocketMQ producer is going to close");
                producer.shutdown();
            }));*/
        } catch (MQClientException e) {
            log.error("mq producer start failed: ", e);
            throw new ProducerStartFailedException("mq producer start failed");
        }
    }

    @Override
    public void shutdown() {
        log.info("RocketMQ producer is going to close. InstanceName={}", getVendorInstanceName());
        try {
            producer.shutdown();
        } catch (Exception e) {
            log.error("errors occurred while close rocket producer.", e);
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
        Message message = MessageConverter.toRocketMessage(produceRequest);

        try {
            SendResult result = producer.send(message);
            return ProduceResultConverter.from(result);
        } catch (Exception e) {
            log.error("rocket mq send message failed. cause: {}", e.getMessage(), e);
            throw new ProducerSendFailedException("rocket mq send message failed");
        }
    }

    public void asyncSend(ProduceRequest produceRequest, ProduceCallback callback) {
        SendCallback sendCallback = new AsyncCallback(callback);
        Message message = MessageConverter.toRocketMessage(produceRequest);
        producer.setRetryTimesWhenSendAsyncFailed(0);

        try {
            producer.send(message, sendCallback);
        } catch (Exception e) {
            log.error("rocket mq async send message failed. cause: {}", e.getMessage(), e);
            throw new ProducerSendFailedException("rocket mq async send message failed");
        }

    }

    public ProduceResult oneWaySend(ProduceRequest produceRequest) {
        Message message = MessageConverter.toRocketMessage(produceRequest);

        try {
            producer.sendOneway(message);
            return ProduceResult.SUCCESS();
        } catch (Exception e) {
            log.error("rocket mq oneway send message failed. cause: {}", e.getMessage(), e);
            throw new ProducerSendFailedException("rocket mq send oneWay message failed");
        }
    }

    public ProduceResult orderedSend(ProduceRequest produceRequest) {
        Object orderBy = produceRequest.getOrderBy();
        if (null == orderBy) {
            return syncSend(produceRequest);
        }

        if (!(orderBy instanceof Integer) && !(orderBy instanceof Long)) {
            return syncSend(produceRequest);
        }

        MessageQueueSelector selector = new OrderSelector();
        Message message = MessageConverter.toRocketMessage(produceRequest);

        try {
            SendResult result = producer.send(message, selector, orderBy);
            return ProduceResultConverter.from(result);
        } catch (Exception e) {
            log.error("rocket mq ordered send message failed. cause: {}", e.getMessage(), e);
            throw new ProducerSendFailedException("rocket mq send ordered message failed");
        }
    }

    public ProduceResult scheduledSend(ProduceRequest produceRequest) {
        Message message = MessageConverter.toRocketMessage(produceRequest);

        String delayTimestamp = getDelayTimestamp(produceRequest);
        if (null != delayTimestamp) {
            message.putUserProperty(MESSAGE_DELAY_KEY, delayTimestamp);

            if (AccessChannel.LOCAL.equals(producer.getAccessChannel())) {
                //wait 10s
                message.setDelayTimeLevel(3);
            }
        }

        try {
            //log.info("message={}, delayTimeLeveal={}, delayTimestamp={}", message, message.getDelayTimeLevel(), delayTimestamp);
            SendResult result = producer.send(message);
            return ProduceResultConverter.from(result);
        } catch (Exception e) {
            log.error("rocket mq scheduled send message failed. cause: {}", e.getMessage(), e);
            throw new ProducerSendFailedException("rocket mq send message failed");
        }
    }

    private String getDelayTimestamp(ProduceRequest produceRequest) {
        LocalDateTime activeAt = produceRequest.getActiveAt();
        if (null != activeAt) {
            Long ts = DateUtil.asEpochMilli(activeAt);
            return String.valueOf(ts);
        }

        Long delay = produceRequest.getDelay();
        if (null != delay) {
            Long ts = System.currentTimeMillis() + delay;
            return String.valueOf(ts);
        }

        return null;
    }

}
