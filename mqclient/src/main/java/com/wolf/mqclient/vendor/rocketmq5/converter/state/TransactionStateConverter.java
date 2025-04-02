package com.wolf.mqclient.vendor.rocketmq5.converter.state;

import com.wolf.common.convention.worker.Converter;
import com.wolf.mqclient.core.transaction.TransactionStateEnum;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
public class TransactionStateConverter implements Converter {
    public static TransactionResolution toRocket(TransactionStateEnum stateEnum) {
        if (null == stateEnum) {
            return TransactionResolution.UNKNOWN;
        }

        switch (stateEnum) {
            case COMMIT:
                return TransactionResolution.COMMIT;
            case ROLLBACK:
                return TransactionResolution.ROLLBACK;
            case TIMEOUT:
            case RETRY:
            default:
                return TransactionResolution.UNKNOWN;
        }

    }
}
