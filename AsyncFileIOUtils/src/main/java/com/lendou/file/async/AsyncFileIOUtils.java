package com.lendou.file.async;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import javax.annotation.processing.Completion;
import java.util.concurrent.*;

/**
 * <b>文件异步下载工具类 - AsyncFileIOUtils</b>
 *
 * <p>提供文件异步下载-回调的接口</p>
 */
public class AsyncFileIOUtils {

    // TODO: 参数100需要从配置文件中读取
    private static final ExecutorService executor = Executors.newFixedThreadPool(100);

    /**
     * 下载文件内容.<br/>
     * 文件下载完成后, 该方法会调用{@link AsyncFileHandler}的handle方法.
     *
     * @param fileInfo 文件内容
     * @param fileHandler
     */
    public static void download(FileInfo fileInfo, AsyncFileHandler fileHandler) {
        Validate.notNull(fileInfo);
        Validate.notNull(fileHandler);

        executor.execute(new AsyncFileDownloader(fileInfo, fileHandler));
    }
}
