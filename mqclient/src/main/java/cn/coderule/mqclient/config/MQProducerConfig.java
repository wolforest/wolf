package cn.coderule.mqclient.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weixing
 * @since 2022/10/11 17:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQProducerConfig {
    protected MQVendorConfig vendorConfig;
    protected String group;
}
