package com.wolf.mqclient.vendor.rocketmq.converter.state;

import com.wolf.common.convention.worker.Converter;
import com.wolf.mqclient.core.transaction.TransactionStateEnum;
import org.apache.rocketmq.client.producer.LocalTransactionState;

/**
 * com.wolf.mqclient.adapter.rocketmq.converter.state
 *
 * @author Wingle
 * @since 2021/12/16 上午12:10
 **/
public class TransactionStateConverter implements Converter {
    public static LocalTransactionState toRocket(TransactionStateEnum stateEnum) {
        if (null == stateEnum) {
            return LocalTransactionState.UNKNOW;
        }

        switch (stateEnum) {
            case COMMIT:
                return LocalTransactionState.COMMIT_MESSAGE;
            case ROLLBACK:
                return LocalTransactionState.ROLLBACK_MESSAGE;
            case TIMEOUT:
            case RETRY:
            default:
                return LocalTransactionState.UNKNOW;
        }

    }
}
