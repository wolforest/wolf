package cn.coderule.mqclient.vendor.mock.transaction;

import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.core.transaction.Transaction;
import cn.coderule.mqclient.core.transaction.TransactionResult;
import lombok.extern.slf4j.Slf4j;


/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
@Slf4j
public class MockTransaction implements Transaction {

    private Message message;

    public MockTransaction() {
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public TransactionResult commit() {
        try {
            log.info("[MQ] mockmq transaction commit successfully. msgId={} msgKeys={} topic={} tag={}", message.getMessageContext().getMqId(), message.getId(), message.getTopic(), message.getTag());
            return TransactionResult.COMMIT();
        } catch (Exception e) {
            log.error("[MQ] mockmq transaction commit failed. msgId={} msgKeys={}", message.getMessageContext().getMqId(), message.getId(), e);
            return TransactionResult.COMMIT_HAS_EXCEPTION(e);
        }
    }

    @Override
    public TransactionResult rollback() {
        try {
            log.info("[MQ] mockmq transaction rollback successfully. msgId={} msgKeys={} topic={} tag={}", message.getMessageContext().getMqId(), message.getId(), message.getTopic(), message.getTag());
            return TransactionResult.ROLLBACK();
        } catch (Exception e) {
            log.error("[MQ] mockmq transaction rollback failed. msgId={} msgKeys={}", message.getMessageContext().getMqId(), message.getId(), e);
            return TransactionResult.ROLLBACK_HAS_EXCEPTION(e);
        }
    }
}
