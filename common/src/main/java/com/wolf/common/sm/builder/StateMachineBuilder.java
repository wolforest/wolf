package com.wolf.common.sm.builder;

import com.wolf.common.convention.container.Context;
import com.wolf.common.sm.Event;
import com.wolf.common.sm.State;
import com.wolf.common.sm.StateMachine;
import com.wolf.common.sm.Transition;

/**
 * com.wolf.common.sm.builder
 *
 * @author Wingle
 * @since 2020/12/2 9:46 上午
 **/
public interface StateMachineBuilder<S extends State, E extends Event, C extends Context> {
    TransitionBuilder<S, E, C> addTransition();

    TransitionBuilder<S, E, C> source(S state);

    TransitionBuilder<S, E, C> target(S state);

    TransitionBuilder<S, E, C> event(E event);

    void addTransition(Transition<S, E, C> transition);

    StateMachine<S, E, C> build();
}
