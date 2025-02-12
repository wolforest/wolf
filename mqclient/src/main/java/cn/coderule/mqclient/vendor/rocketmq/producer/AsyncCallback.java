package cn.coderule.mqclient.vendor.rocketmq.producer;

import cn.coderule.mqclient.core.producer.ProduceCallback;
import cn.coderule.mqclient.core.producer.ProduceResult;
import cn.coderule.mqclient.vendor.rocketmq.converter.result.ProduceResultConverter;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * com.wolf.mqclient.adapter.rocketmq.producer
 *
 * @author Wingle
 * @since 2021/12/16 下午12:49
 **/
public class AsyncCallback implements SendCallback {
    private final ProduceCallback callback;

    public AsyncCallback(ProduceCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSuccess(SendResult sendResult) {
        ProduceResult produceResult = ProduceResultConverter.from(sendResult);
        callback.onComplete(produceResult, null);
    }

    @Override
    public void onException(Throwable e) {
        callback.onComplete(ProduceResult.FAILURE(), e);
    }
}
