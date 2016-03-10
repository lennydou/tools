package com.lendou.file.async;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * 文件切片下载帮助类.<br/>
 *
 * Note: 该类没有状态, 是线程安全的
 */
class FileDownloadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadHelper.class);

    /**
     * 下载文件切片内容, 使用断点续传方式
     *
     * @return 文件切片是否下载完成
     */
    public static void downloadSlice(FileContext fileContext, int sliceId, CloseableHttpClient httpClient) {

        for (int i = 0; i < FileConfig.getFileConfig().getMaxRetryCount(); i++) {
            if (fileContext.isFailed()) {
                LOGGER.error("File {} is failed, will skip download for slice {}", fileContext.getUri(), sliceId);
                System.out.println(String.format("File %s is failed, will skip download for slice %s", fileContext.getUri(), sliceId));
                break;
            }

            // 记录重试的日志
            if (i > 0) {
                LOGGER.warn("Download file {} slice {}, with retry {}", fileContext.getUri(), sliceId);
                System.out.println(String.format("Download file %s slice %s, with retry %s", fileContext.getUri(), sliceId, i));
            }

            // 如果下载完成, 直接跳出循环; 否则继续重试去下载
            if (downloadSliceInternal(fileContext, sliceId, httpClient)) {
                break;
            }
        }
    }

    /**
     * 下载文件切片内容, 使用断点续传方式
     *
     * @return 文件切片是否下载完成
     */
    private static boolean downloadSliceInternal(FileContext fileContext, int sliceId, CloseableHttpClient httpClient) {
        final AsyncFileSliceInfo sliceInfo = fileContext.getSliceInfo(sliceId);
        if (fileContext.isFailed()) {
            return sliceInfo.isCompleted();
        }

        // 计算本次需要下载的byte大小
        CloseableHttpResponse response = null;
        BufferedInputStream bis = null;
        try {
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();

            HttpGet httpGet = new HttpGet(fileContext.getUri());
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Range", "bytes=" + sliceInfo.getCur() + "-" + sliceInfo.getRight());
            System.out.println(String.format("Download file range from %d to %d", sliceInfo.getCur(), sliceInfo.getRight()));

            response = httpClient.execute(httpGet, new BasicHttpContext());
            bis = new BufferedInputStream(response.getEntity().getContent());

            // 从response读取数据写入到data数组中
            // Note: 这里会有多个线程同时写入data数组, 但是每个线程写入到data数组不同的区段, 所以这里不需要加锁.
            final byte[] data = fileContext.getContent();
            final int length = sliceInfo.getRight();
            int offset = sliceInfo.getCur();

            int bytesRead;
            while ((bytesRead = bis.read(data, offset, length - offset)) != -1) {
                offset += bytesRead;
                sliceInfo.setCur(offset);
                Stat.add(bytesRead);
                if (offset == sliceInfo.getRight()) {
                    break;
                }
            }

            Validate.isTrue(sliceInfo.isCompleted());
            LOGGER.info("Success to download file slice {}, {}-{}", fileContext.getFileId(), sliceInfo.getLeft(), sliceInfo.getRight());
            System.out.println(String.format("Success to download file slice %s, %s-%s", fileContext.getFileId(), sliceInfo.getLeft(), sliceInfo.getRight()));

        } catch (Throwable e) {
            LOGGER.warn("Failed to download file slice {}, {}-{}", fileContext.getFileId(), sliceInfo.getLeft(), sliceInfo.getRight(), e);
            System.out.println(String.format("Failed to download file slice %s, %s-%s", fileContext.getFileId(), sliceInfo.getLeft(), sliceInfo.getRight()));
            e.printStackTrace();

        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close BufferedInputStream", e);
                }
            }

            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close response", e);
                }
            }
        }

        return sliceInfo.isCompleted();
    }
}
