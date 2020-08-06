package com.tvlife.imageloader.core.assist;

import java.io.IOException;
import java.io.InputStream;

public class ContentLengthInputStream extends InputStream {
    private final long length;
    private long pos;
    private final InputStream stream;

    public ContentLengthInputStream(InputStream stream2, long length2) {
        this.stream = stream2;
        this.length = length2;
    }

    public synchronized int available() {
        return (int) (this.length - this.pos);
    }

    public void close() throws IOException {
        this.stream.close();
    }

    public void mark(int readlimit) {
        this.pos = (long) readlimit;
        this.stream.mark(readlimit);
    }

    public int read() throws IOException {
        this.pos++;
        return this.stream.read();
    }

    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        this.pos += (long) byteCount;
        return this.stream.read(buffer, byteOffset, byteCount);
    }

    public synchronized void reset() throws IOException {
        this.pos = 0;
        this.stream.reset();
    }

    public long skip(long byteCount) throws IOException {
        this.pos += byteCount;
        return this.stream.skip(byteCount);
    }
}
