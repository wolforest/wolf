package cn.coderule.common.lang.concurrent;

import cn.coderule.common.lang.concurrent.sync.SemaphoreGuard;
import java.util.concurrent.Semaphore;
import org.junit.Test;

import static org.junit.Assert.*;

public class SemaphoreGuardTest {

    @Test
    public void release_no_guard() {
        Semaphore semaphore = new Semaphore(3);

        semaphore.release(5);

        assertEquals("semaphore release failed.", 8, semaphore.availablePermits());
    }

    @Test
    public void release_with_guard() {
        Semaphore semaphore = new Semaphore(3);
        SemaphoreGuard guard = new SemaphoreGuard(semaphore);

        assertTrue("SemaphoreGuard release failed", guard.release());
        assertFalse("SemaphoreGuard release failed", guard.release());
        assertFalse("SemaphoreGuard release failed", guard.release());

        guard.reset();
        assertTrue("SemaphoreGuard release failed", guard.release());
        assertFalse("SemaphoreGuard release failed", guard.release());
        assertFalse("SemaphoreGuard release failed", guard.release());
    }

}
