
package cn.coderule.common.lang.concurrent.thread.monitor;

import java.util.concurrent.ThreadPoolExecutor;

public class QueueSizeMonitor implements ThreadPoolMonitor {

    private final int maxQueueCapacity;

    public QueueSizeMonitor(int maxQueueCapacity) {
        this.maxQueueCapacity = maxQueueCapacity;
    }

    @Override
    public String getDescription() {
        return "queueSize";
    }

    @Override
    public double calculateMetric(ThreadPoolExecutor executor) {
        return executor.getQueue().size();
    }

    @Override
    public boolean shouldLogJstack(ThreadPoolExecutor executor, double value) {
        return value > maxQueueCapacity * 0.85;
    }
}
