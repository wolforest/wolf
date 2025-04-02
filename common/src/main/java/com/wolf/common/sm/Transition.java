package com.wolf.common.sm;

import com.wolf.common.convention.container.Context;

/**
 * com.wolf.common.sm
 *
 * @author Wingle
 * @since 2020/11/30 1:06 上午
 **/
public interface Transition<S extends State, E extends Event, C extends Context> {
    /**
     *
     * @param context context
     * @return to or null
     */
    S transit(C context);

    S getSource();
    S getTarget();
    E getEvent();
    C getContext();

    Action<S, E, C> getAction();
    Condition<S, E, C> getCondition();
}
