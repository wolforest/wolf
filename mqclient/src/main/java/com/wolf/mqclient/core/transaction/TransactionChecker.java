package com.wolf.mqclient.core.transaction;

import com.wolf.mqclient.core.message.Message;

/**
 * @author weixing
 * @since 2022/12/7 18:57
 */
/**
 * Used to determine {@link TransactionResult} when {@link Transaction} is not committed or roll-backed in time.
 * {@link Transaction#commit()} and {@link Transaction#rollback()} does not promise that it would be applied
 * successfully, so that checker here is necessary.
 *
 * <p>If {@link TransactionChecker#checkTransaction(Message)} returns {@link TransactionResult#UNKNOWN} or exception
 * raised during the invocation of {@link TransactionChecker#checkTransaction(Message)}, the examination from server will be
 * performed periodically.
 */
public interface TransactionChecker {
    /**
     * Server will solve the suspended transactional message by this method.
     *
     * <p>If exception was thrown in this method, which equals {@link TransactionResult#UNKNOWN} is returned.
     *
     * @param message to determine {@link TransactionResult}.
     * @return the transaction resolution.
     */
    TransactionResult checkTransaction(Message message);
}
