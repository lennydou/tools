package com.lendou.file.io;

import java.io.IOException;
import java.io.InputStream;

public interface Content {
    void read(InputStream is) throws IOException;
}
