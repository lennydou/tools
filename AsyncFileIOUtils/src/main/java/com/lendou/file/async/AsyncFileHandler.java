package com.lendou.file.async;

/**
 * 客户端需要实现的文件处理类
 */
public interface AsyncFileHandler {

    /**
     * 文件下载完成后, 该方法会被调用. FileInfo文件信息, fileData是文件内容.<br/ >
     *
     * 如果文件下载失败, fileData为空.
     *
     * @param fileContext 文件信息
     */
    void handle(FileContext fileContext);
}
