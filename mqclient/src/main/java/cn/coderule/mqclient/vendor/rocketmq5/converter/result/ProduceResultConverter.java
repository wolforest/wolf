package cn.coderule.mqclient.vendor.rocketmq5.converter.result;

import cn.coderule.common.convention.worker.Converter;
import cn.coderule.mqclient.core.producer.ProduceResult;
import cn.coderule.mqclient.core.producer.ProduceStateEnum;
import org.apache.rocketmq.client.apis.producer.SendReceipt;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
public class ProduceResultConverter implements Converter {
    public static ProduceResult success(SendReceipt sendReceipt) {
        return ProduceResult.builder()
                .state(ProduceStateEnum.SUCCESS)
                .messageId(sendReceipt.getMessageId().toString())
                .build();
    }
}
