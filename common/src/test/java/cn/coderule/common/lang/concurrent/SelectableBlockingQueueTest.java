package cn.coderule.common.lang.concurrent;

import cn.coderule.common.lang.concurrent.queue.SelectableBlockingQueue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SelectableBlockingQueueTest {

    @Test
    public void arrayBlockingQueueBasics() throws Exception {
        SelectableBlockingQueue<Integer> queue =
                new SelectableBlockingQueue<>(SelectableBlockingQueue.Type.ARRAY, 2);
        assertTrue(queue.offer(1));
        assertTrue(queue.offer(2));
        assertFalse(queue.offer(3));
        assertEquals(0, queue.remainingCapacity());

        assertEquals(Integer.valueOf(1), queue.take());
        assertEquals(1, queue.remainingCapacity());
        assertEquals(Integer.valueOf(2), queue.poll());
        assertNull(queue.poll());
    }

    @Test
    public void linkedBlockingQueueUnbounded() throws Exception {
        SelectableBlockingQueue<String> queue =
                new SelectableBlockingQueue<>(SelectableBlockingQueue.Type.LINKED);
        assertTrue(queue.offer("a"));
        assertTrue(queue.offer("b"));
        assertEquals("a", queue.take());
        assertEquals("b", queue.take());
        assertNull(queue.poll(10, TimeUnit.MILLISECONDS));
    }

    @Test
    public void concurrentLinkedListBlockingQueueCapacity() throws Exception {
        SelectableBlockingQueue<Integer> queue =
                new SelectableBlockingQueue<>(SelectableBlockingQueue.Type.CONCURRENT_LINKED_LIST, 1);
        queue.put(7);
        assertFalse(queue.offer(8));
        assertEquals(Integer.valueOf(7), queue.take());
        assertTrue(queue.offer(8, 50, TimeUnit.MILLISECONDS));
        assertEquals(Integer.valueOf(8), queue.take());
    }

    @Test
    public void concurrentLinkedListDrainTo() throws Exception {
        SelectableBlockingQueue<Integer> queue =
                new SelectableBlockingQueue<>(SelectableBlockingQueue.Type.CONCURRENT_LINKED_LIST, 4);
        queue.put(1);
        queue.put(2);
        queue.put(3);

        List<Integer> drained = new ArrayList<>();
        int count = queue.drainTo(drained, 2);
        assertEquals(2, count);
        assertEquals(2, drained.size());
        assertEquals(Integer.valueOf(1), drained.get(0));
        assertEquals(Integer.valueOf(2), drained.get(1));
        assertEquals(Integer.valueOf(3), queue.take());
    }
}
