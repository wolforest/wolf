package cn.coderule.mqclient.config;

import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.collection.SetUtil;
import cn.coderule.common.util.lang.string.StringUtil;
import cn.coderule.mqclient.MQTemplate;
import cn.coderule.mqclient.config.exception.MQConfigException;
import cn.coderule.mqclient.core.consumer.Consumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author weixing
 * @since 2022/10/11 17:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MQConsumerConfig {

    private String name;

    private MQVendorConfig vendorConfig;

    private MQConsumerGroupConfig groupConfig;

    private String group;

    //private boolean enabled;

    private List<String> topics;

    private Map<String/*topic*/, Set<String/*tag*/>> topicTags;

    private CheckPoint checkPoint;

    private Class<?> messageClass;

    private Consumer consumer;

    public MQConsumerConfig(Builder builder) {
        name = builder.consumer.getClass().getSimpleName();
        vendorConfig = builder.vendorConfig;
        groupConfig = builder.groupConfig;
        group = builder.group;
        topics = builder.topics;
        topicTags = builder.topicTags;
        checkPoint = builder.checkPoint;
        consumer = builder.consumer;
    }

    @lombok.Builder
    @Getter
    public static class CheckPoint {
        private Long timestamp;
        private boolean force;
    }

    public static class Builder {
        private final MQTemplate template;

        private MQVendorConfig vendorConfig;
        private MQConsumerGroupConfig groupConfig;
        private String group;
        private List<String> topics;
        private Map<String, Set<String>> topicTags;
        private CheckPoint checkPoint;
        private Class<?> messageClass;
        private Consumer consumer;

        @Deprecated
        private int minThreadNum;
        @Deprecated
        private int maxThreadNum;

        public Builder(MQTemplate template) {
            this.template = template;
            this.topics = new ArrayList<>();
            this.topicTags = new HashMap<>();
        }

        public Builder vendor(MQVendorConfig config) {
            this.vendorConfig = config;
            return this;
        }

        public Builder consumer(Consumer consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder groupConfig(MQConsumerGroupConfig groupConfig) {
            this.group = groupConfig.getGroup();
            this.groupConfig = groupConfig;
            return this;
        }

        public Builder topic(String topic) {
            if (StringUtil.isBlank(topic)) {
                return this;
            }
            if (!this.topics.contains(topic)) {
                this.topics.add(topic);

                this.topicTags.put(topic, new HashSet<>());
            }
            return this;
        }

        public Builder topicWithTags(String topic, Set<String> tags) {
            if (CollectionUtil.isEmpty(tags) || StringUtil.isBlank(topic)) {
                return this;
            }

            tags = SetUtil.filterEmpty(tags);
            if (CollectionUtil.isEmpty(tags)) {
                return this;
            }

            if (!this.topics.contains(topic)) {
                this.topics.add(topic);
            }

            Set<String> curTags = this.topicTags.get(topic);
            if (null == curTags) {
                this.topicTags.put(topic, tags);
            } else {
                curTags.addAll(tags);
            }

            return this;
        }

        public Builder topicWithTags(String topic, String... tags) {
            if (tags.length <= 0 || StringUtil.isBlank(topic)) {
                return this;
            }

            List<String> tagList = new ArrayList<>(Arrays.asList(tags));
            tagList.removeIf(s -> s == null || s.isEmpty());
            if (CollectionUtil.isEmpty(tagList)) {
                return this;
            }

            if (!this.topics.contains(topic)) {
                this.topics.add(topic);
            }

            Set<String> curTags = this.topicTags.get(topic);
            Set<String> addTags = new HashSet<>(tagList);
            if (null == curTags) {
                this.topicTags.put(topic, addTags);
            } else {
                curTags.addAll(addTags);
            }

            return this;
        }

        public Builder tag(String tag) {
            if (StringUtil.isBlank(tag)) {
                return this;
            }

            for (Map.Entry<String, Set<String>> entry : this.topicTags.entrySet()) {
                entry.getValue().add(tag);
            }

            return this;
        }

        public Builder tags(Set<String> tags) {
            if (CollectionUtil.isEmpty(tags)) {
                return this;
            }
            for (Map.Entry<String, Set<String>> entry : this.topicTags.entrySet()) {
                entry.getValue().addAll(tags);
            }
            return this;
        }

        public Builder tags(String... tags) {
            if (tags.length <= 0) {
                return this;
            }
            for (Map.Entry<String, Set<String>> entry : this.topicTags.entrySet()) {
                entry.getValue().addAll(Arrays.asList(tags));
            }
            return this;
        }

        @Deprecated
        public Builder concurrent(int max) {
            return concurrent(0, max);
        }

        @Deprecated
        public Builder concurrent(int min, int max) {
            if (min > max) {
                throw new MQConfigException("consumer min thread num can not greater than max thread num");
            }

            if (min > 0) {
                minThreadNum = min;
            }

            if (max > 0) {
                maxThreadNum = max;
            }

            return this;
        }

        public Builder checkPoint(CheckPoint checkPoint) {
            this.checkPoint = checkPoint;
            return this;
        }

        public void start() {
            MQConsumerConfig consumerConfig = build();
            template.start(consumerConfig);
        }

        private MQConsumerConfig build() {
            checkConfig();

            if (null == groupConfig) {
                // get yml groupConfig
                groupConfig = template.getMqConfig().getConsumerGroupConfig(group);

                if (minThreadNum > 0) {
                    groupConfig.setMinThreadNum(minThreadNum);
                }
                if (maxThreadNum > 0) {
                    groupConfig.setMaxThreadNum(maxThreadNum);
                }
            }

            if (null == vendorConfig) {
                MQVendorConfig vendorConfig = template.getMqConfig().getVendorConfig(topics.get(0));
                if (null == vendorConfig) {
                    throw new MQConfigException("The vendor config was not found. topic: " + topics.get(0));
                }
                this.vendorConfig = vendorConfig;
            }

            return new MQConsumerConfig(this);
        }

        private void checkConfig() {
            // check topic
            if (CollectionUtil.isEmpty(topics)) {
                throw new MQConfigException("The consumer topic is not specified.");
            }

            // check group
            if (null == group) {
                throw new MQConfigException("The consumer group is not specified.");
            }

            if (null == consumer) {
                throw new MQConfigException("The consumer is not specified.");
            }
        }
    }
}
