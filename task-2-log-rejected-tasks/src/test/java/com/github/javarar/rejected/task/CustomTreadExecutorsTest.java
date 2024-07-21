package com.github.javarar.rejected.task;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class CustomTreadExecutorsTest {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Test
    void threadPoolDoesNotThrowExceptionOnQueueOverflow() throws InterruptedException {
        final int corePoolSize = 1;
        final int maximumPoolSize = 1;
        final long keepAliveTime = 0L;
        final TimeUnit unit = TimeUnit.SECONDS;
        final int workQueueSize = 1;

        ThreadPoolExecutor executor = (ThreadPoolExecutor) CustomThreadExecutors.logRejectedThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueueSize
        );
        for (int i = 0; i <= 10; i++) {
            executor.submit(this::increment);

        }
        executor.shutdown();
        executor.awaitTermination(1L, unit);

        assertAll(
                () -> assertEquals(1, executor.getCompletedTaskCount()),
                () -> assertEquals(1, counter.get())
        );
    }

    private void increment() {
        try {
            Thread.sleep(1_000);
            counter.incrementAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
