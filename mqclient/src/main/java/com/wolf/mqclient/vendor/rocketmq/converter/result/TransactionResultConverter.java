package com.wolf.mqclient.vendor.rocketmq.converter.result;

import com.wolf.common.convention.worker.Converter;
import com.wolf.mqclient.core.transaction.TransactionResult;
import com.wolf.mqclient.vendor.rocketmq.converter.state.TransactionStateConverter;
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
