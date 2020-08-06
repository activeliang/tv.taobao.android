package org.android.spdy;

public enum SpdyVersion {
    SPDY2(2),
    SPDY3(3),
    SPDY3DOT1(4);
    
    private int version;

    private SpdyVersion(int version2) {
        this.version = version2;
    }

    /* access modifiers changed from: package-private */
    public int getInt() {
        return this.version;
    }
}
