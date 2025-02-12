package cn.coderule.mqclient.vendor.rocketmq5.converter.state;

import cn.coderule.common.convention.worker.Converter;
import cn.coderule.mqclient.core.consumer.ConsumeStateEnum;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
public class ConsumeStateConverter implements Converter {
    public static ConsumeResult toRocket5(ConsumeStateEnum stateEnum) {
        if (stateEnum == null) {
            return ConsumeResult.FAILURE;
        }

        if (stateEnum == ConsumeStateEnum.SUCCESS) {
            return ConsumeResult.SUCCESS;
        }

        return ConsumeResult.FAILURE;
    }
}
