package com.lendou.file.async;

import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.concurrent.CountDownLatch;

/**
 * 从文件下载器能够支持线程, 异步执行
 */
public class AsyncFileSliceDownloader implements Runnable {

    private CloseableHttpClient httpClient;

    // 文件信息
    private FileContext fileContext;

    // 切片编号
    private int sliceId;

    private CountDownLatch latch;

    public AsyncFileSliceDownloader(FileContext fileContext, int sliceId, CloseableHttpClient httpClient, CountDownLatch latch) {
        Validate.notNull(fileContext);
        Validate.notNull(httpClient);
        Validate.notNull(latch);

        this.fileContext = fileContext;
        this.sliceId = sliceId;
        this.httpClient = httpClient;
        this.latch = latch;
    }

    public void run() {
        FileDownloadHelper.downloadSlice(fileContext, sliceId, httpClient);
        latch.countDown();
    }
}
