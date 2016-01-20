package com.lendou.file.async.test;

import com.lendou.file.async.AsyncFileHandler;
import com.lendou.file.async.AsyncFileIOUtils;
import com.lendou.file.async.FileContext;
import com.lendou.file.async.Stat;

import java.util.concurrent.atomic.AtomicInteger;

public class TestFileHandler implements AsyncFileHandler {

    private long start = System.currentTimeMillis();

    private AtomicInteger count;

    public TestFileHandler(int count) {
        this.count = new AtomicInteger(count);
    }

    public void handle(FileContext fileContext) {
        System.out.println("in fileHandler: " + fileContext.getContent().length);

//        try {
//            FileUtils.writeByteArrayToFile(new File("/home/dyl/pictures/test/data_" + fileInfo.getFileId()), fileInfo.getContent());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("Download file " + fileContext.getFileId() + " " + fileContext.isFailed());

        System.out.println("time: " + (System.currentTimeMillis() - start));
        if (count.decrementAndGet() == 0) {
            Stat.stop();
            AsyncFileIOUtils.shutdown();
            System.out.println("stop ....");
        }
    }
}
