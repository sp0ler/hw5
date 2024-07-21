package com.github.javarar.rejected.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomThreadExecutors {

    public static Executor logRejectedThreadPoolExecutor(
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            int workQueueSize
    ) {
        AtomicInteger counterTasks = new AtomicInteger(1);
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(workQueueSize, true);
        RejectedExecutionHandler myHandler = (runnable, executor) -> {
            try {
                if (executor.getQueue().size() > workQueueSize) {
                    executor.getQueue().put(runnable);
                } else {
                    log.error("Невозможно добавить задачу на исполнение №{}", counterTasks.getAndIncrement());
                }
            } catch (Exception ignored) {}
        };

        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), myHandler
        );
    }
}
