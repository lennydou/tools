package com.lendou.file.async;

import org.apache.commons.lang3.Validate;

/**
 * 文件信息类
 */
class FileInfo {
    /**
     * 文件下载链接URI
     */
    private String uri;

    /**
     * 文件Id, 用作hashMap的key
     *
     * Note: 该值不落硬盘
     */
    private String fileId;

    /**
     * 文件名, 用于缓存到本地硬盘
     */
    private String fileName;

    /**
     * 文件长度
     */
    private int fileSize;

    /**
     * 缓存文件的长度
     */
    private int offset;

    /**
     * 构造下载文件的信息
     *
     * @param uri      文件下载的URI信息
     * @param fileId   文件唯一Id
     * @param fileName 缓存到本地硬盘的文件名
     * @param fileSize 文件长度
     * @param offset   缓存文件的长度
     */
    public FileInfo(String uri, String fileId, String fileName, int fileSize, int offset) {
        Validate.notEmpty(uri);
        Validate.notEmpty(fileId);
        Validate.notEmpty(fileName);
        Validate.isTrue(fileSize > 0);
        Validate.isTrue(offset >= 0);

        this.uri = uri;
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.offset = offset;
    }

    /**
     * 获得文件Id信息
     *
     * @return 文件Id
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * 获得下载链接
     *
     * @return 文件下载链接
     */
    public String getUri() {
        return uri;
    }

    /**
     * 获得文件下载的起始偏移量
     *
     * @return 文件下载的起始偏移量
     */
    public int getOffset() {
        return offset;
    }

    /**
     * 获得文件大小
     *
     * @return 文件大小
     */
    public int getFileSize() {
        return fileSize;
    }
}
