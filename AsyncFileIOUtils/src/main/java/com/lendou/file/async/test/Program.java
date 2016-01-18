package com.lendou.file.async.test;

import com.lendou.file.async.AsyncFileIOUtils;
import com.lendou.file.async.FileInfo;
import com.lendou.file.async.Stat;

/**
 * 主函数
 */
public class Program {

    private static String url1 = "http://dlsw.baidu.com/sw-search-sp/soft/3a/12350/QQ_8.0.16968.0_setup.1451877876.exe";
    private static String url2 = "http://bigota.d.miui.com/V7.1.3.0.KXDCNCK/cancro_images_V7.1.3.0.KXDCNCK_20151209.0000.6_4.4_cn_4e21b24cc2.tgz";
    private static String url3 = "http://bigota.d.miui.com/V7.1.4.0.LXKCNCK/libra_images_V7.1.4.0.LXKCNCK_20151209.0000.6_5.1_cn_a2d1be3cb8.tgz";

    private static TestFileHandler fileHandler = new TestFileHandler(1);

    public static void main(String[] args) {
        Stat.start();
        FileInfo fileInfo1 = FileInfo.createFileInfo(url1, "file1", AsyncFileIOUtils.getFileSize(url1));
//        FileInfo fileInfo2 = FileInfo.createFileInfo(url2, "file2", AsyncFileIOUtils.getFileSize(url2));
//        FileInfo fileInfo3 = FileInfo.createFileInfo(url3, "file3", AsyncFileIOUtils.getFileSize(url3));
        System.out.println(fileInfo1.getFileSize());
//        System.out.println(fileInfo2.getFileSize());
//        System.out.println(fileInfo3.getFileSize());

        AsyncFileIOUtils.download(fileInfo1, fileHandler);
//        AsyncFileIOUtils.download(fileInfo2, fileHandler);
//        AsyncFileIOUtils.download(fileInfo3, fileHandler);
    }
}
