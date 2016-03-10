package com.lendou.file.io;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件切片下载帮助类.<br/>
 *
 * Note: 该类没有状态, 是线程安全的
 */
class FileContentHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileContentHelper.class);

    private static final String HEADER_RANGE = "Range";

    /**
     * 下载文件切片内容, 使用断点续传方式
     *
     * @return 文件切片是否下载完成
     */
    public static void download(CloseableHttpClient httpClient,
                                FileConfig fileConfig,
                                RequestConfig reqConfig,
                                FileContext fileContext) {

        Content content = fileContext.isInMemory() ? new MemoryContent(fileContext) : new FileContent(fileContext);

        HttpGet httpGet = new HttpGet(fileContext.getUri());
        httpGet.setConfig(reqConfig);

        Stat.start();

        for (int i = 0; i < fileConfig.getMaxRetryCount(); i++) {

            // 记录重试的日志
            if (i > 0) {
                LOGGER.warn("Download file {}, with retry {}", fileContext.getUri());
                System.out.println(String.format("Download file %s, with retry %s", fileContext.getUri(), i));
            }

            downloadInternal(httpClient, httpGet, fileContext, content);

            // 如果下载完成, 直接跳出循环; 否则继续重试去下载
            if (fileContext.isCompleted()) {
                break;
            }
        }

        Stat.stop();
    }

    /**
     * 下载文件切片内容, 使用断点续传方式
     *
     * @return 文件切片是否下载完成
     */
    private static void downloadInternal(
            CloseableHttpClient httpClient,
            HttpGet httpGet,
            FileContext fileContext,
            Content content) {

        Validate.notNull(fileContext);
        Validate.notNull(httpClient);
        Validate.notNull(httpGet);
        Validate.notNull(content);

        // 计算本次需要下载的byte大小
        CloseableHttpResponse response = null;
        try {
            //设置请求和传输超时时间
            httpGet.setHeader(HEADER_RANGE, "bytes=" + fileContext.getOffset() + "-" + fileContext.getFileSize());
            System.out.println(String.format("Download file range from %d to %d", fileContext.getOffset(), fileContext.getFileSize()));

            // 拿到返回对象response, 并验证response的返回值
            response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status < HttpStatus.SC_OK || status >= HttpStatus.SC_MULTIPLE_CHOICES) {
                LOGGER.warn("Unexpected response status: {}", status);
                return;
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                // 返回http报文没有报文体
                return;
            }

            // 从response读取数据写入到data数组中
            final InputStream is = entity.getContent();
            try {
                content.read(is);
            } catch (Exception e) {
                LOGGER.error("Failed to download file {}", fileContext.getUri(), e);
                return;

            } finally {
                is.close();
            }

            // 到此处, 文件下载已经完成
            Validate.isTrue(fileContext.isCompleted());

            LOGGER.info("Success to download file slice {}, {}", fileContext.getUri(), fileContext.getFileSize());
            System.out.println(String.format("Success to download file slice %s, %s", fileContext.getUri(), fileContext.getFileSize()));

        } catch (Throwable e) {
            LOGGER.warn("Failed to download file slice {}, {}", fileContext.getUri(), fileContext.getFileSize(), e);
            System.out.println(String.format("Failed to download file slice %s, %s", fileContext.getUri(), fileContext.getFileSize()));

            // TODO: 仅仅用于测试
            e.printStackTrace();

        } finally {
            // 为了确保系统资源被正确地释放: 我们要么管理Http实体的内容流(InputStream), 要么关闭Http响应(httpResponse)
            // 关闭Http实体内容流和关闭Http响应的区别在于: 前者通过消耗掉Http实体内容来保持相关的http连接, 然而后者会立即关闭, 丢弃http连接
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close response", e);
                }
            }
        }
    }
}
