package com.wolf.mqclient.vendor.rocketmq.converter.result;

import com.wolf.common.convention.worker.Converter;
import com.wolf.mqclient.core.producer.ProduceResult;
import com.wolf.mqclient.core.producer.ProduceStateEnum;
import com.wolf.mqclient.vendor.rocketmq.converter.state.ProduceStateConverter;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * com.wolf.mqclient.adapter.rocketmq.converter
 *
 * @author Wingle
 * @since 2021/12/14 下午5:23
 **/
public class ProduceResultConverter implements Converter {
    public static ProduceResult from(SendResult result) {
        return ProduceResult.builder()
                .state(ProduceStateConverter.fromRocket(result.getSendStatus()))
                .messageId(result.getMsgId())
                .offset(result.getQueueOffset())
                .build();
    }

    public static ProduceResult from(RecordMetadata metadata, String key) {
        return ProduceResult.builder()
                .state(ProduceStateEnum.SUCCESS)
                .messageId(key)
                .offset(metadata.offset())
                .build();
    }
}
