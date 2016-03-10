package com.lendou.file.io;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>文件下载工具类 - FileIOUtils</b>
 */
public class FileDownloadManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadManager.class);

    private static FileConfig fileConfig = FileConfig.DEFAULT;
    private static RequestConfig reqConfig = RequestConfig.DEFAULT;

    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(fileConfig.getMaxTotalConnections());
        cm.setDefaultMaxPerRoute(fileConfig.getMaxPerRoute());

        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }

    public static void setFileConfig(FileConfig config) {
        Validate.notNull(config);
        fileConfig = config;
    }

    public static void setDefaultReqConfig(RequestConfig requestConfig) {
        reqConfig = requestConfig;
    }

    /**
     * 使用线程同步方式下载文件内容.<br/>
     * 下载的文件内容保存在 FileContext 中的 content 字段
     *
     * @param fileContext 文件上下文
     */
    public static void download(FileContext fileContext) {
        Validate.notNull(fileContext);

        FileContentHelper.download(httpClient, fileConfig, reqConfig, fileContext);
    }
}
