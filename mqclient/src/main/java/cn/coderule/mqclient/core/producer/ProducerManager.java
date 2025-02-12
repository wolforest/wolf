package cn.coderule.mqclient.core.producer;

import cn.coderule.common.util.lang.collection.ListUtil;
import cn.coderule.mqclient.config.MQConfig;
import cn.coderule.mqclient.config.MQProducerConfig;
import cn.coderule.mqclient.config.MQVendorConfig;
import cn.coderule.mqclient.config.exception.MQConfigException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weixing
 * @since 2022/10/11 20:48
 */
@Slf4j
public class ProducerManager {

    private final MQConfig mqConfig;

    private final VendorProducerFactory vendorProducerFactory;

    @Getter
    private final Map<String, List<VendorProducer>> vendorProducers = new HashMap<>();

    public ProducerManager(MQConfig mqConfig) {
        this.mqConfig = mqConfig;
        this.vendorProducerFactory = new VendorProducerFactory();
    }

    public void start(MQVendorConfig config) {
        if (!config.isEnabled()) {
            log.info("[MQ] producer can not start, because the vendor is disabled. vendorId={}", config.getVendorId());
            return;
        }

        for (int i = 1; i <= config.getProducerNum(); i++) {
            MQProducerConfig producerConfig = mqConfig.getProducerConfig(config.getVendorId());

            VendorProducer producer = vendorProducerFactory.create(producerConfig);
            producer.start();

            hold(config.getVendorId(), producer);

            log.info("[MQ] producer has been started. producerId={}, vendorId={}, group={}", producer.getVendorInstanceName(), config.getVendorId(), producerConfig.getGroup());
        }
    }

    public void shutdownAll() {
        vendorProducers.values().forEach(producerList -> {
            producerList.forEach(VendorProducer::shutdown);
        });
    }

    public VendorProducer get(String vendorId) {
        List<VendorProducer> list = vendorProducers.get(vendorId);
        if (ListUtil.isEmpty(list)) {
            throw new MQConfigException("The vendor producer was not found. vendorId: " + vendorId);
        }

        // todolist@mq random pick up
        return list.get(0);
    }

    public VendorProducer getByUniqueTopic(String topic) {
        String vendorId = mqConfig.findVendorId(topic);
        return get(vendorId);
    }

    private void hold(String vendorId, VendorProducer producer) {
        List<VendorProducer> list = vendorProducers.get(vendorId);
        if (null == list) {
            list = new ArrayList<>();
            vendorProducers.put(vendorId, list);
        }
        list.add(producer);
    }
}
