package cn.coderule.mqclient.vendor.rocketmq5.converter.result;

import cn.coderule.mqclient.core.consumer.ConsumeResult;
import cn.coderule.mqclient.vendor.rocketmq5.converter.state.ConsumeStateConverter;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
public class ConsumeResultConverter {
    public static org.apache.rocketmq.client.apis.consumer.ConsumeResult toRocket5Status(ConsumeResult result) {
        if (null == result || null == result.getState()) {
            return org.apache.rocketmq.client.apis.consumer.ConsumeResult.FAILURE;
        }

        return ConsumeStateConverter.toRocket5(result.getState());
    }
}
