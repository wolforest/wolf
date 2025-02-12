package cn.coderule.mqclient.config;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weixing
 * @since 2022/10/12 14:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MQVendorConfig {
    private String vendorId;
    private String vendorType;
    private String nameServer;
    private String endpoints;
    private String appKey;
    private String appSecret;
    private String username;
    private String password;
    private Set<String> topics;
    private String defaultProducerGroup;
    @Builder.Default
    private int producerNum = 1;
    // todolist@mq default transaction producer num for each topic
    @Builder.Default
    private int transactionProducerNum = 1;
    private boolean enabled;
}
