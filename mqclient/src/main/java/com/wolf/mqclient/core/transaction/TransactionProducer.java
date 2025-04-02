package com.wolf.mqclient.core.transaction;

import com.wolf.mqclient.core.message.Message;
import com.wolf.mqclient.core.producer.ProduceResult;

/**
 * @author weixing
 * @since 2022/12/11 15:25
 */
@Deprecated
public interface TransactionProducer extends TransactionChecker {
    ProduceResult send();

    TransactionResult execute(Message message, Object arg);

    TransactionResult checkTransaction(Message message);
}
