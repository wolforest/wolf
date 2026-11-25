package cn.coderule.common.lang.concurrent;

import cn.coderule.common.lang.concurrent.sync.SemaphoreGuard;
import java.util.concurrent.Semaphore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SemaphoreGuardTest {

    @Test
    public void release_no_guard() {
        Semaphore semaphore = new Semaphore(3);

        semaphore.release(5);

        assertEquals(8, semaphore.availablePermits(), "semaphore release failed.");
    }

    @Test
    public void release_with_guard() {
        Semaphore semaphore = new Semaphore(3);
        SemaphoreGuard guard = new SemaphoreGuard(semaphore);

        assertTrue(guard.release(), "SemaphoreGuard release failed");
        assertFalse(guard.release(), "SemaphoreGuard release failed");
        assertFalse(guard.release(), "SemaphoreGuard release failed");

        guard.reset();
        assertTrue(guard.release(), "SemaphoreGuard release failed");
        assertFalse(guard.release(), "SemaphoreGuard release failed");
        assertFalse(guard.release(), "SemaphoreGuard release failed");
    }

}
