package com.taobao.taobaoavsdk.cache.library.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class Files {
    Files() {
    }

    static void makeDir(File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                throw new IOException("File " + directory + " is not directory!");
            }
        } else if (!directory.mkdirs()) {
            throw new IOException(String.format("Directory %s can't be created", new Object[]{directory.getAbsolutePath()}));
        }
    }

    static List<File> getLruListFiles(File directory) {
        List<File> result = new LinkedList<>();
        File[] files = directory.listFiles();
        if (files == null) {
            return result;
        }
        List<File> result2 = Arrays.asList(files);
        Collections.sort(result2, new LastModifiedComparator());
        return result2;
    }

    static void setLastModifiedNow(File file) throws IOException {
        if (file.exists() && !file.setLastModified(System.currentTimeMillis())) {
            modify(file);
        }
    }

    static void modify(File file) throws IOException {
        long size = file.length();
        if (size == 0) {
            recreateZeroSizeFile(file);
            return;
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
        try {
            accessFile.seek(size - 1);
            byte lastByte = accessFile.readByte();
            accessFile.seek(size - 1);
            accessFile.write(lastByte);
        } finally {
            accessFile.close();
        }
    }

    private static void recreateZeroSizeFile(File file) throws IOException {
        if (!file.delete() || !file.createNewFile()) {
            throw new IOException("Error recreate zero-size file " + file);
        }
    }

    private static final class LastModifiedComparator implements Comparator<File> {
        private LastModifiedComparator() {
        }

        public int compare(File lhs, File rhs) {
            return compareLong(lhs.lastModified(), rhs.lastModified());
        }

        private int compareLong(long first, long second) {
            if (first < second) {
                return -1;
            }
            return first == second ? 0 : 1;
        }
    }
}
