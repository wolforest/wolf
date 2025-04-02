package cn.coderule.common.lang.concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;

public class SemaphoreGuard {
    @Getter
    private final Semaphore semaphore;
    private final AtomicBoolean locked = new AtomicBoolean(false);

    public SemaphoreGuard(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public boolean release() {
        if (null == semaphore) {
            return false;
        }

        if (locked.compareAndSet(false, true)) {
            semaphore.release();
            return true;
        }

        return false;
    }

    public void reset() {
        locked.set(false);
    }
}
