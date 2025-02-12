package cn.coderule.mqclient.vendor.rocketmq5.producer;

import cn.coderule.common.lang.exception.SystemException;
import cn.coderule.common.util.lang.StringUtil;
import cn.coderule.common.util.lang.time.DateUtil;
import cn.coderule.mqclient.config.MQProducerConfig;
import cn.coderule.mqclient.core.MQVendor;
import cn.coderule.mqclient.core.message.MessageSendTypeEnum;
import cn.coderule.mqclient.core.producer.ProduceCallback;
import cn.coderule.mqclient.core.producer.ProduceRequest;
import cn.coderule.mqclient.core.producer.ProduceResult;
import cn.coderule.mqclient.core.producer.VendorProducer;
import cn.coderule.mqclient.core.producer.exception.ProducerSendFailedException;
import cn.coderule.mqclient.core.producer.exception.ProducerStartFailedException;
import cn.coderule.mqclient.vendor.rocketmq5.Rocket5ClientManager;
import cn.coderule.mqclient.vendor.rocketmq5.Rocket5Factory;
import cn.coderule.mqclient.vendor.rocketmq5.converter.MessageConverter;
import cn.coderule.mqclient.vendor.rocketmq5.converter.result.ProduceResultConverter;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
@Slf4j
public class Rocket5Producer implements VendorProducer {
    //@Getter
    private final MQProducerConfig config;

    //@Getter
    private Producer producer;

    private static final AtomicInteger PRODUCER_ID_SEQ = new AtomicInteger(1);

    private String instanceName;

    public Rocket5Producer(MQProducerConfig config) {
        this.config = config;
    }

    @Override
    public String getVendorType() {
        return MQVendor.VENDOR_ROCKET_MQ;
    }

    @Override
    public String getVendorInstanceName() {
        return instanceName;
    }

    @Override
    public void start() {
        try {
            this.producer = Rocket5Factory.createProducer(config);
            this.instanceName = StringUtil.joinWith(
                "-", "producer",
                config.getVendorConfig().getVendorId(), PRODUCER_ID_SEQ.getAndIncrement()
            );
        } catch (ClientException e) {
            log.error("[MQ] rocketmq5 producer start failed: ", e);
            throw new ProducerStartFailedException("rocketmq5 producer start failed");
        }
    }

    @Override
    public void shutdown() {
        log.info("[MQ] rocketmq5 producer is going to close. InstanceName={}", getVendorInstanceName());
        try {
            producer.close();
        } catch (Exception e) {
            log.error("[MQ] errors occurred while close rocketmq5 producer.", e);
        }
    }

    @Override
    public ProduceResult send(ProduceRequest produceRequest) {
        MessageSendTypeEnum sendType = produceRequest.getSendType();
        if (null == sendType) {
            return syncSend(produceRequest);
        }

        switch (sendType) {
            case ONE_WAY:
                return oneWaySend(produceRequest);
            case SCHEDULE:
                return scheduledSend(produceRequest);
            case ORDER:
                return orderedSend(produceRequest);
            default:
                return syncSend(produceRequest);
        }
    }

    @Override
    public void send(ProduceRequest produceRequest, ProduceCallback callback) {
        asyncSend(produceRequest, callback);
    }

    public ProduceResult syncSend(ProduceRequest produceRequest) {
        Message message = MessageConverter.toRocketMessage(produceRequest);

        try {
            SendReceipt sendReceipt = producer.send(message);
            return ProduceResultConverter.success(sendReceipt);
        } catch (Exception e) {
            log.error("[MQ] rocketmq5 send message failed. cause: {}", e.getMessage(), e);
            throw new ProducerSendFailedException("rocketmq5 send message failed:" + e.getMessage());
        }
    }

    public void asyncSend(ProduceRequest produceRequest, ProduceCallback callback) {
        Message message = MessageConverter.toRocketMessage(produceRequest);

        final CompletableFuture<SendReceipt> future = producer.sendAsync(message);
        ExecutorService sendCallbackExecutor = Rocket5ClientManager.getInstance().getSendCallbackExecutor();

        future.whenCompleteAsync((sendReceipt, t) -> {
            if (null != t) {
                log.error("[MQ] rocketmq5 async send message failed. cause: {}", t.getMessage(), t);
                callback.onComplete(ProduceResult.FAILURE(), t);
                return;
            }

            log.info("[MQ] rocketmq5 async send message successfully, messageId={}", sendReceipt.getMessageId());

            ProduceResult produceResult = ProduceResult.SUCCESS();
            produceResult.setMessageId(sendReceipt.getMessageId().toString());
            callback.onComplete(produceResult, null);

        }, sendCallbackExecutor);
    }

    public ProduceResult oneWaySend(ProduceRequest produceRequest) {
        Message message = MessageConverter.toRocketMessage(produceRequest);
        producer.sendAsync(message);
        return ProduceResult.SUCCESS();
    }

    public ProduceResult orderedSend(ProduceRequest produceRequest) {
        String messageGroup = String.valueOf(produceRequest.getOrderBy());
        if (null == messageGroup) {
            return send(produceRequest);
        }

        Message message = MessageConverter.toOrderedRocketMessage(produceRequest, messageGroup);

        try {
            SendReceipt sendReceipt = producer.send(message);
            return ProduceResultConverter.success(sendReceipt);
        } catch (Exception e) {
            log.error("[MQ] rocketmq5 ordered send message failed. cause: {}", e.getMessage(), e);
            throw new ProducerSendFailedException("rocketmq5 send ordered message failed:" + e.getMessage());
        }
    }

    public ProduceResult scheduledSend(ProduceRequest produceRequest) {
        if (null == produceRequest.getActiveAt() && null == produceRequest.getDelay()) {
            throw new SystemException("activeAt and delay should be both not null.");
        }

        Long delayTimestamp = getDelayTimestamp(produceRequest);
        Message message = MessageConverter.toDelayRocketMessage(produceRequest, delayTimestamp);

        try {
            SendReceipt sendReceipt = producer.send(message);
            return ProduceResultConverter.success(sendReceipt);
        } catch (Exception e) {
            log.error("[MQ] rocketmq5 scheduled send message failed. cause: {} produceRequest={}", e.getMessage(), produceRequest, e);
            throw new ProducerSendFailedException("rocketmq5 send scheduled message failed:" + e.getMessage());
        }
    }

    private Long getDelayTimestamp(ProduceRequest produceRequest) {
        Long ts;

        LocalDateTime activeAt = produceRequest.getActiveAt();
        if (null != activeAt) {
            ts = DateUtil.asEpochMilli(activeAt);
            return ts;
        }

        Long delay = produceRequest.getDelay();
        if (null != delay) {
            ts = System.currentTimeMillis() + delay;
            return ts;
        }

        return System.currentTimeMillis();
    }

}
