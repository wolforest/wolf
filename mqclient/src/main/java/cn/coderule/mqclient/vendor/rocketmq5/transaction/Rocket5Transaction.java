package cn.coderule.mqclient.vendor.rocketmq5.transaction;

import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.core.transaction.Transaction;
import cn.coderule.mqclient.core.transaction.TransactionResult;
import cn.coderule.mqclient.vendor.rocketmq5.converter.MessageConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.producer.SendReceipt;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
@Slf4j
public class Rocket5Transaction implements Transaction {
    @Getter
    private final org.apache.rocketmq.client.apis.producer.Transaction transaction;

    private Message message;
    @Getter
    private org.apache.rocketmq.client.apis.message.Message rocketMessage;

    @Getter
    private SendReceipt sendReceipt;

    public Rocket5Transaction(org.apache.rocketmq.client.apis.producer.Transaction transaction) {
        this.transaction = transaction;
    }

    public void storeSendReceipt(SendReceipt sendReceipt) {
        this.sendReceipt = sendReceipt;
        MessageConverter.mergeRocketMessageProperty(this.message, sendReceipt);
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public void setMessage(Message message) {
        this.message = message;
        this.rocketMessage = MessageConverter.toRocketMessage(message);
    }

    @Override
    public TransactionResult commit() {
        try {
            transaction.commit();
            log.info("[MQ] rocketmq5 transaction commit successfully. msgId={} msgKeys={} topic={} tag={}", sendReceipt.getMessageId(), rocketMessage.getKeys(), rocketMessage.getTopic(), rocketMessage.getTag());
            return TransactionResult.COMMIT();
        } catch (Exception e) {
            log.error("[MQ] rocketmq5 transaction commit failed. msgId={} msgKeys={}", sendReceipt.getMessageId(), rocketMessage.getKeys(), e);
            return TransactionResult.COMMIT_HAS_EXCEPTION(e);
        }
    }

    @Override
    public TransactionResult rollback() {
        try {
            transaction.rollback();
            log.info("[MQ] rocketmq5 transaction rollback successfully. msgId={} msgKeys={} topic={} tag={}", sendReceipt.getMessageId(), rocketMessage.getKeys(), rocketMessage.getTopic(), rocketMessage.getTag());
            return TransactionResult.ROLLBACK();
        } catch (Exception e) {
            log.error("[MQ] rocketmq5 transaction rollback failed. msgId={} msgKeys={}", sendReceipt.getMessageId(), rocketMessage.getKeys(), e);
            return TransactionResult.ROLLBACK_HAS_EXCEPTION(e);
        }
    }
}
