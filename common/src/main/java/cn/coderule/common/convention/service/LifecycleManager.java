package cn.coderule.common.convention.service;

import java.util.ArrayList;
import java.util.List;

public class LifecycleManager implements Lifecycle {
    private final List<Lifecycle> components = new ArrayList<>();

    public void register(Lifecycle component) {
        components.add(component);
    }

    @Override
    public void initialize() throws Exception {
        components.forEach(lifecycle -> {
            try {
                lifecycle.initialize();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void cleanup() throws Exception {
        components.forEach(lifecycle -> {
            try {
                lifecycle.cleanup();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void start() throws Exception {
        components.forEach(lifecycle -> {
            try {
                lifecycle.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void shutdown() throws Exception {
        components.forEach(lifecycle -> {
            try {
                lifecycle.shutdown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
