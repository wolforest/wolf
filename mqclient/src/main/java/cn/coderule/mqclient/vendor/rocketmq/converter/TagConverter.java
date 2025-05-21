package cn.coderule.mqclient.vendor.rocketmq.converter;

import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.string.StringUtil;
import cn.coderule.mqclient.config.MQConsumerConfig;
import cn.coderule.mqclient.config.MQDefaultConst;
import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.core.producer.ProduceRequest;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Converter message tags to RocketMQ message tags
 *
 * @author Wingle
 * @since 2021/12/13 下午10:30
 **/
public class TagConverter {

    public static String to(MQConsumerConfig config, String topic) {
        Set<String> tags = config.getTopicTags().get(topic);
        return to(tags, "*");
    }

    public static String to(ProduceRequest produceRequest) {
        return to(produceRequest.getMessage().getTags(), MQDefaultConst.DEFAULT_TAG);
    }

    public static String to(Message message) {
        return to(message.getTags(), MQDefaultConst.DEFAULT_TAG);
    }

    private static String to(Set<String> tags, String defaultTag) {
        if (CollectionUtil.isEmpty(tags)) {
            return defaultTag;
        }

        String expression = tags.stream()
                .filter(StringUtil::notBlank)
                .collect(Collectors.joining(" || "));
        if (StringUtil.notBlank(expression)) {
            return expression;
        }

        return defaultTag;
    }
}
