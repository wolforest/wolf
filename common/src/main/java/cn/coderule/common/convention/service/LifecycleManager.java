package cn.coderule.common.convention.service;

import java.util.ArrayList;
import java.util.List;

public class LifecycleManager implements Lifecycle {
    private final List<Lifecycle> components = new ArrayList<>();

    public void register(Lifecycle component) {
        components.add(component);
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public void initialize() {
        components.forEach(Lifecycle::initialize);
    }

    @Override
    public void cleanup() {
        components.forEach(Lifecycle::cleanup);
    }

    @Override
    public void start() {
        components.forEach(Lifecycle::start);
    }

    @Override
    public void shutdown() {
        components.forEach(Lifecycle::shutdown);
    }
}
