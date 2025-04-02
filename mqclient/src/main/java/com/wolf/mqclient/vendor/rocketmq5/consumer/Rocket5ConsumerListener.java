package com.wolf.mqclient.vendor.rocketmq5.consumer;

import com.wolf.mqclient.MQTemplate;
import com.wolf.mqclient.config.MQDefaultConst;
import com.wolf.mqclient.core.consumer.Consumer;
import com.wolf.mqclient.core.message.Message;
import com.wolf.mqclient.vendor.rocketmq5.converter.MessageConverter;
import com.wolf.mqclient.vendor.rocketmq5.converter.result.ConsumeResultConverter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.java.message.MessageViewImpl;

/**
 * @author weixing
 * @since 2023/6/9 16:44
 */
@Slf4j
public class Rocket5ConsumerListener implements MessageListener {

    public static final long MIN_DELAY_MILLISECONDS = 1000L;

    private final Consumer consumer;

    public Rocket5ConsumerListener(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public ConsumeResult consume(MessageView messageView) {
        //log.info("[MQ] message received. messageView={}", messageView);

        if (null == consumer) {
            return ConsumeResult.SUCCESS;
        }

        Message message = MessageConverter.toConsumeMessage(messageView);
        if (messageView.getDeliveryTimestamp().isPresent()) {
            long currentTimeMillis = System.currentTimeMillis();
            long diff = currentTimeMillis - messageView.getDeliveryTimestamp().get();
            if (!message.isRetried() && diff > MQDefaultConst.MAX_ACCEPTABLE_ACTUAL_DELIVERY_DELAY_MILLISECOND) {
                MessageViewImpl messageViewImpl = (MessageViewImpl) messageView;
                log.warn("[MQ] delay message may not delivered/consume accurately. msgKeys={} msgId={} topic={} tag={} born={} expect={} transport={} decode={} arrived={} diff={}",
                    message.getId(), message.getMessageContext().getMqId(), message.getTopic(), message.getTag(), message.getMessageContext().getTimestamp(), messageView.getDeliveryTimestamp(), messageViewImpl.getTransportDeliveryTimestamp(), messageViewImpl.getDecodeTimestamp(), currentTimeMillis, diff);
            }

            if (message.getProperties().containsKey(MQDefaultConst.MESSAGE_PROPERTY_DELIVERY_TIMESTAMP)) {
                Long expectedDeliveryTime = Long.valueOf(message.getProperties().get(MQDefaultConst.MESSAGE_PROPERTY_DELIVERY_TIMESTAMP));
                long delay = expectedDeliveryTime - currentTimeMillis;
                if (delay > MIN_DELAY_MILLISECONDS) {
                    // may encounter an exception when resending the message
                    try {
                        MQTemplate.getInstance().produce()
                            .id(message.getId())
                            .topic(message.getTopic())
                            .message(message.getBody())
                            .tag(message.getTag())
                            .property(message.getProperties())
                            .delay(delay / 1000)
                            .send();
                        log.info("[MQ] delay message has been resent with deliveryTime rewriting. msgKeys={} msgId={} nextDelay={} topic={} tag={}", message.getId(), message.getMessageContext().getMqId(), delay, message.getTopic(), message.getTag());
                        return ConsumeResult.SUCCESS;
                    } catch (Exception e) {
                        log.error("[MQ] delay message resend failed. msgKeys={} msgId={} nextDelay={} topic={} tag={}", message.getId(), message.getMessageContext().getMqId(), delay, message.getTopic(), message.getTag(), e);
                        return ConsumeResult.FAILURE;
                    }
                }
            }
        }

        List<Message> messageList = List.of(message);
        com.wolf.mqclient.core.consumer.ConsumeResult result = consumer.consume(messageList);

        return ConsumeResultConverter.toRocket5Status(result);
    }
}
