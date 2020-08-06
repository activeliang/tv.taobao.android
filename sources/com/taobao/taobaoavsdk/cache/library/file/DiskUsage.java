package com.taobao.taobaoavsdk.cache.library.file;

import java.io.File;
import java.io.IOException;

public interface DiskUsage {
    void touch(File file) throws IOException;
}
