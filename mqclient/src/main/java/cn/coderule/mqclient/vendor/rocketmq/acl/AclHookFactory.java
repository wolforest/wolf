package cn.coderule.mqclient.vendor.rocketmq.acl;

import cn.coderule.common.util.lang.string.StringUtil;
import cn.coderule.mqclient.config.MQVendorConfig;
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
