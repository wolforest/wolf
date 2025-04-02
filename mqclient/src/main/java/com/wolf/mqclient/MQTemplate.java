package com.wolf.mqclient;

import com.wolf.mqclient.config.MQConfig;
import com.wolf.mqclient.config.MQConsumerConfig;
import com.wolf.mqclient.core.consumer.Consumer;
import com.wolf.mqclient.core.consumer.ConsumerManager;
import com.wolf.mqclient.core.producer.ProduceCallback;
import com.wolf.mqclient.core.producer.ProduceRequest;
import com.wolf.mqclient.core.producer.ProduceResult;
import com.wolf.mqclient.core.producer.ProducerManager;
import com.wolf.mqclient.core.producer.VendorProducer;
import com.wolf.mqclient.core.transaction.Transaction;
import com.wolf.mqclient.core.transaction.TransactionProducerManager;
import com.wolf.mqclient.core.transaction.TransactionRequest;
import com.wolf.mqclient.core.transaction.VendorTransactionProducer;
import lombok.Getter;

/**
 * com.wolf.mqclient
 * mq gateway
 *
 * @author Wingle
 * @since 2021/11/30 下午9:45
 **/
public class MQTemplate {
    private static volatile MQTemplate INSTANCE;

    @Getter
    private final MQConfig mqConfig;

    private final ProducerManager producerManager;

    private final TransactionProducerManager transactionProducerManager;

    @Getter
    private final ConsumerManager consumerManager;

    public static MQTemplate init(MQConfig mqConfig, ProducerManager producerManager, TransactionProducerManager transactionProducerManager, ConsumerManager consumerManager) {
        if (INSTANCE == null) {
            synchronized (MQTemplate.class) {
                if (null == INSTANCE) {
                    INSTANCE = new MQTemplate(mqConfig, producerManager, transactionProducerManager, consumerManager);
                }
            }
        }
        return INSTANCE;
    }

    public static MQTemplate getInstance() {
        return INSTANCE;
    }

    private MQTemplate(MQConfig mqConfig, ProducerManager producerManager, TransactionProducerManager transactionProducerManager, ConsumerManager consumerManager) {
        this.mqConfig = mqConfig;
        this.producerManager = producerManager;
        this.transactionProducerManager = transactionProducerManager;
        this.consumerManager = consumerManager;
    }

    public ProduceRequest.Builder produce() {
        return new ProduceRequest.Builder(this);
    }

    public ProduceResult send(ProduceRequest request) {
        VendorProducer vendorProducer = producerManager.getByUniqueTopic(request.getMessage().getTopic());
        return vendorProducer.send(request);
    }

    public void send(ProduceRequest request, ProduceCallback callback) {
        VendorProducer vendorProducer = producerManager.getByUniqueTopic(request.getMessage().getTopic());
        vendorProducer.send(request, callback);
    }

    /*public void transactionProducer(String topic, TransactionChecker checker) {
        MQTransactionProducerConfig config = new MQTransactionProducerConfig();
    }

    public void start(MQTransactionProducerConfig config) {
        transactionProducerManager.start(config);
    }*/

    public TransactionRequest.Builder transaction() {
        return new TransactionRequest.Builder(this);
    }

    public Transaction begin(TransactionRequest request) {
        VendorTransactionProducer vendorTransactionProducer = transactionProducerManager.get(request.getMessage().getTopic());
        Transaction transaction = vendorTransactionProducer.beginTransaction(request.getMessage());
        vendorTransactionProducer.send(transaction);
        return transaction;
    }

    public ProduceResult send(TransactionRequest request) {
        VendorTransactionProducer vendorTransactionProducer = transactionProducerManager.get(request.getMessage().getTopic());
        return vendorTransactionProducer.send(request);
    }

    public MQConsumerConfig.Builder consumer(Consumer consumer) {
        MQConsumerConfig.Builder builder = new MQConsumerConfig.Builder(this);
        return builder.consumer(consumer);
    }

    public void start(MQConsumerConfig config) {
        consumerManager.start(config);
    }
}
