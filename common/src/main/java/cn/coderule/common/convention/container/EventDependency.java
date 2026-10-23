package cn.coderule.common.convention.container;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;

public class EventDependency {
    private final ConcurrentMap<Object, Boolean> sourceMap;
    @Getter
    private final Object event;
    @Getter
    private final Object arg;

    public static EventDependency of(Object targetEvent, Object targetArg, Object... sourceEvents) {
        if (sourceEvents == null || sourceEvents.length == 0) {
            throw new IllegalArgumentException("sourceEvents is empty");
        }

        EventDependency dependency = new EventDependency(targetEvent, targetArg);
        for (Object sourceEvent : sourceEvents) {
            dependency.addSource(sourceEvent);
        }

        return dependency;
    }

    public EventDependency(Object event, Object arg) {
        this.event = event;
        this.arg = arg;

        this.sourceMap = new ConcurrentHashMap<>();
    }

    public boolean isEmpty() {
        return sourceMap.isEmpty();
    }

    public void addSource(Object sourceEvent) {
        if (sourceMap.containsKey(sourceEvent)) {
            return;
        }

        sourceMap.put(sourceEvent, false);
    }

    public boolean markFired(Object sourceEvent) {
        if (!sourceMap.containsKey(sourceEvent)) {
            return false;
        }

        if (sourceMap.get(sourceEvent)) {
            return false;
        }

        sourceMap.put(sourceEvent, true);
        return isSatisfied();
    }

    public void reset() {
        sourceMap.replaceAll((_, _) -> false);
    }

    private boolean isSatisfied() {
        return sourceMap.values()
            .stream()
            .allMatch(Boolean.TRUE::equals);
    }
}
