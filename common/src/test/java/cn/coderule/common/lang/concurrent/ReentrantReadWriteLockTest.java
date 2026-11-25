package cn.coderule.common.lang.concurrent;

import cn.coderule.common.lang.concurrent.lock.ReentrantReadWriteLock;
import org.junit.jupiter.api.Test;

public class ReentrantReadWriteLockTest {

    @Test
    public void unlockWithoutException() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        for (int i = 0; i < 10; i++) {
            lock.getWriteLock().unlock();
            lock.getReadLock().unlock();
        }
    }
}
