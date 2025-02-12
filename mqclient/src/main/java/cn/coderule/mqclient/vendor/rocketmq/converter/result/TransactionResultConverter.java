package cn.coderule.mqclient.vendor.rocketmq.converter.result;

import cn.coderule.common.convention.worker.Converter;
import cn.coderule.mqclient.core.transaction.TransactionResult;
import cn.coderule.mqclient.vendor.rocketmq.converter.state.TransactionStateConverter;
import org.apache.rocketmq.client.producer.LocalTransactionState;

/**
 * com.wolf.mqclient.adapter.rocketmq.converter
 *
 * @author Wingle
 * @since 2021/12/16 上午12:10
 **/
public class TransactionResultConverter implements Converter {

    public static LocalTransactionState toTransactionState(TransactionResult result) {
        if (result == null) {
            return LocalTransactionState.UNKNOW;
        }

        return TransactionStateConverter.toRocket(result.getState());
    }
}
