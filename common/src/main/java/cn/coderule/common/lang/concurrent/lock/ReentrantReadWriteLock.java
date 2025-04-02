package cn.coderule.common.lang.concurrent.lock;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ReentrantReadWriteLock extends java.util.concurrent.locks.ReentrantReadWriteLock {
    private final WriteLock writeLock;
    private final ReadLock readLock;

    public ReentrantReadWriteLock() {
        this(false);
    }

    public ReentrantReadWriteLock(boolean fair) {
        super(fair);
        writeLock = new WriteLock(this);
        readLock = new ReadLock(this);
    }

    public static class WriteLock extends java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock {

        public WriteLock(java.util.concurrent.locks.ReentrantReadWriteLock lock) {
            super(lock);
        }

        public void unlock() {
            try {
                super.unlock();
            } catch (IllegalMonitorStateException e) {
                log.error("Attempting to unlock a read lock that was not acquired.", e);
            }
        }
    }

    public static class ReadLock extends java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock {

        public ReadLock(java.util.concurrent.locks.ReentrantReadWriteLock lock) {
            super(lock);
        }

        public void unlock() {
            try {
                super.unlock();
            } catch (IllegalMonitorStateException e) {
                log.error("Attempting to unlock a read lock that was not acquired.", e);
            }
        }
    }
}
