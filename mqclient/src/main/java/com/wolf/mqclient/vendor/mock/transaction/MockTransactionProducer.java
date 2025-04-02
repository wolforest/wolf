package com.wolf.mqclient.vendor.mock.transaction;

import com.wolf.common.util.lang.StringUtil;
import com.wolf.mqclient.config.MQTransactionProducerConfig;
import com.wolf.mqclient.core.MQVendor;
import com.wolf.mqclient.core.message.Message;
import com.wolf.mqclient.core.producer.ProduceResult;
import com.wolf.mqclient.core.producer.ProduceStateEnum;
import com.wolf.mqclient.core.producer.exception.ProducerStartFailedException;
import com.wolf.mqclient.core.transaction.Transaction;
import com.wolf.mqclient.core.transaction.TransactionRequest;
import com.wolf.mqclient.core.transaction.VendorTransactionProducer;
import com.wolf.mqclient.core.transaction.exception.TransactionProducerSendFailedException;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
@Slf4j
public class MockTransactionProducer implements VendorTransactionProducer {

    private final MQTransactionProducerConfig config;

    private static final AtomicInteger TRANSACTION_PRODUCER_ID_SEQ = new AtomicInteger(1);

    private String instanceName;

    public MockTransactionProducer(MQTransactionProducerConfig config) {
        this.config = config;
    }

    @Override
    public String getVendorType() {
        return MQVendor.VENDOR_ROCKET_MQ_5;
    }

    @Override
    public String getVendorInstanceName() {
        return instanceName;
    }

    @Override
    public void start() {
        try {
            this.instanceName = StringUtil.joinWith("-", "trxn-producer", config.getVendorConfig().getVendorId(), TRANSACTION_PRODUCER_ID_SEQ.getAndIncrement());
        } catch (Exception e) {
            log.error("[MQ] mockmq transaction producer start failed: ", e);
            throw new ProducerStartFailedException("mockmq transaction producer start failed");
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public Transaction beginTransaction(Message message) {
        MockTransaction trx = new MockTransaction();
        trx.setMessage(message);

        return trx;
    }

    @Override
    public ProduceResult send(Transaction transaction) {
        MockTransaction trx = (MockTransaction) transaction;
        Message message = trx.getMessage();

        try {
            String messageId = StringUtil.uuid();
            trx.getMessage().getMessageContext().setMqId(messageId);
            log.info("[MQ] mockmq send transaction message succeeded. msgKeys={} msgId={}", message.getId(), messageId);
            return ProduceResult.builder()
                .state(ProduceStateEnum.SUCCESS)
                .messageId(messageId)
                .build();
        } catch (Exception e) {
            log.error("[MQ] mockmq send transaction message failed. msgKeys={}", message.getId(), e);
            throw new TransactionProducerSendFailedException("mockmq send transaction message failed");
        }
    }

    @Override
    public ProduceResult send(TransactionRequest transactionRequest) {
        throw new UnsupportedOperationException("Transaction Request send directly is not supported by mockmq");
    }
}
