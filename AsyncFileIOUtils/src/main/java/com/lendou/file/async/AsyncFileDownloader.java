package com.lendou.file.async;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件异步下载类
 */
class AsyncFileDownloader extends FileDownloader implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileDownloader.class);

    // 文件信息
    private FileInfo fileInfo;

    private AsyncFileHandler fileHandler;

    public AsyncFileDownloader(FileInfo fileInfo, AsyncFileHandler fileHandler) {
        Validate.notNull(fileInfo);
        Validate.notNull(fileHandler);

        this.fileInfo = fileInfo;
        this.fileHandler = fileHandler;
    }

    public void run() {
        fileInfo.initContent();

        // 使用CountDownLatch分配多个线程, 同时工作来完成下载任务, 最后再调用fileHandler
        // 把其他几个任务给别人, 然后自己留一个任务

        // 调用fileHandler, 把线程内容传递出去
        fileHandler.handle(fileInfo);
    }
}
