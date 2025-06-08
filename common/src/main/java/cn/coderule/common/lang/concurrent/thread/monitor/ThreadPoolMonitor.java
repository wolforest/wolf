
package cn.coderule.common.lang.concurrent.thread.monitor;

import java.util.concurrent.ThreadPoolExecutor;

public interface ThreadPoolMonitor {

    String getDescription();

    double calculateMetric(ThreadPoolExecutor executor);

    boolean shouldLogJstack(ThreadPoolExecutor executor, double value);
}
