package com.wolf.mqclient.annotation;

import com.wolf.mqclient.config.MQDefaultConst;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author weixing
 * @since 2022/10/11 20:18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQTransaction {
    String topic();

    //String group();

    String tag() default MQDefaultConst.DEFAULT_TAG;

    String vendorId() default "";

    String env() default "";
}
