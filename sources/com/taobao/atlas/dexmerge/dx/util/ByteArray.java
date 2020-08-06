package com.taobao.atlas.dexmerge.dx.util;

import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ByteArray {
    /* access modifiers changed from: private */
    public final byte[] bytes;
    /* access modifiers changed from: private */
    public final int size;
    /* access modifiers changed from: private */
    public final int start;

    public interface GetCursor {
        int getCursor();
    }

    public ByteArray(byte[] bytes2, int start2, int end) {
        if (bytes2 == null) {
            throw new NullPointerException("bytes == null");
        } else if (start2 < 0) {
            throw new IllegalArgumentException("start < 0");
        } else if (end < start2) {
            throw new IllegalArgumentException("end < start");
        } else if (end > bytes2.length) {
            throw new IllegalArgumentException("end > bytes.length");
        } else {
            this.bytes = bytes2;
            this.start = start2;
            this.size = end - start2;
        }
    }

    public ByteArray(byte[] bytes2) {
        this(bytes2, 0, bytes2.length);
    }

    public int size() {
        return this.size;
    }

    public ByteArray slice(int start2, int end) {
        checkOffsets(start2, end);
        return new ByteArray(this.bytes, this.start + start2, this.start + end);
    }

    public int underlyingOffset(int offset, byte[] bytes2) {
        if (bytes2 == this.bytes) {
            return this.start + offset;
        }
        throw new IllegalArgumentException("wrong bytes");
    }

    public int getByte(int off) {
        checkOffsets(off, off + 1);
        return getByte0(off);
    }

    public int getShort(int off) {
        checkOffsets(off, off + 2);
        return (getByte0(off) << 8) | getUnsignedByte0(off + 1);
    }

    public int getInt(int off) {
        checkOffsets(off, off + 4);
        return (getByte0(off) << 24) | (getUnsignedByte0(off + 1) << 16) | (getUnsignedByte0(off + 2) << 8) | getUnsignedByte0(off + 3);
    }

    public long getLong(int off) {
        checkOffsets(off, off + 8);
        return (((long) ((getByte0(off + 4) << 24) | (getUnsignedByte0(off + 5) << 16) | (getUnsignedByte0(off + 6) << 8) | getUnsignedByte0(off + 7))) & 4294967295L) | (((long) ((((getByte0(off) << 24) | (getUnsignedByte0(off + 1) << 16)) | (getUnsignedByte0(off + 2) << 8)) | getUnsignedByte0(off + 3))) << 32);
    }

    public int getUnsignedByte(int off) {
        checkOffsets(off, off + 1);
        return getUnsignedByte0(off);
    }

    public int getUnsignedShort(int off) {
        checkOffsets(off, off + 2);
        return (getUnsignedByte0(off) << 8) | getUnsignedByte0(off + 1);
    }

    public void getBytes(byte[] out, int offset) {
        if (out.length - offset < this.size) {
            throw new IndexOutOfBoundsException("(out.length - offset) < size()");
        }
        System.arraycopy(this.bytes, this.start, out, offset, this.size);
    }

    private void checkOffsets(int s, int e) {
        if (s < 0 || e < s || e > this.size) {
            throw new IllegalArgumentException("bad range: " + s + ".." + e + "; actual size " + this.size);
        }
    }

    private int getByte0(int off) {
        return this.bytes[this.start + off];
    }

    /* access modifiers changed from: private */
    public int getUnsignedByte0(int off) {
        return this.bytes[this.start + off] & OnReminderListener.RET_FULL;
    }

    public MyDataInputStream makeDataInputStream() {
        return new MyDataInputStream(makeInputStream());
    }

    public MyInputStream makeInputStream() {
        return new MyInputStream();
    }

    public class MyInputStream extends InputStream {
        private int cursor = 0;
        private int mark = 0;

        public MyInputStream() {
        }

        public int read() throws IOException {
            if (this.cursor >= ByteArray.this.size) {
                return -1;
            }
            int access$100 = ByteArray.this.getUnsignedByte0(this.cursor);
            this.cursor++;
            return access$100;
        }

        public int read(byte[] arr, int offset, int length) {
            if (offset + length > arr.length) {
                length = arr.length - offset;
            }
            int maxLength = ByteArray.this.size - this.cursor;
            if (length > maxLength) {
                length = maxLength;
            }
            System.arraycopy(ByteArray.this.bytes, this.cursor + ByteArray.this.start, arr, offset, length);
            this.cursor += length;
            return length;
        }

        public int available() {
            return ByteArray.this.size - this.cursor;
        }

        public void mark(int reserve) {
            this.mark = this.cursor;
        }

        public void reset() {
            this.cursor = this.mark;
        }

        public boolean markSupported() {
            return true;
        }
    }

    public static class MyDataInputStream extends DataInputStream {
        private final MyInputStream wrapped;

        public MyDataInputStream(MyInputStream wrapped2) {
            super(wrapped2);
            this.wrapped = wrapped2;
        }
    }
}
