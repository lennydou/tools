package com.lendou.file.async;

import org.apache.commons.lang3.Validate;

/**
 * 文件下载配置文件
 */
public class FileDownloadConfig {

    /**
     * 每次http请求的文件slice的最小size.
     *
     * 当对文件切片时, 如果最后一个slice的size小于MIN_UNIT_SIZE, 则把该最后一个slice和倒数第二个slice合并作为同一个slice.
     */
    private static final int MIN_UNIT_SIZE = 1024;

    /**
     * 文件切片后每个slice的大小
     */
    private int unitSize = 16 * 1024;

    /**
     * 文件下载的timeout时间, 该timeout时间是整个文件所有切片下载的timeout时间
     */
    private int timeout = 5 * 60;

    /**
     * 是否使用slice的方式下载文件, 默认是false
     */
    private boolean useFileSliceDownloader = false;

    /**
     * 获得每次http请求的文件slice的最小size.
     *
     * 当对文件切片时, 如果最后一个slice的size小于MIN_UNIT_SIZE, 则把该最后一个slice和倒数第二个slice合并作为同一个slice.
     */
    public static int getMinUnitSize() {
        return MIN_UNIT_SIZE;
    }

    /**
     * 获得文件切片后每个slice的大小
     */
    public int getUnitSize() {
        return unitSize;
    }

    /**
     * 获得文件下载的timeout时间, 该timeout时间是整个文件所有切片下载的timeout时间
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * 是否使用slice的方式下载文件, 默认是false
     */
    public boolean isUseFileSliceDownloader() {
        return useFileSliceDownloader;
    }

    private FileDownloadConfig() {}

    /**
     * 构建默认的FileDownloadConfig实例
     *
     * @return 默认的FileDownloadConfig实例
     */
    public static FileDownloadConfig createDefault() {
        return new FileDownloadConfig();
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
        private FileDownloadConfig config = new FileDownloadConfig();

        private Builder(){}

        /**
         * 设置文件切片后每个slice的大小
         *
         * @param unitSize 文件切片后每个slice的大小
         */
        public Builder setUnitSize(int unitSize) {
            Validate.isTrue(unitSize > 0);
            config.unitSize = unitSize;
            return this;
        }

        /**
         * 设置文件下载的timeout时间, 该timeout时间是整个文件所有切片下载的timeout时间
         *
         * @param timeout 文件下载的timeout时间
         */
        public Builder setTimeout(int timeout) {
            Validate.isTrue(timeout > 0);
            config.timeout = timeout;
            return this;
        }

        /**
         * 设置是否使用slice的方式下载文件, 默认是false
         *
         * @param useFileSliceDownloader 是否使用slice的方式下载文件
         */
        public Builder useFileSliceDownloader(boolean useFileSliceDownloader) {
            config.useFileSliceDownloader = useFileSliceDownloader;
            return this;
        }

        /**
         * 构造FileDownloadConfig实例
         *
         * @return FileDownloadConfig实例
         */
        public FileDownloadConfig build() {
            return config;
        }
    }
}
