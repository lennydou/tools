package com.lendou.file.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.http.HttpClient;

/**
 * 文件片段的下载器
 */
public class AsyncFileSliceDownloader extends FileDownloader implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileDownloader.class);

    // 文件信息
    private FileInfo fileInfo;

    // 切片编号
    private int sliceId;

    // HttpClient
    private HttpClient httpClient;

    public AsyncFileSliceDownloader(FileInfo fileInfo, int sliceId, HttpClient httpClient) {
        this.fileInfo = fileInfo;
        this.sliceId = sliceId;
        this.httpClient = httpClient;
    }

    public void run() {
        final int maxRetry = 10;
        int i = 0;
        while (i++ < maxRetry && !downloadSlice(fileInfo, sliceId));
    }
}
