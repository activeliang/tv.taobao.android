package com.taobao.taobaoavsdk.cache.library.file;

import java.io.File;
import java.io.IOException;

public class UnlimitedDiskUsage implements DiskUsage {
    public void touch(File file) throws IOException {
    }
}
