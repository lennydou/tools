package com.lendou.file.async;

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

    /**
     * 根据文件大小, 计算出文件的切片数量
     *
     * @param fileSize 文件大小
     * @return 文件的切片信息
     */
    public static AsyncFileSliceInfo[] generateFileSliceInfos(int fileSize) {
        Validate.isTrue(fileSize > 0L);

        // 1. 从配置文件中获得每个切片的大小
        final int unitSize = FileConfig.getFileConfig().getUnitSize();

        // 2. 计算需要的切片数量
        int sliceCount = (int) (fileSize / unitSize);
        final int surplusSize = fileSize % unitSize;
        if (surplusSize > FileConfig.getMinUnitSize()) {
            // 如果最后剩余的部分大于 FileConfig.MinUnitSize, 则其作为一个单独的切片, 否则和上一个切片合并
            ++sliceCount;
        }

        // 3. 依次创建每个切片, 放入到sliceInfos
        AsyncFileSliceInfo[] sliceInfos = new AsyncFileSliceInfo[sliceCount];

        // 3.1 先构建 sliceCount - 1 个切片, 留最后一个切片单独处理
        int i = 0;
        int left = 0;
        for (; i < sliceCount - 1; i++) {
            int right = left + unitSize;
            sliceInfos[i] = new AsyncFileSliceInfo(left, right);
            left = right;
        }

        // 3.2 构建最后一个切片
        Validate.isTrue(left < fileSize);
        sliceInfos[i] = new AsyncFileSliceInfo(left, fileSize);

        return sliceInfos;
    }
}
