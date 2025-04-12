package cn.coderule.common.lang.concurrent.thread.pool;

import cn.coderule.common.lang.concurrent.thread.monitor.QueueSizeMonitor;
import cn.coderule.common.lang.concurrent.thread.monitor.ThreadPoolMonitor;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cn.coderule.common.util.lang.ThreadUtil;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ThreadPoolFactory {
    private static final List<ThreadPoolWrapper> MONITOR_EXECUTOR = new CopyOnWriteArrayList<>();
    private static final ScheduledExecutorService MONITOR_SCHEDULED =
        ThreadUtil.newSingleScheduledThreadExecutor(
            new ThreadFactoryBuilder().setNameFormat("ThreadPoolFactory-%d").build()
        );

    private static volatile long monitorInterval = TimeUnit.SECONDS.toMillis(3);
    private static volatile boolean enableLogJstack = true;
    private static volatile long logInterval = 60000;
    private static volatile long lastLogTime = System.currentTimeMillis();

    public static ThreadPoolExecutor create(int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        String name,
        int queueCapacity) {
        return create(corePoolSize, maximumPoolSize, keepAliveTime, unit, name, queueCapacity, Collections.emptyList());
    }

    public static ThreadPoolExecutor create(int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        String name,
        int queueCapacity,
        ThreadPoolMonitor... ThreadPoolMonitors) {
        return create(corePoolSize, maximumPoolSize, keepAliveTime, unit, name, queueCapacity,
            Lists.newArrayList(ThreadPoolMonitors));
    }

    public static ThreadPoolExecutor create(int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        String name,
        int queueCapacity,
        List<ThreadPoolMonitor> ThreadPoolMonitors) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) ThreadUtil.newThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            new LinkedBlockingQueue<>(queueCapacity),
            new ThreadFactoryBuilder().setNameFormat(name + "-%d").build(),
            new ThreadPoolExecutor.DiscardOldestPolicy());
        List<ThreadPoolMonitor> printers = Lists.newArrayList(new QueueSizeMonitor(queueCapacity));
        printers.addAll(ThreadPoolMonitors);

        MONITOR_EXECUTOR.add(ThreadPoolWrapper.builder()
            .name(name)
            .executor(executor)
            .monitors(printers)
            .build());
        return executor;
    }

    public static void logStatus() {
        for (ThreadPoolWrapper threadPoolWrapper : MONITOR_EXECUTOR) {
            logStatus(threadPoolWrapper);
        }
    }

    public static void logStatus(ThreadPoolWrapper threadPoolWrapper) {
        List<ThreadPoolMonitor> monitors = threadPoolWrapper.getMonitors();
        for (ThreadPoolMonitor monitor : monitors) {
            double value = monitor.calculateMetric(threadPoolWrapper.getExecutor());
            log.info("\t{}\t{}\t{}", threadPoolWrapper.getName(), monitor.getDescription(), value);

            if (!enableLogJstack) {
                continue;
            }

            if (monitor.shouldLogJstack(threadPoolWrapper.getExecutor(), value)
                && System.currentTimeMillis() - lastLogTime > logInterval) {
                lastLogTime = System.currentTimeMillis();
                log.warn("jstack start\n{}", ThreadUtil.jstack());
            }
        }
    }

    public static void start() {
        MONITOR_SCHEDULED.scheduleAtFixedRate(
            ThreadPoolFactory::logStatus,
            20,
            monitorInterval,
            TimeUnit.MILLISECONDS
        );
    }

    public static void shutdown() {
        MONITOR_SCHEDULED.shutdown();
    }
}
