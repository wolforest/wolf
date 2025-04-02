package cn.coderule.mqclient.core.transaction;

import cn.coderule.mqclient.core.MQVendor;
import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.core.producer.ProduceResult;

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
