package cn.coderule.common.lang.concurrent.lock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReentrantLock extends java.util.concurrent.locks.ReentrantLock {
    @Override
    public void unlock() {
        try {
            super.unlock();
        } catch (IllegalMonitorStateException e) {
            log.error("Attempting to unlock a read lock that was not acquired.", e);
        }
    }
}
