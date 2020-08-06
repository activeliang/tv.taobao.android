package com.alibaba.sdk.android.oss.internal;

import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public class CheckCRC64DownloadInputStream extends CheckedInputStream {
    private long mClientCRC64;
    private String mRequestId;
    private long mServerCRC64;
    private long mTotalBytesRead;
    private long mTotalLength;

    public CheckCRC64DownloadInputStream(InputStream is, Checksum csum, long total, long serverCRC64, String requestId) {
        super(is, csum);
        this.mTotalLength = total;
        this.mServerCRC64 = serverCRC64;
        this.mRequestId = requestId;
    }

    public int read() throws IOException {
        int read = super.read();
        checkCRC64(read);
        return read;
    }

    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        int read = super.read(buffer, byteOffset, byteCount);
        checkCRC64(read);
        return read;
    }

    private void checkCRC64(int byteRead) throws IOException {
        this.mTotalBytesRead += (long) byteRead;
        if (this.mTotalBytesRead >= this.mTotalLength) {
            this.mClientCRC64 = getChecksum().getValue();
            OSSUtils.checkChecksum(Long.valueOf(this.mClientCRC64), Long.valueOf(this.mServerCRC64), this.mRequestId);
        }
    }

    public long getClientCRC64() {
        return this.mClientCRC64;
    }
}
