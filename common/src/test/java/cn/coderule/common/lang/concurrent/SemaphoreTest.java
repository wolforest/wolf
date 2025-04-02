package cn.coderule.common.lang.concurrent;

import org.junit.Test;

import static org.junit.Assert.*;

public class SemaphoreTest {

    @Test
    public void acquire() {
        Semaphore semaphore = new Semaphore(3);
        SemaphoreGuard guard = semaphore.acquire();
        assertTrue("Semaphore release failed.", guard.release());
        assertFalse("Semaphore release failed.", guard.release());
        assertFalse("Semaphore release failed.", guard.release());
        assertFalse("Semaphore release failed.", guard.release());
    }

    @Test
    public void tryAcquire() {
        Semaphore semaphore = new Semaphore(1);
        SemaphoreGuard firstGuard = semaphore.tryAcquire();
        assertNotNull("tryAcquire failed", firstGuard);

        SemaphoreGuard secondGuard = semaphore.tryAcquire();
        assertNull("tryAcquire failed", secondGuard);

        SemaphoreGuard thirdGuard = semaphore.tryAcquire();
        assertNull("tryAcquire failed", thirdGuard);

        firstGuard.release();
        SemaphoreGuard forthGuard = semaphore.tryAcquire();
        assertNotNull("tryAcquire failed", firstGuard);

        SemaphoreGuard fifthGuard = semaphore.tryAcquire();
        assertNull("tryAcquire failed", fifthGuard);

        SemaphoreGuard sixthGuard = semaphore.tryAcquire();
        assertNull("tryAcquire failed", sixthGuard);

    }
}
