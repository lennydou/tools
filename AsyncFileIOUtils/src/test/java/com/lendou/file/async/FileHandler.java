package com.lendou.file.async;

/**
 * 文件处理类
 */
public class FileHandler implements AsyncFileHandler {

    public void handle(FileInfo fileInfo) {
        System.out.println("Called in FileHandler");
    }
}
