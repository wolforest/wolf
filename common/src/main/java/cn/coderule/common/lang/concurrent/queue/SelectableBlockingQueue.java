package cn.coderule.common.lang.concurrent.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SelectableBlockingQueue<E> implements java.util.concurrent.BlockingQueue<E> {
    public enum Type {
        ARRAY,
        LINKED,
        CONCURRENT_LINKED_LIST
    }

    private final java.util.concurrent.BlockingQueue<E> delegate;

    public SelectableBlockingQueue(Type type, int capacity) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        switch (type) {
            case ARRAY:
                if (capacity <= 0) {
                    throw new IllegalArgumentException("capacity must be positive for ARRAY");
                }
                this.delegate = new ArrayBlockingQueue<>(capacity);
                break;
            case LINKED:
                if (capacity <= 0) {
                    this.delegate = new LinkedBlockingQueue<>();
                } else {
                    this.delegate = new LinkedBlockingQueue<>(capacity);
                }
                break;
            case CONCURRENT_LINKED_LIST:
                if (capacity <= 0) {
                    capacity = Integer.MAX_VALUE;
                }
                this.delegate = new ConcurrentLinkedBlockingQueue<>(capacity);
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public SelectableBlockingQueue(Type type) {
        this(type, 0);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public int remainingCapacity() {
        return delegate.remainingCapacity();
    }

    @Override
    public boolean add(E e) {
        return delegate.add(e);
    }

    @Override
    public boolean offer(E e) {
        return delegate.offer(e);
    }

    @Override
    public void put(E e) throws InterruptedException {
        delegate.put(e);
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.offer(e, timeout, unit);
    }

    @Override
    public E take() throws InterruptedException {
        return delegate.take();
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.poll(timeout, unit);
    }

    @Override
    public E remove() {
        return delegate.remove();
    }

    @Override
    public E poll() {
        return delegate.poll();
    }

    @Override
    public E element() {
        return delegate.element();
    }

    @Override
    public E peek() {
        return delegate.peek();
    }

    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return delegate.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return delegate.drainTo(c, maxElements);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return delegate.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }
}
