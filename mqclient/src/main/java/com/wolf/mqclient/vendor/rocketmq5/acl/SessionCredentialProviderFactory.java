package com.wolf.mqclient.vendor.rocketmq5.acl;

import com.wolf.common.util.lang.StringUtil;
import com.wolf.mqclient.config.MQVendorConfig;
import org.apache.rocketmq.client.apis.SessionCredentialsProvider;
import org.apache.rocketmq.client.apis.StaticSessionCredentialsProvider;

/**
 * @author weixing
 * @since 2023/6/9 16:17
 */
public class SessionCredentialProviderFactory {
    public static SessionCredentialsProvider create(MQVendorConfig config) {
        if (StringUtil.isBlank(config.getAppKey())) {
            return null;
        }

        return new StaticSessionCredentialsProvider(
            config.getAppKey(),
            config.getAppSecret()
        );
    }
}
