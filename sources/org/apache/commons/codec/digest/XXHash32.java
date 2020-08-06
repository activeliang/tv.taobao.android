package org.apache.commons.codec.digest;

import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.util.zip.Checksum;

public class XXHash32 implements Checksum {
    private static final int BUF_SIZE = 16;
    private static final int PRIME1 = -1640531535;
    private static final int PRIME2 = -2048144777;
    private static final int PRIME3 = -1028477379;
    private static final int PRIME4 = 668265263;
    private static final int PRIME5 = 374761393;
    private static final int ROTATE_BITS = 13;
    private final byte[] buffer;
    private final byte[] oneByte;
    private int pos;
    private final int seed;
    private final int[] state;
    private int totalLen;

    public XXHash32() {
        this(0);
    }

    public XXHash32(int seed2) {
        this.oneByte = new byte[1];
        this.state = new int[4];
        this.buffer = new byte[16];
        this.seed = seed2;
        initializeState();
    }

    public void reset() {
        initializeState();
        this.totalLen = 0;
        this.pos = 0;
    }

    public void update(int b) {
        this.oneByte[0] = (byte) (b & 255);
        update(this.oneByte, 0, 1);
    }

    public void update(byte[] b, int off, int len) {
        if (len > 0) {
            this.totalLen += len;
            int end = off + len;
            if (this.pos + len < 16) {
                System.arraycopy(b, off, this.buffer, this.pos, len);
                this.pos += len;
                return;
            }
            if (this.pos > 0) {
                int size = 16 - this.pos;
                System.arraycopy(b, off, this.buffer, this.pos, size);
                process(this.buffer, 0);
                off += size;
            }
            int limit = end - 16;
            while (off <= limit) {
                process(b, off);
                off += 16;
            }
            if (off < end) {
                this.pos = end - off;
                System.arraycopy(b, off, this.buffer, 0, this.pos);
            }
        }
    }

    public long getValue() {
        int hash;
        if (this.totalLen > 16) {
            hash = Integer.rotateLeft(this.state[0], 1) + Integer.rotateLeft(this.state[1], 7) + Integer.rotateLeft(this.state[2], 12) + Integer.rotateLeft(this.state[3], 18);
        } else {
            hash = this.state[2] + PRIME5;
        }
        int hash2 = hash + this.totalLen;
        int idx = 0;
        int limit = this.pos - 4;
        while (idx <= limit) {
            hash2 = Integer.rotateLeft((getInt(this.buffer, idx) * PRIME3) + hash2, 17) * PRIME4;
            idx += 4;
        }
        while (idx < this.pos) {
            hash2 = Integer.rotateLeft(((this.buffer[idx] & OnReminderListener.RET_FULL) * PRIME5) + hash2, 11) * PRIME1;
            idx++;
        }
        int hash3 = (hash2 ^ (hash2 >>> 15)) * PRIME2;
        int hash4 = (hash3 ^ (hash3 >>> 13)) * PRIME3;
        return ((long) (hash4 ^ (hash4 >>> 16))) & 4294967295L;
    }

    private static int getInt(byte[] buffer2, int idx) {
        return (int) (fromLittleEndian(buffer2, idx, 4) & 4294967295L);
    }

    private void initializeState() {
        this.state[0] = this.seed + PRIME1 + PRIME2;
        this.state[1] = this.seed + PRIME2;
        this.state[2] = this.seed;
        this.state[3] = this.seed - PRIME1;
    }

    private void process(byte[] b, int offset) {
        int s0 = this.state[0];
        int s1 = this.state[1];
        int s2 = this.state[2];
        int s3 = this.state[3];
        int s02 = Integer.rotateLeft((getInt(b, offset) * PRIME2) + s0, 13) * PRIME1;
        int s12 = Integer.rotateLeft((getInt(b, offset + 4) * PRIME2) + s1, 13) * PRIME1;
        int s22 = Integer.rotateLeft((getInt(b, offset + 8) * PRIME2) + s2, 13) * PRIME1;
        int s32 = Integer.rotateLeft((getInt(b, offset + 12) * PRIME2) + s3, 13) * PRIME1;
        this.state[0] = s02;
        this.state[1] = s12;
        this.state[2] = s22;
        this.state[3] = s32;
        this.pos = 0;
    }

    private static long fromLittleEndian(byte[] bytes, int off, int length) {
        if (length > 8) {
            throw new IllegalArgumentException("can't read more than eight bytes into a long value");
        }
        long l = 0;
        for (int i = 0; i < length; i++) {
            l |= (((long) bytes[off + i]) & 255) << (i * 8);
        }
        return l;
    }
}
