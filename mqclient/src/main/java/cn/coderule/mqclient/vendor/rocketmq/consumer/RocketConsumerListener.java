package cn.coderule.mqclient.vendor.rocketmq.consumer;

import cn.coderule.mqclient.core.consumer.ConsumeResult;
import cn.coderule.mqclient.core.consumer.Consumer;
import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.vendor.rocketmq.converter.MessageConverter;
import cn.coderule.mqclient.vendor.rocketmq.converter.result.ConsumeResultConverter;
import java.util.List;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * com.wolf.mqclient.adapter.rocketmq.consumer
 *
 * @author Wingle
 * @since 2021/12/16 下午8:13
 **/
public class RocketConsumerListener implements MessageListenerConcurrently {

    private final Consumer consumer;

    public RocketConsumerListener(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> rocketMessageList, ConsumeConcurrentlyContext context) {
        if (null == consumer) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        List<Message> messageList = MessageConverter.toConsumeMessage(rocketMessageList);
        ConsumeResult result = consumer.consume(messageList);

        return ConsumeResultConverter.toRocketStatus(result);
    }
}
