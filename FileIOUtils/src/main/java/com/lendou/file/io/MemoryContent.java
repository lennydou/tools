package com.lendou.file.io;

import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.InputStream;

public final class MemoryContent implements Content {

    private FileContext fileContext;


    public MemoryContent(FileContext fileContext) {
        Validate.isTrue(fileContext.getFileSize() >= 0);

        this.fileContext = fileContext;
    }

    public void read(InputStream is) throws IOException {
        Validate.notNull(is);
        Validate.notNull(fileContext);

        int offset = fileContext.getOffset();
        int bytesRead;
        while ((bytesRead = is.read(fileContext.getContent(), offset, fileContext.getFileSize() - offset)) != -1) {
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
