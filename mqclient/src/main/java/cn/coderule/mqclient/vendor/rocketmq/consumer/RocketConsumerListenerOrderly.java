package cn.coderule.mqclient.vendor.rocketmq.consumer;

import cn.coderule.mqclient.core.consumer.ConsumeResult;
import cn.coderule.mqclient.core.consumer.Consumer;
import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.vendor.rocketmq.converter.MessageConverter;
import cn.coderule.mqclient.vendor.rocketmq.converter.result.ConsumeResultConverter;
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
