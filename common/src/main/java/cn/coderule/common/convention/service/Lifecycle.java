package cn.coderule.common.convention.service;

public interface Lifecycle {
    void start() throws Exception;
    void shutdown() throws Exception;

    /**
     * Initialization, it will be called before start
     */
    default void initialize() throws Exception {}

    /**
     * cleanup, it will be called before shutdown
     */
    default void cleanup() throws Exception {}

    default State getState() {
        return State.RUNNING;
    }

    default boolean isRunning() {
        return State.RUNNING.equals(getState());
    }

    default boolean isWaiting() {
        return State.WAITING.equals(getState());
    }

    enum State {
        INITIALIZING,
        STARTING,
        RUNNING,
        WAITING,
        ENDING,
        SHUTTING_DOWN,
        TERMINATED
    }
}
