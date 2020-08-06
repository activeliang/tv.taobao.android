package com.taobao.android.runtime;

import android.support.annotation.NonNull;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OatFile extends BaseDexBuffer {
    private static final byte[] ELF_MAGIC = {Byte.MAX_VALUE, 69, 76, 70};
    private static final int MAX_OAT_VERSION = 86;
    private static final int MIN_ELF_HEADER_SIZE = 52;
    private static final int MIN_OAT_VERSION_LOLLIPOP = 39;
    private static final int MIN_OAT_VERSION_LOLLIPOP_MR1 = 45;
    private static final int MIN_OAT_VERSION_M = 56;
    private static final byte[] OAT_MAGIC = {111, 97, 116, 10};
    public static final int SUPPORTED = 1;
    public static final int UNKNOWN = 2;
    public static final int UNSUPPORTED = 0;
    private final boolean is64bit;

    public static class NotAnOatFileException extends RuntimeException {
    }

    public OatFile(@NonNull byte[] buf, long length) {
        super(buf);
        int offset;
        int entrySize;
        int entryCount;
        if (buf.length < 52) {
            throw new NotAnOatFileException();
        }
        verifyMagic(buf);
        if (buf[4] == 1) {
            this.is64bit = false;
        } else if (buf[4] == 2) {
            this.is64bit = true;
        } else {
            throw new InvalidOatFileException(String.format("Invalid word-size value: %x", new Object[]{Byte.valueOf(buf[5])}));
        }
        if (this.is64bit) {
            offset = readLongAsSmallUint(40);
            entrySize = readUshort(58);
            entryCount = readUshort(60);
        } else {
            offset = readSmallUint(32);
            entrySize = readUshort(46);
            entryCount = readUshort(48);
        }
        if (((long) ((entrySize * entryCount) + offset)) > length) {
            throw new InvalidOatFileException("The ELF section headers extend past the end of the file");
        }
    }

    private static void verifyMagic(byte[] buf) {
        for (int i = 0; i < ELF_MAGIC.length; i++) {
            if (buf[i] != ELF_MAGIC[i]) {
                throw new NotAnOatFileException();
            }
        }
    }

    public static OatFile fromFile(@NonNull File file) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        try {
            if (!is.markSupported()) {
                throw new IllegalArgumentException("InputStream must support mark");
            }
            is.mark(4);
            byte[] partialHeader = new byte[4];
            try {
                is.read(partialHeader);
                is.reset();
                verifyMagic(partialHeader);
                is.reset();
                byte[] buf = new byte[64];
                is.read(buf);
                return new OatFile(buf, file.length());
            } catch (EOFException e) {
                throw new NotAnOatFileException();
            } catch (Throwable th) {
                is.reset();
                throw th;
            }
        } finally {
            is.close();
        }
    }

    public static class InvalidOatFileException extends RuntimeException {
        public InvalidOatFileException(String message) {
            super(message);
        }
    }
}
