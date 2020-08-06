package org.android.spdy;

public enum SslVersion {
    SLIGHT_VERSION_V1(0);
    
    private int code;

    private SslVersion(int code2) {
        this.code = code2;
    }

    /* access modifiers changed from: package-private */
    public int getint() {
        return this.code;
    }
}
