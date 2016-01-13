package org.lendou.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTest implements Runnable {

    private static volatile ExecutorServiceTest instance = new ExecutorServiceTest();

    private static ScheduledFuture<?> handle = null;

    public static ExecutorServiceTest getInstance() {
        return instance;
    }

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public static void schedule(long interval) {
        if (handle != null) {
            handle.cancel(false);
        }

        handle =  scheduledExecutorService.scheduleWithFixedDelay(
                ExecutorServiceTest.getInstance(),
                0,
                interval,
                TimeUnit.MILLISECONDS);
    }

    public void run() {
        System.out.println("b - " + Thread.currentThread().getId() + " - " + TimeHelper.getCurTime());
    }
}
