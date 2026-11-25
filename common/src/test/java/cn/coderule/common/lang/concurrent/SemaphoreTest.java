package cn.coderule.common.lang.concurrent;

import cn.coderule.common.lang.concurrent.sync.Semaphore;
import cn.coderule.common.lang.concurrent.sync.SemaphoreGuard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SemaphoreTest {

    @Test
    public void acquire() {
        Semaphore semaphore = new Semaphore(3);
        SemaphoreGuard guard = semaphore.acquire();
        assertTrue(guard.release(), "Semaphore release failed.");
        assertFalse(guard.release(), "Semaphore release failed.");
        assertFalse(guard.release(), "Semaphore release failed.");
        assertFalse(guard.release(), "Semaphore release failed.");
    }

    @Test
    public void tryAcquire() {
        Semaphore semaphore = new Semaphore(1);
        SemaphoreGuard firstGuard = semaphore.tryAcquire();
        assertNotNull(firstGuard, "tryAcquire failed");

        SemaphoreGuard secondGuard = semaphore.tryAcquire();
        assertNull(secondGuard, "tryAcquire failed");

        SemaphoreGuard thirdGuard = semaphore.tryAcquire();
        assertNull(thirdGuard, "tryAcquire failed");

        firstGuard.release();
        SemaphoreGuard forthGuard = semaphore.tryAcquire();
        assertNotNull(firstGuard, "tryAcquire failed");

        SemaphoreGuard fifthGuard = semaphore.tryAcquire();
        assertNull(fifthGuard, "tryAcquire failed");

        SemaphoreGuard sixthGuard = semaphore.tryAcquire();
        assertNull(sixthGuard, "tryAcquire failed");

    }
}
