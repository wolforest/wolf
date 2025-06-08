
package cn.coderule.common.util.lang;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureUtil {
    public static <T> CompletableFuture<T> append(
        CompletableFuture<T> future,
        CompletableFuture<T> nextFuture,
        ExecutorService executor
    ) {
        future.whenCompleteAsync((t, throwable) -> {
            if (throwable != null) {
                nextFuture.completeExceptionally(throwable);
            } else {
                nextFuture.complete(t);
            }
        }, executor);
        return nextFuture;
    }

    public static <T> CompletableFuture<T> append(CompletableFuture<T> future, ExecutorService executor) {
        return append(future, new CompletableFuture<>(), executor);
    }

    public static <T> CompletableFuture<T> complete(Throwable t) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(t);
        return future;
    }
}
