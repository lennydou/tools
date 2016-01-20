package com.lendou.file.async;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <b>文件异步下载工具类 - AsyncFileIOUtils</b>
 *
 * <p>提供文件异步下载-回调的接口</p>
 */
public class AsyncFileIOUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileIOUtils.class);

    /**
     * 下载文件内容.<br/>
     * 文件下载完成后, 该方法会调用{@link AsyncFileHandler}的handle方法.
     *
     * @param fileContext 文件内容
     * @param fileHandler 文件下载完成后的回调方法
     */
    public static void download(FileContext fileContext, AsyncFileHandler fileHandler) {
        Validate.notNull(fileContext);
        Validate.notNull(fileHandler);

        AsyncFileDownloader.download(fileContext, fileHandler);
    }

    public static int getFileSize(String uri) {
        Validate.notEmpty(uri);

        int fileSize = 0;

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpHead httpGet = new HttpHead(uri);

            // Create a custom response handler
            final ResponseHandler<Integer> responseHandler = new ResponseHandler<Integer>() {
                public Integer handleResponse(HttpResponse httpResponse) throws IOException {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    if (status < 200 || status >= 300) {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }

                    Validate.isTrue(httpResponse.getHeaders("Content-Length").length > 0);
                    return NumberUtils.toInt(httpResponse.getHeaders("Content-Length")[0].getValue(), 0);
                }
            };

            fileSize = httpClient.execute(httpGet, responseHandler);

        } catch (Throwable e) {
            LOGGER.error("Failed to get file size for uri: {}", uri, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close http client", e);
            }
        }

        return fileSize;
    }

    /**
     * 关闭AsyncFileIOUtils的请求
     */
    public static void shutdown() {
        AsyncFileDownloader.shutdown();
    }
}
