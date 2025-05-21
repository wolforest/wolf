package cn.coderule.mqclient.core.transaction;

import cn.coderule.common.util.lang.string.StringUtil;
import cn.coderule.mqclient.config.MQDefaultConst;
import cn.coderule.mqclient.core.transaction.exception.TransactionListenerOverrideException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author weixing
 * @since 2022/12/12 16:17
 */
public class TransactionCheckerRegistry {
    private static final Map<String /* topic */, Map<String /* checkerKey */, TransactionChecker>> transactionCheckerMap = new ConcurrentHashMap<>();

    private TransactionCheckerRegistry() {}

    public static void register(String topic, String tag, TransactionChecker checker) {
        if (!transactionCheckerMap.containsKey(topic)) {
            transactionCheckerMap.put(topic, new ConcurrentHashMap<>());
        }

        if (transactionCheckerMap.get(topic).containsKey(tag)) {
            throw new TransactionListenerOverrideException("Can not override transaction checker [topic=" + topic + " tag=" + tag + "]");
        }
        String checkerKey = getCheckerKey(tag);
        transactionCheckerMap.get(topic).put(checkerKey, checker);
    }

    public static TransactionChecker get(String topic, String tag) {
        Map<String, TransactionChecker> topicCheckerMap = get(topic);
        if (null == topicCheckerMap) {
            return null;
        }

        return topicCheckerMap.get(getCheckerKey(tag));
    }

    public static Map<String, TransactionChecker> get(String topic) {
        return transactionCheckerMap.get(topic);
    }

    public static Map<String, Map<String, TransactionChecker>> get() {
        return transactionCheckerMap;
    }

    private static String getCheckerKey(String tag) {
        return StringUtil.isBlank(tag) ? MQDefaultConst.DEFAULT_TAG : tag;
    }
}
