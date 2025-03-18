package cn.coderule.common.lang.concurrent;

import java.util.concurrent.locks.Lock;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReentrantLockTest {

    @Test
    public void unlock() {
        Lock lock = new ReentrantLock();

        for (int i = 0; i < 10; i++) {
            lock.unlock();
        }
    }
}
