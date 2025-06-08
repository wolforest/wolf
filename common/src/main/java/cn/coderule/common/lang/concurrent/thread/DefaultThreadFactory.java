
package cn.coderule.common.lang.concurrent.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultThreadFactory implements ThreadFactory {
    private final AtomicLong threadIndex = new AtomicLong(0);
    private final String threadPrefix;
    private final boolean daemon;

    public DefaultThreadFactory(final String threadPrefix) {
        this(threadPrefix, false);
    }

    public DefaultThreadFactory(final String threadPrefix, boolean daemon) {
        this.threadPrefix = threadPrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadPrefix + this.threadIndex.incrementAndGet());
        thread.setDaemon(daemon);

        // Log all uncaught exception
        thread.setUncaughtExceptionHandler((t, e) ->
            log.error("[BUG] Thread has an uncaught exception, threadId={}, threadName={}",
                t.getId(), t.getName(), e));

        return thread;
    }
}
