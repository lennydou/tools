package com.lendou.file.io;

import org.apache.commons.lang3.Validate;

/**
 * 文件配置信息
 */
public class FileConfig {

    /**
     * 当前的FileConfig实例
     */
    public static FileConfig DEFAULT = new Builder().build();

    private int maxRetryCount;
    private int maxFileSizeInMemory;

    private int socketTimeout;
    private int connectTimeout;
    private int connectionRequestTimeout;
    private int maxTotalConnections;
    private int maxPerRoute;

    /**
     * 禁用外部通过构造器构造实例
     */
    private FileConfig(Builder builder) {
        Validate.notNull(builder);

        this.maxRetryCount = builder.maxRetryCount;
        this.socketTimeout = builder.socketTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.connectionRequestTimeout = builder.connectionRequestTimeout;
        this.maxTotalConnections = builder.maxTotalConnections;
        this.maxPerRoute = builder.maxPerRoute;
        this.maxFileSizeInMemory = builder.maxFileSizeInMemory;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public int getMaxFileSizeInMemory() {
        return maxFileSizeInMemory;
    }

    /**
     * 获得FileDownloadConfig实例构造器
     *
     * @return FileDownloadConfig实例构造器
     */
    public static Builder custom() {
        return new Builder();
    }

    public static class Builder {
        private int maxRetryCount = 100;

        private int socketTimeout = 5000;
        private int connectTimeout = 5000;
        private int connectionRequestTimeout = 5000;

        private int maxTotalConnections = 100;
        private int maxPerRoute = 20;

        private int maxFileSizeInMemory = 4 * 1024 * 1024;

        private Builder(){}

        public Builder setMaxRetryCount(int maxRetryCount) {
            this.maxRetryCount = maxRetryCount;
            return this;
        }

        public Builder setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public Builder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setConnectionRequestTimeout(int connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }

        public void setMaxTotalConnections(int maxTotalConnections) {
            this.maxTotalConnections = maxTotalConnections;
        }

        public void setMaxPerRoute(int maxPerRoute) {
            this.maxPerRoute = maxPerRoute;
        }

        public void setMaxFileSizeInMemory(int maxFileSizeInMemory) {
            this.maxFileSizeInMemory = maxFileSizeInMemory;
        }

        /**
         * 构造FileConfig实例
         *
         * @return FileConfig实例sa
         */
        public FileConfig build() {
            return new FileConfig(this);
        }
    }
}
