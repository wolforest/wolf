
package cn.coderule.common.lang.concurrent.thread.local;

import java.util.Random;

public class ThreadLocalSequence {
    private final static int POSITIVE_MASK = 0x7FFFFFFF;

    private final ThreadLocal<Integer> index;
    private final Random random;

    public ThreadLocalSequence() {
        this.random = new Random();
        this.index = ThreadLocal.withInitial(() -> {
            int index = this.random.nextInt();
           return index & POSITIVE_MASK;
        });
    }

    public int incrementAndGet() {
        Integer index = this.index.get();
        if (null == index) {
            index = random.nextInt();
        }

        index = index & POSITIVE_MASK;
        this.index.set(++index);
        return index;
    }

    public void reset() {
        int index = Math.abs(random.nextInt(Integer.MAX_VALUE));
        this.index.set(index);
    }

    @Override
    public String toString() {
        return "ThreadLocalIndex{" +
            "threadLocalIndex=" + index.get() +
            '}';
    }
}
