package com.lendou.file.async;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 文件信息的帮助类
 */
class FileInfoUtils {
    private static final String SEPARATOR = "-";

    // TODO: 读取某个文件夹下面所有文件, 找到最大的Id, 用于初始化这个值
    private static AtomicLong nextId = new AtomicLong(0);

    /**
     * 生成文件块唯一Id
     *
     * @param stoid      文件的stoid信息
     * @param blockIndex 文件块在文件中的编号
     * @return           文件块的唯一Id
     */
    public static String generateFileId(String stoid, int blockIndex) {
        return stoid + SEPARATOR + blockIndex;
    }

    /**
     * 生成文件名, 文件名是全局唯一的
     *
     * @param sha1 文件的sha1信息
     * @return     文件名
     */
    public static String generateFileName(String sha1) {
        return sha1 + nextId.incrementAndGet();
    }
}
