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
    private FileInfo fileInfo;

    // 切片编号
    private int sliceId;

    private CountDownLatch latch;

    public AsyncFileSliceDownloader(FileInfo fileInfo, int sliceId, CloseableHttpClient httpClient, CountDownLatch latch) {
        Validate.notNull(fileInfo);
        Validate.notNull(httpClient);
        Validate.notNull(latch);

        this.fileInfo = fileInfo;
        this.sliceId = sliceId;
        this.httpClient = httpClient;
        this.latch = latch;
    }

    public void run() {
        FileDownloadHelper.downloadSlice(fileInfo, sliceId, httpClient);
        latch.countDown();
    }
}
