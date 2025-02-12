package cn.coderule.mqclient.vendor.rocketmq5.converter.result;

import cn.coderule.common.convention.worker.Converter;
import cn.coderule.mqclient.core.transaction.TransactionResult;
import cn.coderule.mqclient.vendor.rocketmq5.converter.state.TransactionStateConverter;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
public class TransactionResultConverter implements Converter {

    public static TransactionResolution toTransactionState(TransactionResult result) {
        if (result == null) {
            return TransactionResolution.UNKNOWN;
        }

        return TransactionStateConverter.toRocket(result.getState());
    }
}
