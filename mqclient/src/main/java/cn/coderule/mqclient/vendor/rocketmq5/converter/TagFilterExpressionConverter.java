package cn.coderule.mqclient.vendor.rocketmq5.converter;

import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.string.StringUtil;
import cn.coderule.mqclient.config.MQConsumerConfig;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;

/**
 * @author weixing
 * @since 2023/6/9 16:19
 */
public class TagFilterExpressionConverter {

    public static FilterExpression to(MQConsumerConfig config, String topic) {
        Set<String> tags = config.getTopicTags().get(topic);
        return to(tags, "*");
    }

    private static FilterExpression to(Set<String> tags, String defaultTag) {
        if (CollectionUtil.isEmpty(tags)) {
            return new FilterExpression(defaultTag, FilterExpressionType.TAG);
        }

        String expression = tags.stream()
                .filter(StringUtil::notBlank)
                .collect(Collectors.joining("||"));

        return new FilterExpression(expression, FilterExpressionType.TAG);
    }
}
