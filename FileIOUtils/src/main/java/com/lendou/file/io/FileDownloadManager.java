package com.lendou.file.io;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <b>文件下载工具类 - FileDownloadManager</b>
 */
public class FileDownloadManager {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FileDownloadManager.class);

    private static final Object LOCK = new Object();
    private static FileDownloadManager instance;

    public static FileDownloadManager getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new FileDownloadManager();
                    instance.init();
                }
            }
        }

        return instance;
    }

    private FileConfig fileConfig = FileConfig.DEFAULT;
    private RequestConfig reqConfig = RequestConfig.DEFAULT;

    private CloseableHttpClient httpClient;

    private void init() {
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        connMgr.setMaxTotal(fileConfig.getMaxTotalConnections());
        connMgr.setDefaultMaxPerRoute(fileConfig.getMaxPerRoute());

        httpClient = HttpClients.custom().setConnectionManager(connMgr).build();
    }

    public void setFileConfig(FileConfig config) {
        Validate.notNull(config);
        fileConfig = config;
    }

    public void setDefaultReqConfig(RequestConfig requestConfig) {
        reqConfig = requestConfig;
    }

    /**
     * 使用线程同步方式下载文件内容.<br/>
     * 下载的文件内容保存在 FileContext 中的 content 字段
     *
     * @param fileContext 文件上下文
     */
    public void download(FileContext fileContext) {
        Validate.notNull(fileContext);
        FileContentHelper.download(httpClient, fileConfig, reqConfig, fileContext);
    }

    /**
     * 关闭连接池
     */
    public void shutdown() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close http client", e);
            }
        }
    }
}