package cn.coderule.mqclient.config;

import cn.coderule.common.lang.constant.Constant;

/**
 * com.wolf.mqclient.domain.constant
 *
 * @author Wingle
 * @since 2021/12/13 下午10:15
 **/
public class MQDefaultConst implements Constant {
    private MQDefaultConst() {}

    public final static String DEFAULT_NAMESPACE = "D_NAMESPACE";
    public final static String DEFAULT_TAG = "D_TAG";

    public final static String DEFAULT_PRODUCER_GROUP = "P_GROUP";
    public final static String DEFAULT_CONSUMER_GROUP = "C_GROUP";
    public final static String DEFAULT_TRANSACTION_GROUP = "T_GROUP";

    public final static int DEFAULT_CONSUMER_THREAD_MIN = 1;
    public final static int DEFAULT_CONSUMER_THREAD_MAX = 4;


    // todolist@mq move below to app config

    public static final String TOPIC_COMMON_DELAY = "COMMON_DELAY";

    public static final String TOPIC_TABLE_SCAN = "TABLE_SCAN";
    public static final String GROUP_TABLE_COUNT = "G_TABLE_COUNT";

    // message property key
    public static final String MESSAGE_PROPERTY_RESERVED_PREFIX = "WOLF_MQ_";
    public static final String MESSAGE_PROPERTY_DELIVERY_TIMESTAMP = "WOLF_MQ_DELIVERY_TIMESTAMP";

    public static final long MAX_ACCEPTABLE_ACTUAL_DELIVERY_DELAY_MILLISECOND = 600000;
}
