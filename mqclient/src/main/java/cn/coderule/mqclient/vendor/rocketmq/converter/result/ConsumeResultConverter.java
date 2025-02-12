package cn.coderule.mqclient.vendor.rocketmq.converter.result;

import cn.coderule.mqclient.core.consumer.ConsumeResult;
import cn.coderule.mqclient.vendor.rocketmq.converter.state.ConsumeStateConverter;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;

/**
 * com.wolf.mqclient.adapter.rocketmq.converter
 *
 * @author Wingle
 * @since 2021/12/16 下午10:17
 **/
public class ConsumeResultConverter {

    public static ConsumeConcurrentlyStatus toRocketStatus(ConsumeResult result) {
        if (null == result || null == result.getState()) {
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        return ConsumeStateConverter.toRocket(result.getState());
    }

    public static ConsumeOrderlyStatus toRocketOrderlyStatus(ConsumeResult result) {
        if (null == result || null == result.getState()) {
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }

        return ConsumeStateConverter.toRocketOrderlyStatus(result.getState());
    }
}
