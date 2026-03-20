package cn.coderule.common.lang.concurrent.queue;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public final class ConcurrentLinkedBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E> {
    private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
    private final Semaphore availableItems = new Semaphore(0);
    private final Semaphore availableSpaces;
    private final int capacity;

    public ConcurrentLinkedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.availableSpaces = new Semaphore(capacity);
    }

    @Override
    public int size() {
        int size = capacity - availableSpaces.availablePermits();
        return Math.max(0, size);
    }

    @Override
    public int remainingCapacity() {
        return availableSpaces.availablePermits();
    }

    @Override
    public boolean offer(E e) {
        Objects.requireNonNull(e, "element");
        if (!availableSpaces.tryAcquire()) {
            return false;
        }
        queue.add(e);
        availableItems.release();
        return true;
    }

    @Override
    public void put(E e) throws InterruptedException {
        Objects.requireNonNull(e, "element");
        availableSpaces.acquire();
        queue.add(e);
        availableItems.release();
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        Objects.requireNonNull(e, "element");
        if (!availableSpaces.tryAcquire(timeout, unit)) {
            return false;
        }
        queue.add(e);
        availableItems.release();
        return true;
    }

    @Override
    public E take() throws InterruptedException {
        while (true) {
            availableItems.acquire();
            E value = pollFromQueue();
            if (value != null) {
                releaseSpaces(1);
                return value;
            }
        }
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        long remainingNanos = unit.toNanos(timeout);
        while (remainingNanos > 0) {
            if (!availableItems.tryAcquire(remainingNanos, TimeUnit.NANOSECONDS)) {
                return null;
            }
            E value = pollFromQueue();
            if (value != null) {
                releaseSpaces(1);
                return value;
            }
            remainingNanos = deadline - System.nanoTime();
        }
        return null;
    }

    @Override
    public E poll() {
        if (!availableItems.tryAcquire()) {
            return null;
        }
        E value = pollFromQueue();
        if (value != null) {
            releaseSpaces(1);
            return value;
        }
        return null;
    }

    @Override
    public E peek() {
        return queue.peek();
    }

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        Objects.requireNonNull(c, "collection");
        if (c == this) {
            throw new IllegalArgumentException("Cannot drain to self");
        }
        if (maxElements <= 0) {
            return 0;
        }
        int count = 0;
        while (count < maxElements && availableItems.tryAcquire()) {
            E value = pollFromQueue();
            if (value == null) {
                break;
            }
            c.add(value);
            releaseSpaces(1);
            count++;
        }
        return count;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        boolean removed = queue.remove(o);
        if (removed) {
            availableItems.tryAcquire();
            releaseSpaces(1);
        }
        return removed;
    }

    @Override
    public void clear() {
        int removed = 0;
        while (queue.poll() != null) {
            removed++;
        }
        if (removed <= 0) {
            return;
        }
        for (int i = 0; i < removed; i++) {
            availableItems.tryAcquire();
        }
        releaseSpaces(removed);
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c, "collection");
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) {
                modified = true;
            } else {
                throw new IllegalStateException("Queue full");
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c, "collection");
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c, "collection");
        boolean modified = false;
        for (Iterator<E> it = queue.iterator(); it.hasNext(); ) {
            E value = it.next();
            if (!c.contains(value)) {
                if (queue.remove(value)) {
                    availableItems.tryAcquire();
                    releaseSpaces(1);
                    modified = true;
                }
            }
        }
        return modified;
    }

    @Override
    public E remove() {
        E value = poll();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    private void releaseSpaces(int count) {
        if (count <= 0) {
            return;
        }
        int current = availableSpaces.availablePermits();
        int maxRelease = capacity - current;
        if (maxRelease <= 0) {
            return;
        }
        int toRelease = Math.min(count, maxRelease);
        availableSpaces.release(toRelease);
    }

    @Override
    public E element() {
        E value = peek();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    private E pollFromQueue() {
        return queue.poll();
    }
}
