package cn.coderule.common.convention.service;

public interface Lifecycle {
    void start();
    void shutdown();

    /**
     * Initialization, it will be called before start
     */
    void initialize();

    /**
     * cleanup, it will be called before shutdown
     */
    void cleanup();

    State getState();

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
