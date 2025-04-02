package com.wolf.mqclient.core.consumer;

import com.wolf.common.util.collection.CollectionUtil;
import com.wolf.mqclient.core.message.Message;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractConsumer implements Consumer {

    @Override
    public ConsumeResult consume(List<Message> messageList) {
        if (CollectionUtil.isEmpty(messageList)) {
            return ConsumeResult.SUCCESS();
        }

        if (messageList.size() == 1) {
            return consumeWithExceptionHandle(messageList.get(0));
        }

        boolean hasFailure = false;
        for (Message message : messageList) {
            ConsumeResult result = consumeWithExceptionHandle(message);
            if (!ConsumeStateEnum.SUCCESS.equals(result.getState())) {
                hasFailure = true;
            }
        }

        if (hasFailure && !ignoreConsumeFailure()) {
            return ConsumeResult.FAILURE();
        }

        return ConsumeResult.SUCCESS();
    }

    protected boolean ignoreConsumeFailure() {
        return false;
    }

    protected ConsumeResult consumeWithExceptionHandle(Message message) {
        ConsumeResult result;
        try {
            result = consume(message);
        } catch (Exception e) {
            log.error("errors occurred while consume message. msgKeys={}, msgId={}, topic={}, tags={}, retryTimes={}, consumer={}",
                message.getId(),
                message.getMessageContext().getMqId(),
                message.getTopic(),
                message.getTags(),
                message.getMessageContext().getReconsumeTimes(),
                this.getClass().getSimpleName(),
                e
            );

            result = ignoreConsumeFailure() ? ConsumeResult.SUCCESS() : ConsumeResult.FAILURE();
        }

        return result;
    }
}
