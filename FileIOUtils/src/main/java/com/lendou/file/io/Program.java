package com.lendou.file.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Program {

    // private static String url = "https://d1opms6zj7jotq.cloudfront.net/idea/ideaIU-15.0.4.tar.gz";
    private static String url = "http://a.hiphotos.baidu.com/zhidao/pic/item/a044ad345982b2b7e0e2328b30adcbef76099b7b.jpg";
    // private static String url = "https://upload.wikimedia.org/wikipedia/commons/3/3f/Fronalpstock_big.jpg";

    public static void main(String[] args) throws IOException {
        download(getSize());
    }

    public static void download(int fileSize) throws IOException {
        FileContext context = FileContext.createFileInfo(url, "testFileId", fileSize);

        Stat.start();
        FileDownloadManager.download(context);

        if (context.isInMemory()) {
            FileUtils.writeByteArrayToFile(new File("/data/soft/transfer/files/" + System.currentTimeMillis()), context.getContent());
        }
    }

    public static int getSize() {

        int size = FileSizeHelper.getFileSize(url);
        System.out.println("fileSize: " + size);
        return size;
    }
}
