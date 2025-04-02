package cn.coderule.mqclient.core.transaction;

import java.util.Map;

/**
 * @author weixing
 * @since 2022/12/7 18:57
 */
public interface TransactionProvider extends TransactionChecker {
    <T> Transaction beginTransaction(T model, Map<String, String> args);
}
