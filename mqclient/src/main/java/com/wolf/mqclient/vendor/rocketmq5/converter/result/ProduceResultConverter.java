package com.wolf.mqclient.vendor.rocketmq5.converter.result;

import com.wolf.common.convention.worker.Converter;
import com.wolf.mqclient.core.producer.ProduceResult;
import com.wolf.mqclient.core.producer.ProduceStateEnum;
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
