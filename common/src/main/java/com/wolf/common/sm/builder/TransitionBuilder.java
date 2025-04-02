package com.wolf.common.sm.builder;

import com.wolf.common.convention.container.Context;
import com.wolf.common.sm.Action;
import com.wolf.common.sm.Condition;
import com.wolf.common.sm.Event;
import com.wolf.common.sm.State;
import com.wolf.common.sm.StateMachine;
import com.wolf.common.sm.Transition;

/**
 * com.wolf.common.sm.builder
 *
 * @author Wingle
 * @since 2020/12/2 1:47 上午
 **/
public class TransitionBuilder<S extends State, E extends Event, C extends Context> {
    private final StateMachineBuilder<S, E, C> machineBuilder;

    public TransitionBuilder(StateMachineBuilder<S, E, C> machineBuilder) {
        this.machineBuilder = machineBuilder;
    }

    public TransitionBuilder<S, E, C> source(S state) {
        return this;
    }

    public TransitionBuilder<S, E, C> target(S state) {
        return this;
    }

    public TransitionBuilder<S, E, C> event(E event) {
        return this;
    }

    public TransitionBuilder<S, E, C> when(Condition<S, E, C> condition) {
        return this;
    }

    public TransitionBuilder<S, E, C> perform(Action<S, E, C> action) {
        return this;
    }

    public Transition<S, E, C> buildTransition() {
        return null;
    }

    public TransitionBuilder<S, E, C> addTransition() {
        machineBuilder.addTransition(buildTransition());
        return new TransitionBuilder<>(machineBuilder);
    }

    public StateMachine<S, E, C> build() {
        machineBuilder.addTransition(buildTransition());
        return machineBuilder.build();
    }
}
