package cn.coderule.common.convention.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final ConcurrentMap<Object, List<Consumer<Object>>> listeners;
    private final ConcurrentMap<Object, EventDependency> dependencies;

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public EventBus() {
        this.listeners = new ConcurrentHashMap<>();
        this.dependencies = new ConcurrentHashMap<>();
    }

    public void on(Object event, Consumer<Object> listener) {
        this.listeners.computeIfAbsent(
            event,
            _ -> new java.util.ArrayList<>()
        ).add(listener);
    }

    public void off(Object event, Consumer<Object> listener) {
        List<Consumer<Object>> handlers = this.listeners.get(event);
        if (handlers == null) {
            return;
        }

        handlers.remove(listener);
        if (handlers.isEmpty()) {
            this.listeners.remove(event);
        }
    }

    public void emit(Object event, Object arg) {
        notifyListeners(event, arg);
        propagate(event);
    }

    public void bind(Object targetEvent, Object targetArg, Object... sourceEvents) {
        if (sourceEvents == null || sourceEvents.length == 0) {
            throw new IllegalArgumentException("sourceEvents is null or empty");
        }

        EventDependency dependency = EventDependency.of(targetEvent, targetArg, sourceEvents);

        for(Object event : sourceEvents) {
            dependencies.put(event, dependency);
        }
    }

    private void notifyListeners(Object event, Object arg) {
        List<Consumer<Object>> handlers = this.listeners.get(event);
        if (handlers == null) {
            return;
        }

        for (Consumer<Object> handler : handlers) {
            handler.accept(arg);
        }
    }

    private void propagate(Object event) {
        EventDependency dependency = dependencies.get(event);
        if (dependency == null) {
            return;
        }

        if (!dependency.markFired(event)) {
            return;
        }

        emit(dependency.getEvent(), dependency.getArg());
        dependency.reset();
    }
}
