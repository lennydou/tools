package com.lendou.file.async;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * 远程文件切片下载器
 */
public class FileDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloader.class);

    protected boolean downloadSlice(FileInfo fileInfo, int sliceId) {

        // 计算本次需要下载的byte大小
        final AsyncFileSliceInfo sliceInfo = fileInfo.getSliceInfo(sliceId);
        BufferedInputStream bis = null;
        try {
            HttpGet httpGet = new HttpGet(fileInfo.getUri());
            httpGet.addHeader("Range", "bytes=" + sliceInfo.getCur() + "-" + sliceInfo.getRight());

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpGet, new BasicHttpContext());
            bis = new BufferedInputStream(response.getEntity().getContent());

            // 从response读取数据写入到data数组中
            // Note: 这里会有多个线程同时写入data数组, 但是每个线程写入到data数组不同的区段, 所以这里不需要加锁.
            final byte[] data = fileInfo.getContent();
            final int length = sliceInfo.getRight() - sliceInfo.getCur();
            int offset = sliceInfo.getCur();

            int bytesRead;
            while ((bytesRead = bis.read(data, offset, length - offset)) != -1) {
                offset += bytesRead;
                sliceInfo.setCur(offset);
                if (offset == sliceInfo.getRight()) {
                    break;
                }
            }

            Validate.isTrue(offset == sliceInfo.getRight());

            LOGGER.info("Success to download file slice {}, {}-{}", fileInfo.getFileId(), sliceInfo.getLeft(), sliceInfo.getRight());
            return true;

        } catch (Throwable e) {
            LOGGER.error("Failed to download file slice {}, {}-{}", fileInfo.getFileId(), sliceInfo.getLeft(), sliceInfo.getRight(), e);
            return false;

        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close BufferedInputStream", e);
                }
            }
        }
    }
}
