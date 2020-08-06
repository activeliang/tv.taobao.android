package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.BaseNCodec;

public class BaseNCodecInputStream extends FilterInputStream {
    private final BaseNCodec baseNCodec;
    private final BaseNCodec.Context context = new BaseNCodec.Context();
    private final boolean doEncode;
    private final byte[] singleByte = new byte[1];

    protected BaseNCodecInputStream(InputStream in, BaseNCodec baseNCodec2, boolean doEncode2) {
        super(in);
        this.doEncode = doEncode2;
        this.baseNCodec = baseNCodec2;
    }

    public int available() throws IOException {
        return this.context.eof ? 0 : 1;
    }

    public synchronized void mark(int readLimit) {
    }

    public boolean markSupported() {
        return false;
    }

    public int read() throws IOException {
        int r = read(this.singleByte, 0, 1);
        while (r == 0) {
            r = read(this.singleByte, 0, 1);
        }
        if (r <= 0) {
            return -1;
        }
        byte b = this.singleByte[0];
        if (b < 0) {
            return b + 256;
        }
        return b;
    }

    public int read(byte[] b, int offset, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (offset < 0 || len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (offset > b.length || offset + len > b.length) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        } else {
            int readLen = 0;
            while (readLen == 0) {
                if (!this.baseNCodec.hasData(this.context)) {
                    byte[] buf = new byte[(this.doEncode ? 4096 : 8192)];
                    int c = this.in.read(buf);
                    if (this.doEncode) {
                        this.baseNCodec.encode(buf, 0, c, this.context);
                    } else {
                        this.baseNCodec.decode(buf, 0, c, this.context);
                    }
                }
                readLen = this.baseNCodec.readResults(b, offset, len, this.context);
            }
            return readLen;
        }
    }

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public long skip(long n) throws IOException {
        int len;
        if (n < 0) {
            throw new IllegalArgumentException("Negative skip length: " + n);
        }
        byte[] b = new byte[512];
        long todo = n;
        while (todo > 0 && (len = read(b, 0, (int) Math.min((long) b.length, todo))) != -1) {
            todo -= (long) len;
        }
        return n - todo;
    }
}
