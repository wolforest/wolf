package cn.coderule.common.lang.concurrent.thread;

import cn.coderule.common.lang.concurrent.sync.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ServiceThread implements Runnable {
    private static final long JOIN_TIME = 90 * 1000;

    protected Thread thread;
    protected final CountDownLatch waitPoint = new CountDownLatch(1);
    protected volatile AtomicBoolean hasNotified = new AtomicBoolean(false);
    @Getter
    protected volatile boolean stopped = false;
    @Getter @Setter
    protected boolean isDaemon = false;

    //Make it able to restart the thread
    private final AtomicBoolean started = new AtomicBoolean(false);

    public ServiceThread() {

    }

    public abstract String getServiceName();

    public void start() {
        log.info("Try to start service thread:{} started:{} lastThread:{}", getServiceName(), started.get(), thread);
        if (!started.compareAndSet(false, true)) {
            return;
        }

        stopped = false;
        this.thread = new Thread(this, getServiceName());
        this.thread.setDaemon(isDaemon);

        this.thread.start();
        log.info("Start service thread:{} started:{} lastThread:{}", getServiceName(), started.get(), thread);
    }

    public void shutdown() {
        this.shutdown(false);
    }

    public void shutdown(final boolean interrupt) {
        log.info("Try to shutdown service thread:{} started:{} lastThread:{}", getServiceName(), started.get(), thread);
        if (!started.compareAndSet(true, false)) {
            return;
        }

        this.stopped = true;
        log.info("shutdown thread[{}] interrupt={} ", getServiceName(), interrupt);

        //if thead is waiting, wakeup it
        wakeup();

        try {
            if (interrupt) {
                this.thread.interrupt();
            }

            long beginTime = System.currentTimeMillis();
            if (!this.thread.isDaemon()) {
                this.thread.join(getJoinTime());
            }
            long elapsedTime = System.currentTimeMillis() - beginTime;
            log.info("join thread[{}], elapsed time: {}ms, join time:{}ms", getServiceName(), elapsedTime, JOIN_TIME);
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
        }
    }

    public void stop() {
        if (!started.get()) {
            return;
        }
        this.stopped = true;
        log.info("stop thread[{}] ", this.getServiceName());
    }

    public void wakeup() {
        if (hasNotified.compareAndSet(false, true)) {
            waitPoint.countDown(); // notify
        }
    }

    protected void await(long interval) {
        if (hasNotified.compareAndSet(true, false)) {
            this.postAwait();
            return;
        }

        //entry to wait
        waitPoint.reset();

        try {
            waitPoint.await(interval, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
        } finally {
            hasNotified.set(false);
            this.postAwait();
        }
    }

    protected void postAwait() {
    }

    public long getJoinTime() {
        return JOIN_TIME;
    }
}
