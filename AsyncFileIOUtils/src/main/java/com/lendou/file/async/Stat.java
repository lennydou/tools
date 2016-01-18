package com.lendou.file.async;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 统计类
 */
public class Stat implements Runnable {
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> scheduledHandle = null;

    private static Stat stat = new Stat();

    private AtomicLong secCount = new AtomicLong(0L);
    private AtomicLong totalCount = new AtomicLong(0L);
    private long startTime;

    public static void start() {
        stat.startTime = System.currentTimeMillis();
        stat.secCount.set(0L);

        if (scheduledHandle != null) {
            scheduledHandle.cancel(false);
        }

        scheduledHandle = scheduledExecutorService.scheduleWithFixedDelay(
                stat,
                0,
                1000,
                TimeUnit.MILLISECONDS);
    }

    public static void stop() {
        scheduledHandle.cancel(false);
        scheduledExecutorService.shutdown();
    }

    public static void add(long count) {
        stat.totalCount.addAndGet(count);
        stat.secCount.addAndGet(count);
    }

    public void run() {
        long sec = (System.currentTimeMillis() - startTime) / 1000;
        if (sec < 1) {
            return;
        }

        long total = totalCount.get();
        System.out.println("download speed: " + (total / 1000 / sec) + " / " + total + " - " + (secCount.getAndSet(0L) / 1000) + " KB");
    }
}
