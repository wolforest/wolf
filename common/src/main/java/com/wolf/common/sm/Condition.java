package com.wolf.common.sm;

import com.wolf.common.convention.container.Context;

/**
 * com.wolf.common.sm
 *
 * @author Wingle
 * @since 2020/11/30 1:06 上午
 **/
public interface Condition<S extends State, E extends Event, C extends Context> {
    boolean isSatisfied(C context);
}
