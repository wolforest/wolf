package com.wolf.common.sm.demo.machine;

import com.wolf.common.convention.container.Context;
import com.wolf.common.sm.Event;
import com.wolf.common.sm.State;
import com.wolf.common.sm.StateMachine;
import com.wolf.common.sm.impl.DefaultStateMachine;

/**
 * com.wolf.common.sm.demo.machine
 *
 * @author Wingle
 * @since 2020/12/2 9:53 上午
 **/
public class Style {
    /**
     * embed stateMachine application
     */
    public void simpleStateMachine() {
        StateMachine<State, Event, Context> stateMachine = DefaultStateMachine.builder()
                .source(null).target(null).event(null)
                .source(null).target(null).event(null)

                .build();
    }

    /**
     * stateMachine centered application
     * store the event into MQ to distribute system
     */
    public void stateMachineWithAction() {
        StateMachine<State, Event, Context> stateMachine = DefaultStateMachine.builder()
                .addTransition()
                .source(null).target(null).event(null)
                .when(null).perform(null)
                .when(null).perform(null)

                .addTransition()
                .source(null).target(null).event(null)
                .when(null).perform(null)
                .when(null).perform(null)

                .build();
    }
}
