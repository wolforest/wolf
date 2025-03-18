package cn.coderule.common.lang.concurrent;

import org.junit.Test;

import static org.junit.Assert.*;

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
