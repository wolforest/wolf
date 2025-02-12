package cn.coderule.mqclient.annotation;

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
public @interface MQConsumer {
    String topic() default "DEFAULT_TOPIC";

    String[] topics() default {};

    String group() default "DEFAULT_GROUP";

    String[] tag() default {};

    Class<?> messageClass() default Object.class;

    //String vendorType() default "";

    String vendorId() default "";
}
