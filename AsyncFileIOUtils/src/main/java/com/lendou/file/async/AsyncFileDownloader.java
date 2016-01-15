package com.lendou.file.async;

import sun.net.www.http.HttpClient;

/**
 * 文件异步下载类
 */
class AsyncFileDownloader implements Runnable {

    // 文件信息
    private FileInfo fileInfo;

    // 切片编号
    private int sliceId;

    // 文件内容
    private byte[] data;

    // HttpClient
    private HttpClient httpClient;

    public AsyncFileDownloader(FileInfo fileInfo, int sliceId, byte[] data, HttpClient httpClient) {
        this.fileInfo = fileInfo;
        this.sliceId = sliceId;
        this.data = data;
        this.httpClient = httpClient;
    }

    public void run() {
        AsyncFileSliceInfo sliceInfo = fileInfo
    }
}
