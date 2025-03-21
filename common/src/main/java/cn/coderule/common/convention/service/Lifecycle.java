package cn.coderule.common.convention.service;

public interface Lifecycle {
    void start();
    void shutdown();

    /**
     * Initialization, it will be called before start
     */
    default void initialize() {}

    /**
     * cleanup, it will be called before shutdown
     */
    default void cleanup() {}

    default State getState() {
        return State.RUNNING;
    }

    default boolean isRunning() {
        return State.RUNNING.equals(getState());
    }

    enum State {
        INITIALIZING,
        STARTING,
        RUNNING,
        SHUTTING_DOWN,
        TERMINATED
    }
}
