package com.lendou.file.io;

import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FileContent implements Content {

    private File file;
    private FileContext fileContext;

    public FileContent(FileContext fileContext) {
        Validate.notEmpty(fileContext.getFilePath());

        this.file = new File(fileContext.getFilePath());
        this.fileContext = fileContext;
    }

    public void read(InputStream is) throws IOException {
        Validate.notNull(is);
        Validate.notNull(fileContext);

        FileOutputStream fos = new FileOutputStream(file);

        int offset = fileContext.getOffset();
        byte[] buffer = new byte[4 * 1024];

        int bytesRead;
        while ((bytesRead = is.read(buffer, 0, buffer.length)) != -1) {
            fos.write(buffer, 0, bytesRead);

            offset += bytesRead;
            fileContext.setOffset(offset);
            if (offset == fileContext.getFileSize()) {
                break;
            }

            // 仅用于统计
            Stat.add(bytesRead);
        }
    }
}
