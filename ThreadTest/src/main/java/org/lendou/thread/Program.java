package org.lendou.thread;

/**
 * Hello world!
 *
 */
public class Program {
    public static void main( String[] args ) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            Thread.currentThread().sleep(1000);
        }

        ExecutorServiceTest.schedule(1000);

        for (int i = 0; i < 10; i++) {
            Thread.currentThread().sleep(1000);
        }

        ExecutorServiceTest.schedule(500);
    }
}
