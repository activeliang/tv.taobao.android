package com.taobao.atlas.update.util;

import java.io.File;

public class PatchCleaner {
    public static void clearUpdatePath(String bundleUpdatePath) {
        File updatePath = new File(bundleUpdatePath);
        if (updatePath.exists()) {
            for (File file : updatePath.listFiles()) {
                if (file.isDirectory()) {
                    clearUpdatePath(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
            updatePath.delete();
        }
    }
}
