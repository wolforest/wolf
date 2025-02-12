package cn.coderule.mqclient.vendor.rocketmq.transaction;

import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.Validators;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageAccessor;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.protocol.NamespaceUtil;
import org.apache.rocketmq.remoting.RPCHook;

/**
 * @author weixing
 * @since 2022/12/7 18:48
 */
@Slf4j
public class TransactionMQProducerCustomized extends TransactionMQProducer {

    public TransactionMQProducerCustomized() {
    }

    public TransactionMQProducerCustomized(final String producerGroup) {
        this(null, producerGroup, null);
    }

    public TransactionMQProducerCustomized(final String namespace, final String producerGroup) {
        this(namespace, producerGroup, null);
    }

    public TransactionMQProducerCustomized(final String producerGroup, RPCHook rpcHook) {
        this(null, producerGroup, rpcHook);
    }

    public TransactionMQProducerCustomized(final String namespace, final String producerGroup, RPCHook rpcHook) {
        super(namespace, producerGroup, rpcHook);
    }

    public TransactionMQProducerCustomized(final String namespace, final String producerGroup, RPCHook rpcHook, boolean enableMsgTrace, final String customizedTraceTopic) {
        super(namespace, producerGroup, rpcHook, enableMsgTrace, customizedTraceTopic);
    }

    public RocketTransaction beginTransaction() {
        return new RocketTransaction(this);
    }

    public TransactionSendResult sendTransactionMessage(Message message, RocketTransaction transaction) throws MQClientException {
        if (null == this.getTransactionListener()) {
            throw new MQClientException("TransactionListener is null", null);
        }

        TransactionSendResult sendResult = this.doSendTransactionMessage(message);
        transaction.storeMessageAndSendResult(message, sendResult);

        return sendResult;
    }

    public void endTransaction(final Message message, final SendResult sendResult, final LocalTransactionState localTransactionState, final Throwable localException) {
        try {
            this.defaultMQProducerImpl.endTransaction(message, sendResult, localTransactionState, null);
        } catch (Exception e) {
            log.error("local transaction execute " + localTransactionState + ", but end broker transaction failed", e);
            //e.printStackTrace();
        }
    }

    public TransactionSendResult doSendTransactionMessage(final Message msg) throws MQClientException {

        msg.setTopic(NamespaceUtil.wrapNamespace(this.getNamespace(), msg.getTopic()));

        // ignore DelayTimeLevel parameter
        if (msg.getDelayTimeLevel() != 0) {
            MessageAccessor.clearProperty(msg, MessageConst.PROPERTY_DELAY_TIME_LEVEL);
        }

        Validators.checkMessage(msg, this.defaultMQProducerImpl.getDefaultMQProducer());

        SendResult sendResult = null;
        MessageAccessor.putProperty(msg, MessageConst.PROPERTY_TRANSACTION_PREPARED, "true");
        MessageAccessor.putProperty(msg, MessageConst.PROPERTY_PRODUCER_GROUP, this.defaultMQProducerImpl.getDefaultMQProducer().getProducerGroup());
        try {
            sendResult = this.send(msg);
        } catch (Exception e) {
            throw new MQClientException("send message Exception", e);
        }

        LocalTransactionState localTransactionState = LocalTransactionState.UNKNOW;
        if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            if (sendResult.getTransactionId() != null) {
                msg.putUserProperty("__transactionId__", sendResult.getTransactionId());
            }
            String transactionId = msg.getProperty(MessageConst.PROPERTY_UNIQ_CLIENT_MESSAGE_ID_KEYIDX);
            if (null != transactionId && !"".equals(transactionId)) {
                msg.setTransactionId(transactionId);
            }
        }

        TransactionSendResult transactionSendResult = new TransactionSendResult();
        transactionSendResult.setSendStatus(sendResult.getSendStatus());
        transactionSendResult.setMessageQueue(sendResult.getMessageQueue());
        transactionSendResult.setMsgId(sendResult.getMsgId());
        transactionSendResult.setQueueOffset(sendResult.getQueueOffset());
        transactionSendResult.setTransactionId(sendResult.getTransactionId());
        transactionSendResult.setOffsetMsgId(sendResult.getOffsetMsgId());
        transactionSendResult.setRegionId(sendResult.getRegionId());
        transactionSendResult.setTraceOn(sendResult.isTraceOn());
        transactionSendResult.setLocalTransactionState(localTransactionState);

        return transactionSendResult;
    }

    public int getCheckThreadPoolMinSize() {
        return super.getCheckThreadPoolMinSize();
    }

    public void setCheckThreadPoolMinSize(int min) {
        super.setCheckThreadPoolMinSize(min);
    }

    public int getCheckThreadPoolMaxSize() {
        return super.getCheckThreadPoolMaxSize();
    }

    public void setCheckThreadPoolMaxSize(int max) {
        super.setCheckThreadPoolMaxSize(max);
    }

    public int getCheckRequestHoldMax() {
        return super.getCheckRequestHoldMax();
    }

    public void setCheckRequestHoldMax(int max) {
        super.setCheckRequestHoldMax(max);
    }

    public ExecutorService getExecutorService() {
        return super.getExecutorService();
    }

    public void setExecutorService(ExecutorService executorService) {
        super.setExecutorService(executorService);
    }

    public TransactionListener getTransactionListener() {
        return super.getTransactionListener();
    }

    public void setTransactionListener(TransactionListener transactionListener) {
        super.setTransactionListener(transactionListener);
    }
}
