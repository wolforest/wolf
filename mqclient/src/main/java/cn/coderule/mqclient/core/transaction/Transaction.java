package cn.coderule.mqclient.core.transaction;

import cn.coderule.mqclient.core.message.Message;

/**
 * @author weixing
 * @since 2022/12/7 18:57
 */
public interface Transaction {
    TransactionResult commit();
    TransactionResult rollback();
    Message getMessage();
    void setMessage(Message message);
}
