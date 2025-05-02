package cn.coderule.common.convention.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final ConcurrentMap<Object, List<Consumer<Object>>> listeners;

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public EventBus() {
        this.listeners = new ConcurrentHashMap<>();
    }

    public void on(Object event, Consumer<Object> listener) {
        this.listeners.computeIfAbsent(
            event,
            k -> new java.util.ArrayList<>()
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
        List<Consumer<Object>> handlers = this.listeners.get(event);
        if (handlers == null) {
            return;
        }

        for (Consumer<Object> handler : handlers) {
            handler.accept(arg);
        }
    }
}
