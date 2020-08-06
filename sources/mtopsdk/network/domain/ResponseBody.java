package mtopsdk.network.domain;

import java.io.IOException;
import java.io.InputStream;

public abstract class ResponseBody {
    private static final String TAG = "mtopsdk.ResponseBody";
    private byte[] bodyBytes = null;

    public abstract InputStream byteStream();

    public abstract long contentLength() throws IOException;

    public abstract String contentType();

    public byte[] getBytes() throws IOException {
        if (this.bodyBytes == null) {
            this.bodyBytes = readBytes();
        }
        return this.bodyBytes;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x005a A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0074  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] readBytes() throws java.io.IOException {
        /*
            r14 = this;
            long r4 = r14.contentLength()
            r12 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r11 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r11 <= 0) goto L_0x0025
            java.io.IOException r11 = new java.io.IOException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "Cannot buffer entire body for content length: "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r4)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        L_0x0025:
            java.io.InputStream r9 = r14.byteStream()
            r6 = 0
            r0 = 0
            r3 = 0
            java.io.DataInputStream r7 = new java.io.DataInputStream     // Catch:{ Exception -> 0x0090 }
            r7.<init>(r9)     // Catch:{ Exception -> 0x0090 }
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0092, all -> 0x0089 }
            r1.<init>()     // Catch:{ Exception -> 0x0092, all -> 0x0089 }
            r11 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r11]     // Catch:{ Exception -> 0x0046, all -> 0x008c }
        L_0x003a:
            int r10 = r7.read(r2)     // Catch:{ Exception -> 0x0046, all -> 0x008c }
            r11 = -1
            if (r10 == r11) goto L_0x005c
            r11 = 0
            r1.write(r2, r11, r10)     // Catch:{ Exception -> 0x0046, all -> 0x008c }
            goto L_0x003a
        L_0x0046:
            r8 = move-exception
            r0 = r1
            r6 = r7
        L_0x0049:
            java.lang.String r11 = "mtopsdk.ResponseBody"
            java.lang.String r12 = "[readBytes] read bytes from byteStream error."
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r11, (java.lang.String) r12, (java.lang.Throwable) r8)     // Catch:{ all -> 0x006c }
            mtopsdk.network.util.NetworkUtils.closeQuietly(r6)
            mtopsdk.network.util.NetworkUtils.closeQuietly(r0)
        L_0x0058:
            if (r3 != 0) goto L_0x0074
            r3 = 0
        L_0x005b:
            return r3
        L_0x005c:
            r1.flush()     // Catch:{ Exception -> 0x0046, all -> 0x008c }
            byte[] r3 = r1.toByteArray()     // Catch:{ Exception -> 0x0046, all -> 0x008c }
            mtopsdk.network.util.NetworkUtils.closeQuietly(r7)
            mtopsdk.network.util.NetworkUtils.closeQuietly(r1)
            r0 = r1
            r6 = r7
            goto L_0x0058
        L_0x006c:
            r11 = move-exception
        L_0x006d:
            mtopsdk.network.util.NetworkUtils.closeQuietly(r6)
            mtopsdk.network.util.NetworkUtils.closeQuietly(r0)
            throw r11
        L_0x0074:
            r12 = -1
            int r11 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r11 == 0) goto L_0x005b
            int r11 = r3.length
            long r12 = (long) r11
            int r11 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r11 == 0) goto L_0x005b
            java.io.IOException r11 = new java.io.IOException
            java.lang.String r12 = "Content-Length and stream length disagree"
            r11.<init>(r12)
            throw r11
        L_0x0089:
            r11 = move-exception
            r6 = r7
            goto L_0x006d
        L_0x008c:
            r11 = move-exception
            r0 = r1
            r6 = r7
            goto L_0x006d
        L_0x0090:
            r8 = move-exception
            goto L_0x0049
        L_0x0092:
            r8 = move-exception
            r6 = r7
            goto L_0x0049
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.network.domain.ResponseBody.readBytes():byte[]");
    }
}
