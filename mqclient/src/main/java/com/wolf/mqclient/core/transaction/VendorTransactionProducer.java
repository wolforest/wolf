package com.wolf.mqclient.core.transaction;

import com.wolf.mqclient.core.MQVendor;
import com.wolf.mqclient.core.message.Message;
import com.wolf.mqclient.core.producer.ProduceResult;

/**
 * com.wolf.mqclient.producer
 *
 * @author Wingle
 * @since 2021/12/13 下午9:30
 **/
public interface VendorTransactionProducer extends MQVendor {
    void start();

    void shutdown();

    Transaction beginTransaction(Message message);

    ProduceResult send(Transaction transaction);

    ProduceResult send(TransactionRequest transactionRequest);
}
