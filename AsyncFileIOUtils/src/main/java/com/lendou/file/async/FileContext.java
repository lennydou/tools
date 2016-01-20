package com.lendou.file.async;

import org.apache.commons.lang3.Validate;

/**
 * <b>文件信息类 - FileContext</b>
 *
 * <p>FileContext保存文件的信息, 包括文件的链接, 文件Id, 文件长度</p>
 */
public class FileContext {
    /**
     * 文件下载链接URI
     */
    private String uri;

    /**
     * 文件Id, 必须保证唯一性.
     */
    private String fileId;

    /**
     * 文件长度
     */
    private int fileSize;

    /**
     * 文件名称<br/>
     * 用于文件缓存到硬盘时的文件名.
     */
    private String fileName;

    /**
     * 文件切片信息
     */
    private AsyncFileSliceInfo[] sliceInfos;

    /**
     * 文件二进制内容
     *
     * Note: 虽然有多个线程会同时操作content, 改写content内容。但是每个线程操作的range是不同的, 所以该变量没有线程安全问题.
     */
    private volatile byte[] content;

    /**
     * 文件下载是否失败
     *
     * Note: 虽然有多个线程会写入并读取该值, 但是由于所有线程对该值的写入都是把该值设置为true, 所以没有强同步需求.
     */
    private volatile boolean isFailed = false;

    /**
     * 构造下载文件的信息
     *
     * @param uri 文件下载的URI信息
     * @param fileId 文件唯一Id
     * @param fileSize 文件长度
     */
    private FileContext(String uri, String fileId, int fileSize) {
        Validate.notEmpty(uri);
        Validate.notEmpty(fileId);
        Validate.isTrue(fileSize > 0);

        this.uri = uri;
        this.fileId = fileId;
        this.fileSize = fileSize;

        this.fileName = FileInfoUtils.generateFileName(fileId);
        this.sliceInfos = FileInfoUtils.generateFileSliceInfos(fileSize);
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
     * 获得文件大小
     *
     * @return 文件大小
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * 获得文件名
     * Note: 该方法可见度是package级别
     *
     * @return 文件名
     */
    String getFileName() {
        return fileName;
    }

    /**
     * 获得当前文件切片数量
     *
     * @return 当前文件切片数量
     */
    int getSliceCount() {
        return sliceInfos.length;
    }

    /**
     * 获得文件内容数组
     *
     * @return 文件内容数组
     */
    public byte[] getContent() {
        Validate.notNull(content);
        return content;
    }

    /**
     * 文件下载是否失败
     *
     * @return 文件下载是否失败
     */
    public boolean isFailed() {
        return isFailed;
    }

    /**
     * 设置当前文件下载失败
     */
    public void setFailed() {
        isFailed = false;
    }

    /**
     * 初始化文件内容
     */
    public void initContent() {
        if (content != null && content.length > 0) {
            // 已经初始化过了, 不想要再初始化
            return;
        }

        content = new byte[getFileSize()];
        isFailed = false;
    }

    /**
     * 获得文件的第index个切片信息
     *
     * @param index 切片下标
     * @return 第index个切片信息
     */
    AsyncFileSliceInfo getSliceInfo(int index) {
        Validate.isTrue(index >= 0);
        Validate.isTrue(index < getSliceCount());

        return sliceInfos[index];
    }

    /**
     * 构造{@link FileContext}实例. <br/>
     * <p>使用该函数构造{@link FileContext}实例时, offset值默认是0</p>
     *
     * @param uri 文件下载链接
     * @param fileId 文件Id
     * @param fileSize 文件大小
     * @return {@link FileContext}实例
     * @see FileContext
     */
    public static FileContext createFileInfo(String uri, String fileId, int fileSize) {
        return new FileContext(uri, fileId, fileSize);
    }
}
