package com.wolf.common.sm.impl;


import com.wolf.common.sm.Event;
import com.wolf.common.sm.State;
import com.wolf.common.sm.StateGraph;
import com.wolf.common.sm.exception.DuplicateStateException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * com.wolf.common.sm
 *
 * @author Wingle
 * @since 2019/12/16 10:32 下午
 **/
public class DefaultStateGraph<S extends State, E extends Event> implements StateGraph<S, E> {
    private S rootState;
    private final Map<String, Map<String, S>> stateTree;
    private final Map<String, Set<E>> eventMap;
    private final Map<Integer, S> stateCodeMap;

    public DefaultStateGraph() {
        this(null);
    }

    public DefaultStateGraph(S state) {
        stateTree = new HashMap<>();
        stateCodeMap = new HashMap<>();
        eventMap = new HashMap<>();

        setRoot(state);
    }

    @Override
    public S getRootState() {
        return rootState;
    }

    @Override
    public StateGraph<S, E> addTransition(S source, S target, E event) {
        registerStates(source, target);

        String sourceKey = source.getName();

        Map<String, S> stateMap = this.stateTree.get(sourceKey);
        if (stateMap == null) {
            createStateMap(sourceKey, event, target);
            return this;
        }
        addStateToMap(stateMap, sourceKey, event, target);

        return this;
    }

    @Override
    public S transit(S source, E event) {
        String sourceKey = source.getName();
        String eventKey = getEventKey(event);

        Map<String, S> stateMap = this.stateTree.get(sourceKey);
        if (stateMap == null) {
            return null;
        }

        return stateMap.get(eventKey);
    }

    @Override
    public boolean canTransit(S source, S target) {
        String sourceKey = source.getName();
        Map<String, S> stateMap = this.stateTree.get(sourceKey);

        return stateMap.containsValue(target);
    }

    @Override
    public boolean canTransit(S source, E event) {
        S target = transit(source, event);

        return null != target;
    }

    @Override
    public Set<E> getTransitions(S source) {
        String sourceKey = source.getName();
        return eventMap.get(sourceKey);
    }

    @Override
    public S getState(int code) {
        return stateCodeMap.get(code);
    }

    @SafeVarargs
    private final void registerStates(S... states) {
        for (S state : states) {
            registerState(state);
        }
    }

    private DefaultStateGraph<S, E> setRoot(S state) {
        if (state == null) {
            return this;
        }

        this.rootState = state;
        registerState(state);

        return this;
    }

    private void registerState(S state) {
        int code = state.getCode();

        if (stateCodeMap.containsKey(code)) {
            return;
        }
        stateCodeMap.put(code, state);
    }

    private void createStateMap(String sourceKey, E event, S target) {
        String eventKey = getEventKey(event);

        Map<String, S> stateMap = new TreeMap<>();
        stateMap.put(eventKey, target);
        stateTree.put(sourceKey, stateMap);

        Set<E> eventSet = new HashSet<>();
        eventSet.add(event);
        eventMap.put(sourceKey, eventSet);

    }

    private void addStateToMap(Map<String, S> stateMap, String sourceKey, E event, S target) {
        String eventKey = getEventKey(event);
        S state = stateMap.get(eventKey);

        if (state != null) {
            String targetKey = target.getClass().getName();
            throw new DuplicateStateException(sourceKey, targetKey, eventKey);
        }

        stateMap.put(eventKey, target);
    }

    private String getEventKey(E event) {
        if (event == null) {
            return "null";
        }

        if (event instanceof Enum) {
            return ((Enum<?>) event).name();
        }

        return event.getClass().getName();
    }

}
