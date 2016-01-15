package com.lendou.file.async;

import org.apache.commons.lang3.Validate;

/**
 * 文件配置信息
 */
public class FileConfig {

    /**
     * 每次http请求的文件slice的最小size.
     *
     * 当对文件切片时, 如果最后一个slice的size小于MIN_UNIT_SIZE, 则把该最后一个slice和倒数第二个slice合并作为同一个slice.
     */
    private static final int MIN_UNIT_SIZE = 1024;

    /**
     * 当前的FileConfig实例
     */
    private static FileConfig fileConfig = new Builder().build();

    /**
     * 文件切片后每个slice的大小
     */
    private long unitSize;

    /**
     * 文件下载的timeout时间, 该timeout时间是整个文件所有切片下载的timeout时间
     */
    private int timeout;

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
    public long getUnitSize() {
        return unitSize;
    }

    /**
     * 获得文件下载的timeout时间, 该timeout时间是整个文件所有切片下载的timeout时间
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * 禁用外部通过构造器构造实例
     */
    private FileConfig(Builder builder) {
        Validate.notNull(builder);

        this.unitSize = builder.unitSize;
        this.timeout = builder.timeout;
    }

    /**
     * 获得当前的FileConfig实例
     *
     * @return 默认的FileConfig实例
     */
    public static FileConfig getFileConfig() {
        return fileConfig;
    }

    /**
     * 设置FileConfig信息
     *
     * @param config FileConfig实例
     */
    public static void setFileConfig(FileConfig config) {
        Validate.notNull(config);
        fileConfig = config;
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
        private int unitSize = 64 * 1024;
        private int timeout = 5 * 60;

        private Builder(){}

        /**
         * 设置文件切片后每个slice的大小
         *
         * @param unitSize 文件切片后每个slice的大小
         */
        public Builder setUnitSize(int unitSize) {
            Validate.isTrue(unitSize > 0);
            this.unitSize = unitSize;
            return this;
        }

        /**
         * 设置文件下载的timeout时间, 该timeout时间是整个文件所有切片下载的timeout时间
         *
         * @param timeout 文件下载的timeout时间
         */
        public Builder setTimeout(int timeout) {
            Validate.isTrue(timeout > 0);
            this.timeout = timeout;
            return this;
        }

        /**
         * 构造FileDownloadConfig实例
         *
         * @return FileDownloadConfig实例
         */
        public FileConfig build() {
            return new FileConfig(this);
        }
    }
}
