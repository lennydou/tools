package com.lendou.file.io;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;

/**
 * <b>文件信息类 - FileInfo</b>
 *
 * <p>FileInfo保存文件的信息, 包括文件的链接, 文件Id, 文件长度</p>
 */
public class FileContext {
    private static String BASE_FOLDER = "/data/soft/transfer/files/";
    static {
        File file = new File(BASE_FOLDER);
        if (!file.exists()) {
            boolean ret = file.mkdirs();
            if (ret == false) {
                throw new AssertionError();
            }
        }
    }

    /**
     * 文件下载链接URI
     */
    private String uri;

    /**
     * 文件长度
     */
    private int fileSize;

    /**
     * 文件当前开始下载位置
     */
    private int offset;

    /**
     * 文件名称<br/>
     * 用于文件缓存到硬盘时的文件名.
     */
    private String filePath;

    /**
     * 文件二进制内容
     */
    private byte[] content;

    /**
     * 构造下载文件的信息
     *
     * @param uri 文件下载的URI信息
     * @param fileSize 文件长度
     */
    private FileContext(String uri, int fileSize) {
        Validate.notEmpty(uri);
        Validate.isTrue(fileSize > 0);

        this.uri = uri;
        this.fileSize = fileSize;
        this.offset = 0;
    }

    /**
     * 构造下载文件的信息
     *
     * @param uri 文件下载的URI信息
     * @param fileSize 文件长度
     */
    private FileContext(String uri, int fileSize, String filePath) {
        this(uri, fileSize);
        Validate.notEmpty(filePath);

        this.filePath = filePath;
    }

    /**
     * 构造下载文件的信息
     *
     * @param uri 文件下载的URI信息
     * @param fileSize 文件长度
     */
    private FileContext(String uri, int fileSize, byte[] content) {
        this(uri, fileSize);
        Validate.isTrue(ArrayUtils.isNotEmpty(content));

        this.content = content;
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
     * 获得文件需要开始下载的位置
     *
     * @return 文件需要开始下载的位置
     */
    int getOffset() {
        return offset;
    }

    /**
     * 设置文件的下载点
     *
     * @param point 文件下载点
     * @return 当前FileContext引用
     */
    FileContext setOffset(int point) {
        this.offset = point;
        return this;
    }

    /**
     * 获得文件路径
     *
     * @return 获得文件路径
     */
    public String getFilePath() {
        return filePath;
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
     * 文件是否下载完成
     *
     * @return 文件是否下载完成
     */
    public boolean isCompleted() {
        return offset == fileSize;
    }

    /**
     * 判断文件内容是否下载到内存中
     *
     * @return 文件内容是否下载到内存中
     */
    public boolean isInMemory() {
        return StringUtils.isEmpty(filePath);
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
        Validate.notEmpty(uri);
        Validate.notEmpty(fileId);
        Validate.isTrue(fileSize > 0);

        if (fileSize > FileConfig.DEFAULT.getMaxFileSizeInMemory()) {
            String filePath = BASE_FOLDER + FileInfoUtils.generateFileName(fileId);
            return new FileContext(uri, fileSize, filePath);

        } else {
            return new FileContext(uri, fileSize, new byte[fileSize]);
        }
    }
}
