package com.wolf.mqclient.vendor.rocketmq.transaction;

import com.wolf.mqclient.config.MQTransactionProducerConfig;
import com.wolf.mqclient.core.transaction.TransactionChecker;
import com.wolf.mqclient.core.transaction.TransactionCheckerRegistry;
import com.wolf.mqclient.core.transaction.TransactionProducer;
import com.wolf.mqclient.core.transaction.TransactionResult;
import com.wolf.mqclient.core.transaction.exception.TransactionListenerNotFoundException;
import com.wolf.mqclient.vendor.rocketmq.converter.MessageConverter;
import com.wolf.mqclient.vendor.rocketmq.converter.result.TransactionResultConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * com.wolf.mqclient.adapter.rocketmq.producer
 *
 * @author Wingle
 * @since 2021/12/15 下午11:44
 **/
@Slf4j
public class RocketTransactionListener implements TransactionListener {

    private final MQTransactionProducerConfig config;

    public RocketTransactionListener(MQTransactionProducerConfig config) {
        this.config = config;
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message rocketMessage, Object arg) {

        TransactionChecker transactionChecker = getTransactionChecker(rocketMessage);
        if (null == transactionChecker) {
            throw new TransactionListenerNotFoundException(rocketMessage.getTopic(), rocketMessage.getTags());
        }

        if (!(transactionChecker instanceof TransactionProducer)) {
            throw new UnsupportedOperationException("Rocket MQ Transaction callback mode not enabled!");
        }

        com.wolf.mqclient.core.message.Message message = MessageConverter.toTransactionMessage(rocketMessage);
        TransactionResult transactionResult = ((TransactionProducer) transactionChecker).execute(message, arg);

        return TransactionResultConverter.toTransactionState(transactionResult);
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt rocketMessage) {
        com.wolf.mqclient.core.message.Message message = MessageConverter.toTransactionMessage(rocketMessage);

        TransactionChecker transactionChecker = getTransactionChecker(rocketMessage);
        if (null == transactionChecker) {
            log.error("TransactionChecker not found. msgId={} msgKeys={} topic={} tag={}", rocketMessage.getMsgId(), rocketMessage.getKeys(), rocketMessage.getTopic(), rocketMessage.getTags());
            return TransactionResultConverter.toTransactionState(TransactionResult.COMMIT());
        }

        TransactionResult transactionResult = null;
        try {
            transactionResult = transactionChecker.checkTransaction(message);
        } catch (Exception e) {
            transactionResult = TransactionResult.UNKNOWN();
            log.error("errors occurred while check local transaction state. msgId={} msgKeys={}  topic={} tag={} checker={}",
                rocketMessage.getMsgId(), rocketMessage.getKeys(), rocketMessage.getTopic(), rocketMessage.getTags(),
                transactionChecker.getClass().getSimpleName(), e);
        }

        log.info("check local transaction result={} msgId={} msgKeys={} topic={} tag={}", transactionResult.getState().name(), rocketMessage.getKeys(), rocketMessage.getMsgId(), rocketMessage.getTopic(), rocketMessage.getTags());
        return TransactionResultConverter.toTransactionState(transactionResult);
    }

    private TransactionChecker getTransactionChecker(Message rocketMessage) {
        return TransactionCheckerRegistry.get(rocketMessage.getTopic(), rocketMessage.getTags());
    }
}
