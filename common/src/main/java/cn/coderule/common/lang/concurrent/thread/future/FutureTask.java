
package cn.coderule.common.lang.concurrent.thread.future;

import java.util.concurrent.Callable;

public class FutureTask<V> extends java.util.concurrent.FutureTask<V> {
    private final Runnable runnable;

    public FutureTask(final Callable<V> callable) {
        super(callable);
        this.runnable = null;
    }

    public FutureTask(final Runnable runnable, final V result) {
        super(runnable, result);
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
