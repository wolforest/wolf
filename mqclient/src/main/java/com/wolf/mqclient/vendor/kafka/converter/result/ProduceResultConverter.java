package com.wolf.mqclient.vendor.kafka.converter.result;

import com.wolf.common.convention.worker.Converter;
import com.wolf.mqclient.core.producer.ProduceResult;
import com.wolf.mqclient.core.producer.ProduceStateEnum;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author weixing
 * @since 2023/4/18 10:19
 */
public class ProduceResultConverter implements Converter {
    public static ProduceResult from(RecordMetadata metadata, String key) {
        return ProduceResult.builder()
            .state(ProduceStateEnum.SUCCESS)
            .messageId(key)
            .offset(metadata.offset())
            .build();
    }
}
