package com.wolf.common.sm.impl;

import com.wolf.common.convention.container.Context;
import com.wolf.common.sm.Event;
import com.wolf.common.sm.State;
import com.wolf.common.sm.Transition;

import java.util.Map;

/**
 * com.wolf.common.sm.impl
 *
 * @author Wingle
 * @since 2020/12/2 1:14 上午
 **/
public class TransitionMap<S extends State, E extends Event, C extends Context> {
    private Map<Integer, Map<String, Transition<S, E, C>>> map;

    public Transition<S, E, C> get(S from, E event) {
        return null;
    }

    public Transition<S, E, C> put(S from, S to, S event) {
        return null;
    }

}
