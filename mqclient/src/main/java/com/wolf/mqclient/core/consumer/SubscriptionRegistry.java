package com.wolf.mqclient.core.consumer;

import com.wolf.common.util.collection.MapUtil;
import com.wolf.common.util.collection.SetUtil;
import com.wolf.common.util.lang.StringUtil;
import com.wolf.mqclient.config.MQConsumerConfig;
import com.wolf.mqclient.config.exception.MQConfigException;
import com.wolf.mqclient.core.consumer.exception.ConsumerSubscriptionConflictException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weixing
 * @since 2022/12/13 18:12
 */
@Slf4j
public class SubscriptionRegistry {
    // group@topic@tag
    public static final String PROXIED_CONSUMER_REGISTRY_KEY_PATTERN = "%s@%s@%s";
    private static final Map<String/*group*/, Subscription> subscriptionMap = new HashMap<>();
    @Getter
    private static final Map<String/*group*/, Map<String/*group@topic@tag*/, List<Consumer>>> proxiedConsumerMap = new HashMap<>();
    @Getter
    private static final Map<String/*group*/, Map<String/*topic*/, Set<String/*tag*/>>> proxiedSubscriptionMap = new HashMap<>();

    private SubscriptionRegistry() {}

    public static void register(String group, String topic, String tag) {
        String t = null != tag ? tag : "*";
        register(group, new String[]{topic}, new String[]{t});
    }

    public static void register(String group, String[] topics, String[] tags) {
        if (!subscriptionMap.containsKey(group)) {
            Subscription subscription = new Subscription();
            subscription.setGroup(group);
            subscription.setTopics(topics);
            subscription.setTags(tags);
            subscriptionMap.put(group, subscription);
            return;
        }

        Subscription subscription = get(group);

        if (subscription.getTopics().length > 1) {
            Arrays.sort(subscription.getTopics());
        }
        if (topics.length > 1) {
            Arrays.sort(topics);
        }

        if (subscription.getTags().length > 1) {
            Arrays.sort(subscription.getTags());
        }
        if (tags.length > 1) {
            Arrays.sort(tags);
        }

        if (!Arrays.equals(subscription.getTopics(), topics) || !Arrays.equals(subscription.getTags(), tags)) {
            log.error("[MQ] Consumer subscription conflict. registered=[group={} topics={} tags={}] new=[group={} topics={} tags={}]", group, subscription.topics, subscription.tags, group, topics, tags);
            throw new ConsumerSubscriptionConflictException(group, topics, tags);
        }
    }

    public static void registerForProxyMode(MQConsumerConfig config) {
        String group = config.getGroup();

        Map<String, List<Consumer>> consumerMap;
        if (!proxiedConsumerMap.containsKey(group)) {
            consumerMap = new HashMap<>();
            proxiedConsumerMap.put(group, consumerMap);
        } else {
            consumerMap = proxiedConsumerMap.get(group);
        }
        registerProxiedConsumer(consumerMap, config);

        Map<String, Set<String>> topicToTagsMap;
        if (!proxiedSubscriptionMap.containsKey(group)) {
            topicToTagsMap = new HashMap<>();
            proxiedSubscriptionMap.put(group, topicToTagsMap);
        } else {
            topicToTagsMap = proxiedSubscriptionMap.get(group);
        }
        registerProxiedSubscription(topicToTagsMap, config);
    }

    private static void registerProxiedConsumer(Map<String, List<Consumer>> consumerMap, MQConsumerConfig config) {
        Consumer consumer = config.getConsumer();
        String group = config.getGroup();
        List<String> topics = config.getTopics();
        Map<String, Set<String>> topicTags = config.getTopicTags();

        for (String topic : topics) {
            Set<String> tags = topicTags.get(topic);
            if (SetUtil.isEmpty(tags)) {
                throw new MQConfigException("Consumer subscribe empty tag. consumer=" + consumer.getClass().getSimpleName());
            }
            tags.forEach(tag -> {
                String key = StringUtil.format(PROXIED_CONSUMER_REGISTRY_KEY_PATTERN, group, topic, tag);
                if (!consumerMap.containsKey(key)) {
                    List<Consumer> consumerList = new ArrayList<>();
                    consumerList.add(consumer);
                    consumerMap.put(key, consumerList);
                } else {
                    List<Consumer> consumerList = consumerMap.get(key);
                    if (!consumerList.contains(consumer)) {
                        consumerList.add(consumer);
                    }
                }
            });
        }
    }

    private static void registerProxiedSubscription(Map<String, Set<String>> topicToTagsMap, MQConsumerConfig config) {
        List<String> topics = config.getTopics();
        Map<String, Set<String>> topicTags = config.getTopicTags();

        for (String topic : topics) {
            Set<String> tags = topicTags.get(topic);
            if (!topicToTagsMap.containsKey(topic)) {
                topicToTagsMap.put(topic, tags);
            } else {
                topicToTagsMap.get(topic).addAll(tags);
            }
        }
    }

    public static Subscription get(String group) {
        return subscriptionMap.get(group);
    }

    public static List<Consumer> getProxiedConsumer(String group, String topic, String tag) {
        Map<String, List<Consumer>> consumerMap = proxiedConsumerMap.get(group);
        if (MapUtil.isEmpty(consumerMap)) {
            return null;
        }

        String key = StringUtil.format(PROXIED_CONSUMER_REGISTRY_KEY_PATTERN, group, topic, tag);
        if (consumerMap.containsKey(key)) {
            return consumerMap.get(key);
        }

        key = StringUtil.format(PROXIED_CONSUMER_REGISTRY_KEY_PATTERN, group, topic, "*");
        if (consumerMap.containsKey(key)) {
            return consumerMap.get(key);
        }

        return null;
    }

    @Data
    private static class Subscription {
        private String group;
        private String[] topics;
        private String[] tags;
    }
}
