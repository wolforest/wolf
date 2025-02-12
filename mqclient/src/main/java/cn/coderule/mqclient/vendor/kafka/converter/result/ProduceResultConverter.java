package cn.coderule.mqclient.vendor.kafka.converter.result;

import cn.coderule.common.convention.worker.Converter;
import cn.coderule.mqclient.core.producer.ProduceResult;
import cn.coderule.mqclient.core.producer.ProduceStateEnum;
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
