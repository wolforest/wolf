package cn.coderule.mqclient.config;

import cn.coderule.common.util.lang.collection.MapUtil;
import cn.coderule.common.util.lang.collection.SetUtil;
import cn.coderule.common.util.lang.StringUtil;
import cn.coderule.mqclient.annotation.MQConsumer;
import cn.coderule.mqclient.config.exception.MQConfigException;
import cn.coderule.mqclient.core.MQVendor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * com.wolf.mqclient
 *
 * @author Wingle
 * @since 2021/11/30 下午9:46
 **/
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "wolf.mq", ignoreInvalidFields = true)
public class MQConfig implements InitializingBean {

    private String defaultVendorId;
    private Map<String/*vendorId*/, MQVendorConfig> vendors;
    private Map<String/*group*/, MQConsumerGroupConfig> consumerGroups;
    private Map<String/*topicName*/, MQTransactionProducerConfig> transactions;

    @Override
    public void afterPropertiesSet() {
        // vendor config
        if (MapUtil.isEmpty(vendors)) {
            return;
        }

        for (Map.Entry<String, MQVendorConfig> entry : vendors.entrySet()) {
            entry.getValue().setVendorId(entry.getKey());
        }

        // producer config

        // transaction config
        if (MapUtil.notEmpty(transactions)) {
            for (Map.Entry<String, MQTransactionProducerConfig> entry : transactions.entrySet()) {
                entry.getValue().setTopic(entry.getKey());
                // use topic as group
                entry.getValue().setGroup(entry.getKey());
            }
        }

        // consumer config
        if (MapUtil.notEmpty(consumerGroups)) {
            for (Map.Entry<String, MQConsumerGroupConfig> entry : consumerGroups.entrySet()) {
                entry.getValue().setGroup(entry.getKey());
            }
        }
    }

    public boolean validate() {
        if (MapUtil.isEmpty(vendors)) {
            return false;
        }

        // not exists any enabled client config
        if (vendors.values().stream().noneMatch(
                MQVendorConfig::isEnabled
        )) {
            return false;
        }

        // if enabled client config missing name server item
        if (vendors.values().stream().anyMatch(
                cfg -> StringUtil.isBlank(cfg.getNameServer())
                    && StringUtil.isBlank(cfg.getEndpoints())
                    && cfg.isEnabled()
                    && !cfg.getVendorType().equals(MQVendor.VENDOR_MOCK)
        )) {
            return false;
        }

        return true;
    }

    public MQVendorConfig getVendorConfig(String topic) {
        String vendorId = findVendorId(topic);
        return vendors.get(vendorId);
    }

    public String findVendorId(String topic) {
        for (MQVendorConfig config : vendors.values()) {
            if (SetUtil.notEmpty(config.getTopics())
                    && config.getTopics().contains(topic)) {
                return config.getVendorId();
            }
        }

        if (StringUtil.notEmpty(defaultVendorId)) {
            return defaultVendorId;
        }

        throw new MQConfigException("The topic may not defined in the vendor config.");
    }

    public MQProducerConfig getProducerConfig(String vendorId) {
        MQProducerConfig producerConfig = new MQProducerConfig();

        MQVendorConfig vendorConfig = vendors.get(vendorId);
        if (null == vendorConfig) {
            throw new MQConfigException("The vendor config was not found. vendorId: " + vendorId);
        }

        producerConfig.setVendorConfig(vendorConfig);
        String group = StringUtil.isEmpty(vendorConfig.getDefaultProducerGroup())
                ? MQDefaultConst.DEFAULT_PRODUCER_GROUP
                : vendorConfig.getDefaultProducerGroup();
        producerConfig.setGroup(group);

        return producerConfig;
    }

    public MQTransactionProducerConfig getTransactionProducerConfig(String topic) {
        MQTransactionProducerConfig producerConfig = new MQTransactionProducerConfig();
        // Yes, use annotation topic as group
        producerConfig.setGroup(topic);
        producerConfig.setTopic(topic);

        MQVendorConfig vendorConfig = getVendorConfig(topic);
        if (null == vendorConfig) {
            throw new MQConfigException("The vendor config was not found. topic:" + topic);
        }
        producerConfig.setVendorConfig(vendorConfig);

        MQTransactionProducerConfig topicConfig = getTransactionProducerTopicConfig(topic);
        producerConfig.setMinCheckThreadNum(topicConfig.getMinCheckThreadNum());
        producerConfig.setMaxCheckThreadNum(topicConfig.getMaxCheckThreadNum());
        producerConfig.setMaxRequestHold(topicConfig.getMaxRequestHold());

        return producerConfig;
    }

    public MQTransactionProducerConfig getTransactionProducerTopicConfig(String topic) {
        if (!transactions.containsKey(topic)) {
            // transaction topic config must be configured.
            throw new MQConfigException("The transaction producer topic config not exists. topic: " + topic);
        }

        return transactions.get(topic);
    }

    public MQConsumerGroupConfig getConsumerGroupConfig(String group) {
        if (!consumerGroups.containsKey(group)) {
            MQConsumerGroupConfig config = new MQConsumerGroupConfig();
            config.setGroup(group);
            return config;
            // todolist@mq discuss it.
            //throw new MQConfigException("The consumer group config not exists. group: " + group);
        }

        return consumerGroups.get(group);
    }

    public MQConsumerConfig getConsumerConfig(MQConsumer annotation) {
        MQConsumerConfig consumerConfig = new MQConsumerConfig();

        // group config
        MQConsumerGroupConfig groupConfig = getConsumerGroupConfig(annotation.group());
        consumerConfig.setGroupConfig(groupConfig);

        // config from annotation
        List<String> topics = annotation.topics().length > 0
            ? Arrays.asList(annotation.topics())
            : Collections.singletonList(annotation.topic());

        // topic -> tags map
        Map<String, Set<String>> topicTags = new HashMap<>();
        for (String topic : topics) {
            topicTags.put(topic, new HashSet<>(Arrays.asList(annotation.tag())));
        }

        consumerConfig.setGroup(annotation.group());
        consumerConfig.setTopics(topics);
        consumerConfig.setTopicTags(topicTags);
        consumerConfig.setMessageClass(annotation.messageClass());

        // override default vendor config
        MQVendorConfig vendorConfig = getVendorConfig(topics.get(0));
        if (null == vendorConfig) {
            throw new MQConfigException("The vendor was not found.");
        }
        consumerConfig.setVendorConfig(vendorConfig);

        return consumerConfig;
    }

    public MQConsumerConfig getConsumerConfig(String group, Map<String, Set<String>> subscriptions) {
        MQConsumerConfig consumerConfig = new MQConsumerConfig();

        // group config
        MQConsumerGroupConfig groupConfig = getConsumerGroupConfig(group);
        consumerConfig.setGroupConfig(groupConfig);

        consumerConfig.setGroup(group);
        consumerConfig.setTopics(subscriptions.keySet().stream().toList());
        consumerConfig.setTopicTags(subscriptions);

        // override default vendor config
        MQVendorConfig vendorConfig = getVendorConfig(consumerConfig.getTopics().get(0));
        if (null == vendorConfig) {
            throw new MQConfigException("The vendor was not found.");
        }
        consumerConfig.setVendorConfig(vendorConfig);

        return consumerConfig;
    }

    public void updateConsumerGroupConfig(String group, int minThreadNum, int maxThreadNum) {
        MQConsumerGroupConfig config = consumerGroups.get(group);
        if (null == config) {
            throw new MQConfigException("Consumer group [" + group + "] config not found.");
        }

        config.setMinThreadNum(minThreadNum);
        config.setMaxThreadNum(maxThreadNum);
    }
}
