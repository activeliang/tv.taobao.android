package org.apache.commons.codec.binary;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.codec.binary.BaseNCodec;

public class BaseNCodecOutputStream extends FilterOutputStream {
    private final BaseNCodec baseNCodec;
    private final BaseNCodec.Context context = new BaseNCodec.Context();
    private final boolean doEncode;
    private final byte[] singleByte = new byte[1];

    public BaseNCodecOutputStream(OutputStream out, BaseNCodec basedCodec, boolean doEncode2) {
        super(out);
        this.baseNCodec = basedCodec;
        this.doEncode = doEncode2;
    }

    public void write(int i) throws IOException {
        this.singleByte[0] = (byte) i;
        write(this.singleByte, 0, 1);
    }

    public void write(byte[] b, int offset, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (offset < 0 || len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (offset > b.length || offset + len > b.length) {
            throw new IndexOutOfBoundsException();
        } else if (len > 0) {
            if (this.doEncode) {
                this.baseNCodec.encode(b, offset, len, this.context);
            } else {
                this.baseNCodec.decode(b, offset, len, this.context);
            }
            flush(false);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000b, code lost:
        r1 = new byte[r0];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void flush(boolean r7) throws java.io.IOException {
        /*
            r6 = this;
            r5 = 0
            org.apache.commons.codec.binary.BaseNCodec r3 = r6.baseNCodec
            org.apache.commons.codec.binary.BaseNCodec$Context r4 = r6.context
            int r0 = r3.available(r4)
            if (r0 <= 0) goto L_0x001c
            byte[] r1 = new byte[r0]
            org.apache.commons.codec.binary.BaseNCodec r3 = r6.baseNCodec
            org.apache.commons.codec.binary.BaseNCodec$Context r4 = r6.context
            int r2 = r3.readResults(r1, r5, r0, r4)
            if (r2 <= 0) goto L_0x001c
            java.io.OutputStream r3 = r6.out
            r3.write(r1, r5, r2)
        L_0x001c:
            if (r7 == 0) goto L_0x0023
            java.io.OutputStream r3 = r6.out
            r3.flush()
        L_0x0023:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.binary.BaseNCodecOutputStream.flush(boolean):void");
    }

    public void flush() throws IOException {
        flush(true);
    }

    public void close() throws IOException {
        eof();
        flush();
        this.out.close();
    }

    public void eof() throws IOException {
        if (this.doEncode) {
            this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
        } else {
            this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
        }
    }
}
