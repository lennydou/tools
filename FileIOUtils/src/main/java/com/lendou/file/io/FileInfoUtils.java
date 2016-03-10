package com.lendou.file.io;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 文件信息的帮助类
 */
class FileInfoUtils {
    private static final String SEPARATOR = "-";

    // 全局唯一的编号产生器
    private static AtomicLong nextId;

    // 用于计算md5的digest
    private static MessageDigest msgDigest;

    static {
        // 以当前时间作为nextId的起始值
        nextId = new AtomicLong(System.currentTimeMillis());

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("No MD5 algorithm", e);
        }

        Validate.notNull(msgDigest);
    }

    /**
     * 生成文件名, 文件名是全局唯一的
     *
     * @param fileId 文件Id信息
     * @return 全局唯一的文件名
     */
    public static String generateFileName(String fileId) {
        Validate.notEmpty(fileId);
        return Hex.encodeHexString(msgDigest.digest(fileId.getBytes())) + SEPARATOR + nextId.incrementAndGet();
    }
}
