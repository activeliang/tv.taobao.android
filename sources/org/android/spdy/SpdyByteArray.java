package org.android.spdy;

import java.util.Arrays;

public class SpdyByteArray implements Comparable<SpdyByteArray> {
    private byte[] byteArray;
    int dataLength;
    int length;

    SpdyByteArray() {
        this.byteArray = null;
        this.length = 0;
        this.dataLength = 0;
    }

    SpdyByteArray(int length2) {
        this.byteArray = new byte[length2];
        this.length = length2;
        this.dataLength = 0;
    }

    /* access modifiers changed from: package-private */
    public void setByteArrayDataLength(int length2) {
        this.dataLength = length2;
    }

    public byte[] getByteArray() {
        return this.byteArray;
    }

    public int getDataLength() {
        return this.dataLength;
    }

    public void recycle() {
        Arrays.fill(this.byteArray, (byte) 0);
        this.dataLength = 0;
        SpdyBytePool.getInstance().recycle(this);
    }

    public int compareTo(SpdyByteArray another) {
        if (this.length != another.length) {
            return this.length - another.length;
        }
        if (this.byteArray == null) {
            return -1;
        }
        if (another.byteArray == null) {
            return 1;
        }
        return hashCode() - another.hashCode();
    }
}
