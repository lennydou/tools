package com.lendou.file.async;

import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 文件异步下载类
 */
class AsyncFileDownloader implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileDownloader.class);

    // 所有AsyncFileDownloader都共享同一个ExecutorService实例
    // TODO: 线程池的size设置成100
    private static final ExecutorService executorService = Executors.newFixedThreadPool(100);

    // 文件信息
    private FileInfo fileInfo;

    private AsyncFileHandler fileHandler;

    private AsyncFileDownloader(FileInfo fileInfo, AsyncFileHandler fileHandler) {
        Validate.notNull(fileInfo);
        Validate.notNull(fileHandler);

        this.fileInfo = fileInfo;
        this.fileHandler = fileHandler;
    }

    /**
     * 下载文件的单独线程. 该线程会协同其他线程下载FileInfo中的整个文件. <br/>
     * 该方法使用CountDownLatch分配多个线程, 同时工作来完成下载任务, 最后再调用fileHandler把下载结果返回给调用者. <br/>
     */
    public void run() {
        fileInfo.initContent();

        // 使用CountDownLatch分配多个线程, 同时工作来完成下载任务, 最后再调用fileHandler
        // 把其他几个任务给别人, 然后自己留一个任务

        // 创建一个闭锁, 使用的初始化数量是slice数量 - 1, 因为第一个slice由当前线程负责下载, 把其他切片交给其他线程来下载
        CountDownLatch latch = new CountDownLatch(fileInfo.getSliceCount() - 1);
        for (int i = 1; i < fileInfo.getSliceCount(); i++) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            executorService.execute(new AsyncFileSliceDownloader(fileInfo, i, httpClient, latch));
        }

        // 当前线程下载第一个切片
        FileDownloadHelper.downloadSlice(fileInfo, 0, HttpClients.createDefault());

        try {
            latch.await(FileConfig.getFileConfig().getTimeout(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Failed to download file {}", fileInfo.getUri());
        }

        System.out.println("------------------------ call handle .....................");

        // 调用fileHandler, 把线程内容传递出去
        fileHandler.handle(fileInfo);
    }

    /**
     * 异步下载文件
     *
     * @param fileInfo 待下载文件的信息
     * @param fileHandler 文件下载完成后的处理方法
     */
    public static void download(FileInfo fileInfo, AsyncFileHandler fileHandler) {
        Validate.notNull(fileInfo);
        Validate.notNull(fileHandler);

        executorService.execute(new AsyncFileDownloader(fileInfo, fileHandler));
    }

    /**
     * 关闭executorService
     */
    public static void shutdown() {
        executorService.shutdown();
    }
}
