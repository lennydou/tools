package com.lendou.file.async;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 记录文件信息的类, 用于断点续传
 */
public class FileInfoHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileInfoHolder.class);

    private static ConcurrentHashMap<String, FileContext> fileInfoMap = new ConcurrentHashMap<String, FileContext>();

    public static FileContext getFileInfo(String fileId) {
        Validate.notEmpty(fileId);
        return fileInfoMap.get(fileId);
    }

    public static void addFileInfo(FileContext fileContext) {
        Validate.notNull(fileContext);
        fileInfoMap.putIfAbsent(fileContext.getFileId(), fileContext);
    }
}