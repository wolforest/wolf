package cn.coderule.common.lang.concurrent;

public class Semaphore {
    private final java.util.concurrent.Semaphore semaphore;

    public Semaphore(int permits) {
        this.semaphore = new java.util.concurrent.Semaphore(permits);
    }

    public SemaphoreGuard acquire() {
        semaphore.acquireUninterruptibly();
        return new SemaphoreGuard(this.semaphore);
    }

    public SemaphoreGuard tryAcquire() {
        if (semaphore.tryAcquire()) {
            return new SemaphoreGuard(this.semaphore);
        }
        return null;
    }
}
