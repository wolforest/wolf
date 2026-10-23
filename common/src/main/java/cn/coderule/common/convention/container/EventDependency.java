package cn.coderule.common.convention.container;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

public class EventDependency {
    private final Set<Object> sources;
    private final Set<Object> fired;
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

        this.sources = ConcurrentHashMap.newKeySet();
        this.fired = ConcurrentHashMap.newKeySet();
    }

    public boolean isEmpty() {
        return sources.isEmpty();
    }

    public void addSource(Object sourceEvent) {
        sources.add(sourceEvent);
    }

    public boolean markFired(Object sourceEvent) {
        if (!sources.contains(sourceEvent)) {
            return false;
        }

        if (!fired.add(sourceEvent)) {
            return false;
        }

        return fired.size() == sources.size();
    }

    public void reset() {
        fired.clear();
    }
}
