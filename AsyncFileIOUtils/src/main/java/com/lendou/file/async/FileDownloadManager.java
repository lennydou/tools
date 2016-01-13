package com.lendou.file.async;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 文件下载管理器
 */
public class FileDownloadManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadManager.class);

    /**
     * 文件下载的配置参数类
     */
    private static FileDownloadConfig config = FileDownloadConfig.createDefault();

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static byte[] download(String downloadUri, String stoid, int blockIndex, String sha1, int fileSize) {
        Validate.notEmpty(downloadUri);
        Validate.notEmpty(stoid);
        Validate.notEmpty(sha1);
        Validate.isTrue(blockIndex >= 0 && fileSize > 0);

        // 构造文件下载对象
        String fileId = FileInfoUtils.generateFileId(stoid, blockIndex);
        FileInfo fileInfo = FileInfoHolder.getFileInfo(fileId);
        if (fileInfo == null) {
            fileInfo = new FileInfo(downloadUri, fileId, FileInfoUtils.generateFileName(sha1), fileSize, 0);
            FileInfoHolder.addFileInfo(fileInfo);
        }

        executorService.execute(new FileDownloader(fileInfo));

        return null;
    }

    public static byte[] download(String fileUri, int fileSize) throws URISyntaxException, IOException {
        byte[] data = new byte[fileSize];

        Pair<Integer, Integer> sliceInfo = getFileSliceInfo(fileSize);
        CountDownLatch countDownLatch = new CountDownLatch(sliceInfo.getLeft() + (sliceInfo.getRight() == 0 ? 0 : 1));

        int offset = 0;
        for (int i = 0; i < sliceInfo.getLeft(); i++) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
//            FileDownloader downloader = new FileDownloader(fileUri, data, offset, config.getUnitSize(), countDownLatch, httpClient);
//            executorService.execute(downloader);

            offset += config.getUnitSize();
        }

        if (sliceInfo.getRight() > 0) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
//            FileDownloader downloader = new FileDownloader(fileUri, data, offset, sliceInfo.getRight(), countDownLatch, httpClient);
//            executorService.execute(downloader);
        }

        try {
            countDownLatch.await(config.getTimeout(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Failed to download file {}", fileUri);
        }

        FileUtils.writeByteArrayToFile(new File("/home/dyl/temp/test1.jpg"), data, true);
        System.out.println("Success to download file " + fileUri);
        return data;
    }

    private static Pair<Integer, Integer> getFileSliceInfo(int fileSize) {
        Validate.notNull(fileSize > 0);

        final int unitSize = config.getUnitSize();

        int sliceCount = fileSize / unitSize;
        int surplusSize = fileSize % unitSize;
        if (surplusSize < FileDownloadConfig.getMinUnitSize()) {
            --sliceCount;
            surplusSize += unitSize;
        }

        return Pair.of(sliceCount, surplusSize);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.exit(0);
    }
}
