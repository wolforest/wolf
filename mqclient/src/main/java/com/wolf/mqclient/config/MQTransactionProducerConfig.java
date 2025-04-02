package com.wolf.mqclient.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weixing
 * @since 2022/10/11 17:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MQTransactionProducerConfig extends MQProducerConfig {
    private String topic;

    private int minCheckThreadNum = 1;
    private int maxCheckThreadNum = 1;
    private int maxRequestHold = 2000;
}
