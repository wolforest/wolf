package com.wolf.mqclient.vendor.rocketmq.consumer;

import com.wolf.mqclient.core.consumer.ConsumeResult;
import com.wolf.mqclient.core.consumer.Consumer;
import com.wolf.mqclient.core.message.Message;
import com.wolf.mqclient.vendor.rocketmq.converter.MessageConverter;
import com.wolf.mqclient.vendor.rocketmq.converter.result.ConsumeResultConverter;
import java.util.List;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author weixing
 * @since 2022/12/12 20:54
 */
public class RocketConsumerListenerOrderly implements MessageListenerOrderly {

    private final Consumer consumer;

    public RocketConsumerListenerOrderly(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> rocketMessageList, ConsumeOrderlyContext context) {
        List<Message> messageList = MessageConverter.toConsumeMessage(rocketMessageList);
        ConsumeResult result = consumer.consume(messageList);

        return ConsumeResultConverter.toRocketOrderlyStatus(result);
    }
}
