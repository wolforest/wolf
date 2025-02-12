package cn.coderule.mqclient.vendor.mock.transaction;

import cn.coderule.mqclient.config.MQDefaultConst;
import cn.coderule.mqclient.config.MQTransactionProducerConfig;
import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.core.transaction.TransactionCheckerRegistry;
import cn.coderule.mqclient.core.transaction.TransactionResult;
import cn.coderule.mqclient.vendor.rocketmq5.converter.MessageConverter;
import cn.coderule.mqclient.vendor.rocketmq5.converter.result.TransactionResultConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.apis.producer.TransactionChecker;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
@Slf4j
public class MockTransactionChecker implements TransactionChecker {

    /**
     * Not used now, in case need it later
     */
    private final MQTransactionProducerConfig config;

    public MockTransactionChecker(MQTransactionProducerConfig config) {
        this.config = config;
    }

    @Override
    public TransactionResolution check(MessageView messageView) {
        Message message = MessageConverter.toTransactionMessage(messageView);

        cn.coderule.mqclient.core.transaction.TransactionChecker transactionChecker = getTransactionChecker(messageView);
        if (null == transactionChecker) {
            log.error("[MQ] rocketmq5 TransactionChecker not found. msgId={} msgKeys={} topic={} tag={}", messageView.getMessageId(), messageView.getKeys(), messageView.getTopic(), messageView.getTag());
            return TransactionResultConverter.toTransactionState(TransactionResult.COMMIT());
        }

        TransactionResult transactionResult = null;
        try {
            transactionResult = transactionChecker.checkTransaction(message);
        } catch (Exception e) {
            transactionResult = TransactionResult.UNKNOWN();
            log.error("[MQ] rocketmq5 errors occurred while check local transaction state. msgId={} msgKeys={}  topic={} tag={} checker={}",
                messageView.getMessageId(), messageView.getKeys(), messageView.getTopic(), messageView.getTag(),
                transactionChecker.getClass().getSimpleName(), e);
        }

        log.info("[MQ] rocketmq5 check local transaction result={} msgId={} msgKeys={} topic={} tag={}", transactionResult.getState().name(), messageView.getMessageId(), messageView.getKeys(), messageView.getTopic(), messageView.getTag());

        return TransactionResultConverter.toTransactionState(transactionResult);
    }

    private cn.coderule.mqclient.core.transaction.TransactionChecker getTransactionChecker(MessageView messageView) {
        return TransactionCheckerRegistry.get(messageView.getTopic(), messageView.getTag().orElse(MQDefaultConst.DEFAULT_TAG));
    }

}
