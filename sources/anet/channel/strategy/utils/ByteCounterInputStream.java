package anet.channel.strategy.utils;

import java.io.IOException;
import java.io.InputStream;

public class ByteCounterInputStream extends InputStream {
    private long cnt = 0;
    private InputStream is = null;

    public ByteCounterInputStream(InputStream is2) {
        if (is2 == null) {
            throw new NullPointerException("input stream cannot be null");
        }
        this.is = is2;
    }

    public long getReadByteCount() {
        return this.cnt;
    }

    public int read() throws IOException {
        this.cnt++;
        return this.is.read();
    }

    public int read(byte[] bytes, int i, int i1) throws IOException {
        int c = this.is.read(bytes, i, i1);
        if (c != -1) {
            this.cnt += (long) c;
        }
        return c;
    }
}
