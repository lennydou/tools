package com.lendou.file.async;

/**
 * Created by dyl on 1/14/16.
 */
public interface AsyncFileHandler {

    /**
     * 文件下载完成后, 该方法会被调用. FileInfo文件信息, fileData是文件内容.<br/ >
     *
     * 如果文件下载失败, fileData为空.
     *
     * @param fileInfo 文件信息
     * @param fileData 文件内容
     */
    void handle(FileInfo fileInfo, byte[] fileData);
}
