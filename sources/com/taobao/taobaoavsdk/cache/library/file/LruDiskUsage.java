package com.taobao.taobaoavsdk.cache.library.file;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class LruDiskUsage implements DiskUsage {
    private static final String LOG_TAG = "ProxyCache";
    private final ExecutorService workerThread = Executors.newSingleThreadExecutor();

    /* access modifiers changed from: protected */
    public abstract boolean accept(File file, long j, int i);

    LruDiskUsage() {
    }

    public void touch(File file) throws IOException {
        this.workerThread.submit(new TouchCallable(file));
    }

    /* access modifiers changed from: private */
    public void touchInBackground(File file) throws IOException {
        Files.setLastModifiedNow(file);
        trim(Files.getLruListFiles(file.getParentFile()));
    }

    private void trim(List<File> files) {
        long totalSize = countTotalSize(files);
        int totalCount = files.size();
        for (File file : files) {
            if (!accept(file, totalSize, totalCount)) {
                long fileSize = file.length();
                if (file.delete()) {
                    totalCount--;
                    totalSize -= fileSize;
                }
            }
        }
    }

    private long countTotalSize(List<File> files) {
        long totalSize = 0;
        for (File file : files) {
            totalSize += file.length();
        }
        return totalSize;
    }

    private class TouchCallable implements Callable<Void> {
        private final File file;

        public TouchCallable(File file2) {
            this.file = file2;
        }

        public Void call() throws Exception {
            LruDiskUsage.this.touchInBackground(this.file);
            return null;
        }
    }
}
