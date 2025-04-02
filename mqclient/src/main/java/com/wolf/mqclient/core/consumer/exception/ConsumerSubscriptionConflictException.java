package com.wolf.mqclient.core.consumer.exception;

import com.wolf.common.lang.exception.SystemException;
import com.wolf.common.util.collection.CollectionUtil;
import com.wolf.common.util.lang.StringUtil;
import java.util.Arrays;

/**
 * @author weixing
 * @since 2022/12/13 18:26
 */
public class ConsumerSubscriptionConflictException extends SystemException {
    public ConsumerSubscriptionConflictException(String message) {
        super(110, message);
    }

    public ConsumerSubscriptionConflictException(String group, String[] topics, String[] tags) {
        super(
            StringUtil.format("Consumer subscription conflict [group=%s topic=%s tag=%s]",
                group,
                CollectionUtil.join("|", Arrays.asList(topics)),
                CollectionUtil.join("|", Arrays.asList(tags))
            )
        );
    }

    public ConsumerSubscriptionConflictException(long code, String message) {
        super(code, message);
    }
}
