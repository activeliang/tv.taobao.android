package com.zhiping.dev.android.logcat.config;

import java.io.File;

public interface IOssStorageCfg {
    String getBucketName();

    String getObjectKey(File file);
}
