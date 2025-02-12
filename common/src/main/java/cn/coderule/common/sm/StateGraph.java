package cn.coderule.common.sm;

import java.util.Set;

/**
 * com.wolf.common.sm
 *
 * @author Wingle
 * @since 2019/12/16 10:20 下午
 **/
public interface StateGraph<S extends State, E extends Event> {
    S getRootState();

    S getState(int code);

    Set<E> getTransitions(S source);

    StateGraph<S, E> addTransition(S source, S target, E event);

    S transit(S source, E event);

    boolean canTransit(S source, S target);

    boolean canTransit(S source, E event);
}
