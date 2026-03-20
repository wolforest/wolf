package cn.coderule.common.lang.concurrent.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public final class SelectableBlockingQueueBenchmark {
    private static final int DEFAULT_WARMUP_ROUNDS = 2;
    private static final int DEFAULT_MEASURE_ROUNDS = 3;
    private static final int DEFAULT_OPS_PER_PRODUCER = 1_000_000;
    private static final int DEFAULT_PRODUCERS = 5;
    private static final int DEFAULT_CONSUMERS = 10;
    private static final int DEFAULT_CAPACITY = 1 << 20;

    public static void main(String[] args) throws Exception {
        int warmupRounds = intArg(args, 0, DEFAULT_WARMUP_ROUNDS);
        int measureRounds = intArg(args, 1, DEFAULT_MEASURE_ROUNDS);
        int producers = intArg(args, 2, DEFAULT_PRODUCERS);
        int consumers = intArg(args, 3, DEFAULT_CONSUMERS);
        int opsPerProducer = intArg(args, 4, DEFAULT_OPS_PER_PRODUCER);
        int capacity = intArg(args, 5, DEFAULT_CAPACITY);

        System.out.println("SelectableBlockingQueue benchmark");
        System.out.println("warmupRounds=" + warmupRounds
                + ", measureRounds=" + measureRounds
                + ", producers=" + producers
                + ", consumers=" + consumers
                + ", opsPerProducer=" + opsPerProducer
                + ", capacity=" + capacity);
        System.out.println();

        for (SelectableBlockingQueue.Type type : SelectableBlockingQueue.Type.values()) {
            System.out.println("Type=" + type);
            runSingleThread(type, warmupRounds, measureRounds, opsPerProducer, capacity);
            System.out.println();
            runMpmc(type, warmupRounds, measureRounds, producers, consumers, opsPerProducer, capacity);
            System.out.println();
        }
    }

    private static void runSingleThread(SelectableBlockingQueue.Type type, int warmupRounds,
                                        int measureRounds, int ops, int capacity) {
        for (int i = 0; i < warmupRounds; i++) {
            singleThreadOnce(type, ops, capacity);
        }

        long best = Long.MAX_VALUE;
        for (int i = 0; i < measureRounds; i++) {
            long nanos = singleThreadOnce(type, ops, capacity);
            best = Math.min(best, nanos);
            printResult(type, "single-thread", ops, nanos);
        }
        printBest(type, "single-thread", ops, best);
    }

    private static long singleThreadOnce(SelectableBlockingQueue.Type type, int ops, int capacity) {
        SelectableBlockingQueue<Integer> queue = new SelectableBlockingQueue<>(type, capacity);
        long start = System.nanoTime();
        for (int i = 0; i < ops; i++) {
            queue.offer(i);
            Integer v = queue.poll();
            if (v == null) {
                throw new IllegalStateException("poll returned null");
            }
        }
        return System.nanoTime() - start;
    }

    private static void runMpmc(SelectableBlockingQueue.Type type, int warmupRounds,
                               int measureRounds, int producers, int consumers,
                               int opsPerProducer, int capacity) throws Exception {
        for (int i = 0; i < warmupRounds; i++) {
            mpmcOnce(type, producers, consumers, opsPerProducer, capacity, true);
        }

        long best = Long.MAX_VALUE;
        long totalOps = (long) producers * opsPerProducer;
        for (int i = 0; i < measureRounds; i++) {
            long nanos = mpmcOnce(type, producers, consumers, opsPerProducer, capacity, false);
            best = Math.min(best, nanos);
            printResult(type, "mpmc", totalOps, nanos);
        }
        printBest(type, "mpmc", totalOps, best);
    }

    private static long mpmcOnce(SelectableBlockingQueue.Type type, int producers, int consumers,
                                int opsPerProducer, int capacity, boolean warmup) throws Exception {
        SelectableBlockingQueue<Integer> queue = new SelectableBlockingQueue<>(type, capacity);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(producers + consumers);
        AtomicLong remaining = new AtomicLong((long) producers * opsPerProducer);

        for (int i = 0; i < producers; i++) {
            Thread t = new Thread(() -> {
                await(start);
                for (int j = 0; j < opsPerProducer; j++) {
                    while (!queue.offer(j)) {
                        LockSupport.parkNanos(1_000);
                    }
                }
                done.countDown();
            }, "sbq-producer-" + type + "-" + i);
            t.setDaemon(true);
            t.start();
        }

        for (int i = 0; i < consumers; i++) {
            Thread t = new Thread(() -> {
                await(start);
                long spins = 0;
                while (remaining.get() > 0) {
                    Integer v = queue.poll();
                    if (v != null) {
                        remaining.decrementAndGet();
                        spins = 0;
                    } else {
                        spins++;
                        if ((spins & 0x3FF) == 0) {
                            LockSupport.parkNanos(1_000);
                        }
                    }
                }
                done.countDown();
            }, "sbq-consumer-" + type + "-" + i);
            t.setDaemon(true);
            t.start();
        }

        long startNs = System.nanoTime();
        start.countDown();
        done.await();
        long nanos = System.nanoTime() - startNs;

        if (!warmup && remaining.get() != 0) {
            throw new IllegalStateException("remaining=" + remaining.get());
        }
        return nanos;
    }

    private static void printResult(SelectableBlockingQueue.Type type, String name, long ops, long nanos) {
        double seconds = nanos / 1_000_000_000.0;
        double opsPerSec = ops / seconds;
        System.out.printf("[%s] %s: %,d ops in %.3f s (%.2f ops/s)%n",
                type, name, ops, seconds, opsPerSec);
    }

    private static void printBest(SelectableBlockingQueue.Type type, String name, long ops, long nanos) {
        double seconds = nanos / 1_000_000_000.0;
        double opsPerSec = ops / seconds;
        System.out.printf("[%s] %s best: %,d ops in %.3f s (%.2f ops/s)%n",
                type, name, ops, seconds, opsPerSec);
    }

    private static int intArg(String[] args, int index, int defaultValue) {
        if (args.length <= index) {
            return defaultValue;
        }
        return Integer.parseInt(args[index]);
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
