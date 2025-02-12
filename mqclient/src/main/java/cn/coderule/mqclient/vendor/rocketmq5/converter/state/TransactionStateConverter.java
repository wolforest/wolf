package cn.coderule.mqclient.vendor.rocketmq5.converter.state;

import cn.coderule.common.convention.worker.Converter;
import cn.coderule.mqclient.core.transaction.TransactionStateEnum;
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
