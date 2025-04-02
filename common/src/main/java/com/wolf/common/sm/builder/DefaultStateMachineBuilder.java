package com.wolf.common.sm.builder;

import com.wolf.common.convention.container.Context;
import com.wolf.common.sm.Event;
import com.wolf.common.sm.State;
import com.wolf.common.sm.StateMachine;
import com.wolf.common.sm.Transition;
import com.wolf.common.sm.impl.DefaultStateMachine;

/**
 * com.wolf.common.sm.builder
 *
 * @author Wingle
 * @since 2020/12/2 9:46 上午
 **/
public class DefaultStateMachineBuilder<S extends State, E extends Event, C extends Context> implements StateMachineBuilder<S, E, C> {

    public TransitionBuilder<S, E, C> addTransition() {
        return new TransitionBuilder<>(this);
    }

    @Override
    public TransitionBuilder<S, E, C> source(S state) {
        return new TransitionBuilder<>(this);
    }

    @Override
    public TransitionBuilder<S, E, C> target(S state) {
        return new TransitionBuilder<>(this);
    }

    @Override
    public TransitionBuilder<S, E, C> event(E event) {
        return new TransitionBuilder<>(this);
    }

    @Override
    public void addTransition(Transition<S, E, C> transition) {

    }

    public StateMachine<S, E, C> build() {
        return new DefaultStateMachine<>();
    }
}
