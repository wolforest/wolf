package cn.coderule.mqclient.core.consumer;

import cn.coderule.mqclient.core.message.Message;
import java.util.List;

/**
 * com.wolf.mqclient.consumer
 *
 * @author Wingle
 * @since 2021/12/16 下午8:09
 **/
public interface Consumer {
    ConsumeResult consume(List<Message> messageList);

    ConsumeResult consume(Message message);
}
