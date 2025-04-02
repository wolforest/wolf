package com.wolf.wolfno.config;

import com.wolf.common.util.lang.StringUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class WolfNoCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String enable = context.getEnvironment().getProperty("wolf.wolfno.enable");

        if (StringUtil.isBlank(enable)) {
            return false;
        }
        return Boolean.parseBoolean(enable);
    }
}
