package com.wolf.mqclient.vendor.rocketmq.acl;

import com.wolf.common.util.lang.StringUtil;
import com.wolf.mqclient.config.MQVendorConfig;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;

/**
 * com.wolf.mqclient.adapter.rocketmq.acl
 *
 * @author Wingle
 * @since 2021/12/16 下午6:05
 **/
public class AclHookFactory {
    public static AclClientRPCHook create(MQVendorConfig config) {
        if (StringUtil.isBlank(config.getAppKey())) {
            return null;
        }

        return new AclClientRPCHook(
                new SessionCredentials(
                        config.getAppKey(),
                        config.getAppSecret()
                )
        );
    }
}
