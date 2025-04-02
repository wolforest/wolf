package com.wolf.mqclient.vendor.rocketmq5;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wolf.mqclient.core.MQClientManager;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientServiceProvider;

/**
 * @author weixing
 * @since 2023/6/12 15:12
 */
@Slf4j
public class Rocket5ClientManager implements MQClientManager {

    private static volatile Rocket5ClientManager INSTANCE;

    @Getter
    private final ClientServiceProvider clientServiceProvider;

    private final ExecutorService sendCallbackExecutor;

    private Rocket5ClientManager() {
        clientServiceProvider = ClientServiceProvider.loadService();
        sendCallbackExecutor = createSendCallbackExecutor();
    }

    private ExecutorService createSendCallbackExecutor() {
        BlockingQueue<Runnable> sendCallbackThreadPoolQueue = new LinkedBlockingQueue<Runnable>(50000);
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("rmq5-callback-%d")
            .setUncaughtExceptionHandler(((t, e) -> {
                log.error("[MQ] {} has uncaught exception: {}", t.getName(), e.getMessage(), e);
            }))
            .build();

        ExecutorService sendCallbackExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(),
            60,
            TimeUnit.SECONDS,
            sendCallbackThreadPoolQueue,
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("[MQ] sendCallbackExecutor is going to shutdown.");
            sendCallbackExecutor.shutdown();
            try {
                if (!sendCallbackExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    sendCallbackExecutor.shutdownNow();
                    if (!sendCallbackExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                        log.error("[MQ] sendCallbackExecutor did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                sendCallbackExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        return sendCallbackExecutor;
    }

    public static Rocket5ClientManager getInstance() {
        if (INSTANCE == null) {
            synchronized (Rocket5ClientManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new Rocket5ClientManager();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void shutdown() {

    }

    public ExecutorService getSendCallbackExecutor() {
        return sendCallbackExecutor;
    }
}
