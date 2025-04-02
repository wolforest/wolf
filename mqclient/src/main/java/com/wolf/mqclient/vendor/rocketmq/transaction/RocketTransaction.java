package com.wolf.mqclient.vendor.rocketmq.transaction;

import com.wolf.mqclient.core.transaction.Transaction;
import com.wolf.mqclient.core.transaction.TransactionResult;
import com.wolf.mqclient.vendor.rocketmq.converter.MessageConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @author weixing
 * @since 2022/12/7 18:57
 */
@Slf4j
public class RocketTransaction implements Transaction {
    private final TransactionMQProducerCustomized producer;
    private com.wolf.mqclient.core.message.Message message;
    private Message rocketMessage;
    @Getter
    private SendResult sendResult;

    public RocketTransaction(TransactionMQProducerCustomized producer) {
        this.producer = producer;
    }

    public void storeMessageAndSendResult(Message message, SendResult sendResult) {
        this.rocketMessage = message;
        this.sendResult = sendResult;

        MessageConverter.mergeRocketMessageProperty(this.message, rocketMessage, sendResult);
    }

    @Override
    public com.wolf.mqclient.core.message.Message getMessage() {
        return message;
    }

    @Override
    public void setMessage(com.wolf.mqclient.core.message.Message message) {
        this.message = message;
    }

    @Override
    public TransactionResult commit() {
        try {
            producer.endTransaction(rocketMessage, sendResult, LocalTransactionState.COMMIT_MESSAGE, null);
            log.info("mq transaction commit successfully. msgId={} msgKeys={} topic={} tag={}", sendResult.getMsgId(), rocketMessage.getKeys(), rocketMessage.getTopic(), rocketMessage.getTags());
            return TransactionResult.COMMIT();
        } catch (Exception e) {
            log.warn("local transaction state is " + LocalTransactionState.COMMIT_MESSAGE + ", but end broker transaction failed. msgId={} msgKeys={}", sendResult.getMsgId(), rocketMessage.getKeys(), e);
            return TransactionResult.COMMIT_HAS_EXCEPTION(e);
        }
    }

    @Override
    public TransactionResult rollback() {
        try {
            producer.endTransaction(rocketMessage, sendResult, LocalTransactionState.ROLLBACK_MESSAGE, null);
            log.info("mq transaction rollback successfully. msgId={} msgKeys={} topic={} tag={}", sendResult.getMsgId(), rocketMessage.getKeys(), rocketMessage.getTopic(), rocketMessage.getTags());
            return TransactionResult.ROLLBACK();
        } catch (Exception e) {
            log.warn("local transaction state is " + LocalTransactionState.ROLLBACK_MESSAGE + ", but end broker transaction failed. msgId={} msgKeys={}", sendResult.getMsgId(), rocketMessage.getKeys(), e);
            return TransactionResult.ROLLBACK_HAS_EXCEPTION(e);
        }
    }
}
