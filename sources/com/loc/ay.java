package com.loc;

import android.text.TextUtils;
import com.loc.aw;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

/* compiled from: HttpUrlUtil */
public final class ay {
    private int a;
    private int b;
    private boolean c;
    private SSLContext d;
    private Proxy e;
    private volatile boolean f;
    private long g;
    private long h;
    private String i;
    private b j;
    private aw.a k;

    /* compiled from: HttpUrlUtil */
    public static class a {
        public HttpURLConnection a;
        public int b;

        public a(HttpURLConnection httpURLConnection, int i) {
            this.a = httpURLConnection;
            this.b = i;
        }
    }

    /* compiled from: HttpUrlUtil */
    private static class b {
        private Vector<c> a;
        private volatile c b;

        private b() {
            this.a = new Vector<>();
            this.b = new c((byte) 0);
        }

        /* synthetic */ b(byte b2) {
            this();
        }

        public final c a() {
            return this.b;
        }

        public final c a(String str) {
            if (TextUtils.isEmpty(str)) {
                return this.b;
            }
            for (int i = 0; i < this.a.size(); i++) {
                c cVar = this.a.get(i);
                if (cVar != null && cVar.a().equals(str)) {
                    return cVar;
                }
            }
            c cVar2 = new c((byte) 0);
            cVar2.b(str);
            this.a.add(cVar2);
            return cVar2;
        }

        public final void b(String str) {
            if (!TextUtils.isEmpty(str)) {
                this.b.a(str);
            }
        }
    }

    /* compiled from: HttpUrlUtil */
    private static class c implements HostnameVerifier {
        private String a;
        private String b;

        private c() {
        }

        /* synthetic */ c(byte b2) {
            this();
        }

        public final String a() {
            return this.b;
        }

        public final void a(String str) {
            this.a = str;
        }

        public final void b(String str) {
            this.b = str;
        }

        public final boolean verify(String str, SSLSession sSLSession) {
            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            return !TextUtils.isEmpty(this.a) ? this.a.equals(str) : !TextUtils.isEmpty(this.b) ? defaultHostnameVerifier.verify(this.b, sSLSession) : defaultHostnameVerifier.verify(str, sSLSession);
        }
    }

    ay(int i2, int i3, Proxy proxy, boolean z) {
        this(i2, i3, proxy, z, (byte) 0);
    }

    private ay(int i2, int i3, Proxy proxy, boolean z, byte b2) {
        this.f = false;
        this.g = -1;
        this.h = 0;
        this.a = i2;
        this.b = i3;
        this.e = proxy;
        this.c = p.a().b(z);
        if (p.b()) {
            this.c = false;
        }
        this.k = null;
        try {
            this.i = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        } catch (Throwable th) {
            y.a(th, "ht", "ic");
        }
        if (this.c) {
            try {
                SSLContext instance = SSLContext.getInstance("TLS");
                instance.init((KeyManager[]) null, (TrustManager[]) null, (SecureRandom) null);
                this.d = instance;
            } catch (Throwable th2) {
                y.a(th2, "ht", "ne");
            }
        }
        this.j = new b((byte) 0);
    }

    public static int a(az azVar) {
        try {
            if (l.b()) {
                return 4;
            }
            if (azVar != null && !azVar.g()) {
                return 1;
            }
            return true == (!l.a() ? true : true) ? 2 : 1;
        } catch (Throwable th) {
            ab.b(th, "htu", "gt");
            return 1;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: java.io.PushbackInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: java.io.PushbackInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v9, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v10, resolved type: java.io.PushbackInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v11, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v11, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v12, resolved type: java.io.PushbackInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v12, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v14, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v21, resolved type: java.io.PushbackInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v22, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v23, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v26, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v28, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v29, resolved type: java.io.PushbackInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v32, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v33, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v23, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r4v6, types: [int] */
    /* JADX WARNING: type inference failed for: r4v22, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r3v30 */
    /* JADX WARNING: type inference failed for: r3v31 */
    /* JADX WARNING: type inference failed for: r3v34 */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x01c0, code lost:
        r2 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0080, code lost:
        r2 = r8;
        r3 = null;
        r4 = null;
        r5 = null;
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x001c, code lost:
        r2 = r14.get("gsid");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x01c0 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x0007] */
    /* JADX WARNING: Removed duplicated region for block: B:134:0x0137 A[EDGE_INSN: B:134:0x0137->B:82:0x0137 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0099 A[SYNTHETIC, Splitter:B:23:0x0099] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x009e A[SYNTHETIC, Splitter:B:26:0x009e] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a3 A[SYNTHETIC, Splitter:B:29:0x00a3] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00a8 A[SYNTHETIC, Splitter:B:32:0x00a8] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00db A[Catch:{ IOException -> 0x007f, all -> 0x01c0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x010f A[Catch:{ IOException -> 0x01f3, all -> 0x01cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x011f A[Catch:{ IOException -> 0x0124, all -> 0x01d3 }, LOOP:0: B:72:0x0118->B:74:0x011f, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0135 A[Catch:{ IOException -> 0x0124, all -> 0x01d3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0151 A[SYNTHETIC, Splitter:B:84:0x0151] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0156 A[SYNTHETIC, Splitter:B:87:0x0156] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x015b A[SYNTHETIC, Splitter:B:90:0x015b] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0160 A[SYNTHETIC, Splitter:B:93:0x0160] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.loc.ba a(com.loc.ay.a r22) throws com.loc.j, java.io.IOException {
        /*
            r21 = this;
            r12 = 0
            r11 = 0
            r10 = 0
            r9 = 0
            java.lang.String r3 = ""
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            r0 = r22
            java.net.HttpURLConnection r13 = r0.a     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            r13.connect()     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            java.util.Map r14 = r13.getHeaderFields()     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            int r4 = r13.getResponseCode()     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            if (r14 == 0) goto L_0x0204
            java.lang.String r2 = "gsid"
            java.lang.Object r2 = r14.get(r2)     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            java.util.List r2 = (java.util.List) r2     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            if (r2 == 0) goto L_0x0204
            int r5 = r2.size()     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            if (r5 <= 0) goto L_0x0204
            r5 = 0
            java.lang.Object r2 = r2.get(r5)     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ IOException -> 0x01db, all -> 0x01c0 }
            r8 = r2
        L_0x0035:
            r2 = 200(0xc8, float:2.8E-43)
            if (r4 == r2) goto L_0x00ac
            com.loc.j r2 = new com.loc.j     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.String r5 = "网络异常原因："
            r3.<init>(r5)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.String r5 = r13.getResponseMessage()     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.String r5 = " 网络异常状态码："
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.String r5 = "  "
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.StringBuilder r3 = r3.append(r8)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.String r5 = " "
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            r0 = r21
            java.lang.String r5 = r0.i     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            r0 = r21
            java.lang.String r5 = r0.i     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            r2.<init>(r3, r8, r5)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            r2.a(r4)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            throw r2     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
        L_0x007f:
            r2 = move-exception
            r2 = r8
            r3 = r9
            r4 = r10
            r5 = r11
            r6 = r12
        L_0x0085:
            com.loc.j r7 = new com.loc.j     // Catch:{ all -> 0x0092 }
            java.lang.String r8 = "IO 操作异常 - IOException"
            r0 = r21
            java.lang.String r9 = r0.i     // Catch:{ all -> 0x0092 }
            r7.<init>(r8, r2, r9)     // Catch:{ all -> 0x0092 }
            throw r7     // Catch:{ all -> 0x0092 }
        L_0x0092:
            r2 = move-exception
            r9 = r3
            r10 = r4
            r11 = r5
            r12 = r6
        L_0x0097:
            if (r12 == 0) goto L_0x009c
            r12.close()     // Catch:{ Throwable -> 0x0164 }
        L_0x009c:
            if (r11 == 0) goto L_0x00a1
            r11.close()     // Catch:{ Throwable -> 0x0170 }
        L_0x00a1:
            if (r9 == 0) goto L_0x00a6
            r9.close()     // Catch:{ Throwable -> 0x017c }
        L_0x00a6:
            if (r10 == 0) goto L_0x00ab
            r10.close()     // Catch:{ Throwable -> 0x0188 }
        L_0x00ab:
            throw r2
        L_0x00ac:
            long r16 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            r3 = 0
            r2 = 0
            r5 = 0
            r0 = r22
            java.net.HttpURLConnection r4 = r0.a     // Catch:{ Throwable -> 0x0130 }
            java.net.URL r4 = r4.getURL()     // Catch:{ Throwable -> 0x0130 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0130 }
            r0 = r22
            int r2 = r0.b     // Catch:{ Throwable -> 0x01fb }
            r3 = 3
            if (r2 != r3) goto L_0x012c
            r3 = 1
        L_0x00c7:
            r0 = r22
            int r2 = r0.b     // Catch:{ Throwable -> 0x01ff }
            r5 = 2
            if (r2 != r5) goto L_0x012e
            r2 = 1
        L_0x00cf:
            r20 = r2
            r2 = r4
            r4 = r3
            r3 = r20
        L_0x00d5:
            boolean r5 = android.text.TextUtils.isEmpty(r2)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            if (r5 != 0) goto L_0x00e9
            r18 = 0
            long r6 = r16 - r6
            r0 = r18
            long r6 = java.lang.Math.max(r0, r6)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            r5 = 0
            com.loc.l.a(r2, r3, r4, r5, r6)     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
        L_0x00e9:
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            r5.<init>()     // Catch:{ IOException -> 0x007f, all -> 0x01c0 }
            java.io.InputStream r4 = r13.getInputStream()     // Catch:{ IOException -> 0x01e3, all -> 0x01c3 }
            java.io.PushbackInputStream r2 = new java.io.PushbackInputStream     // Catch:{ IOException -> 0x01eb, all -> 0x01c7 }
            r3 = 2
            r2.<init>(r4, r3)     // Catch:{ IOException -> 0x01eb, all -> 0x01c7 }
            r3 = 2
            byte[] r3 = new byte[r3]     // Catch:{ IOException -> 0x01f3, all -> 0x01cc }
            r2.read(r3)     // Catch:{ IOException -> 0x01f3, all -> 0x01cc }
            r2.unread(r3)     // Catch:{ IOException -> 0x01f3, all -> 0x01cc }
            r6 = 0
            byte r6 = r3[r6]     // Catch:{ IOException -> 0x01f3, all -> 0x01cc }
            r7 = 31
            if (r6 != r7) goto L_0x0135
            r6 = 1
            byte r3 = r3[r6]     // Catch:{ IOException -> 0x01f3, all -> 0x01cc }
            r6 = -117(0xffffffffffffff8b, float:NaN)
            if (r3 != r6) goto L_0x0135
            java.util.zip.GZIPInputStream r3 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x01f3, all -> 0x01cc }
            r3.<init>(r2)     // Catch:{ IOException -> 0x01f3, all -> 0x01cc }
        L_0x0114:
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r6 = new byte[r6]     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
        L_0x0118:
            int r7 = r3.read(r6)     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            r9 = -1
            if (r7 == r9) goto L_0x0137
            r9 = 0
            r5.write(r6, r9, r7)     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            goto L_0x0118
        L_0x0124:
            r6 = move-exception
            r6 = r5
            r5 = r4
            r4 = r3
            r3 = r2
            r2 = r8
            goto L_0x0085
        L_0x012c:
            r3 = 0
            goto L_0x00c7
        L_0x012e:
            r2 = 0
            goto L_0x00cf
        L_0x0130:
            r4 = move-exception
        L_0x0131:
            r4 = r2
            r2 = r3
            r3 = r5
            goto L_0x00d5
        L_0x0135:
            r3 = r2
            goto L_0x0114
        L_0x0137:
            com.loc.ab.c()     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            com.loc.ba r6 = new com.loc.ba     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            r6.<init>()     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            byte[] r7 = r5.toByteArray()     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            r6.a = r7     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            r6.b = r14     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            r0 = r21
            java.lang.String r7 = r0.i     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            r6.c = r7     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            r6.d = r8     // Catch:{ IOException -> 0x0124, all -> 0x01d3 }
            if (r5 == 0) goto L_0x0154
            r5.close()     // Catch:{ Throwable -> 0x0194 }
        L_0x0154:
            if (r4 == 0) goto L_0x0159
            r4.close()     // Catch:{ Throwable -> 0x019f }
        L_0x0159:
            if (r2 == 0) goto L_0x015e
            r2.close()     // Catch:{ Throwable -> 0x01aa }
        L_0x015e:
            if (r3 == 0) goto L_0x0163
            r3.close()     // Catch:{ Throwable -> 0x01b5 }
        L_0x0163:
            return r6
        L_0x0164:
            r3 = move-exception
            java.lang.String r4 = "ht"
            java.lang.String r5 = "par"
            com.loc.y.a((java.lang.Throwable) r3, (java.lang.String) r4, (java.lang.String) r5)
            goto L_0x009c
        L_0x0170:
            r3 = move-exception
            java.lang.String r4 = "ht"
            java.lang.String r5 = "par"
            com.loc.y.a((java.lang.Throwable) r3, (java.lang.String) r4, (java.lang.String) r5)
            goto L_0x00a1
        L_0x017c:
            r3 = move-exception
            java.lang.String r4 = "ht"
            java.lang.String r5 = "par"
            com.loc.y.a((java.lang.Throwable) r3, (java.lang.String) r4, (java.lang.String) r5)
            goto L_0x00a6
        L_0x0188:
            r3 = move-exception
            java.lang.String r4 = "ht"
            java.lang.String r5 = "par"
            com.loc.y.a((java.lang.Throwable) r3, (java.lang.String) r4, (java.lang.String) r5)
            goto L_0x00ab
        L_0x0194:
            r5 = move-exception
            java.lang.String r7 = "ht"
            java.lang.String r8 = "par"
            com.loc.y.a((java.lang.Throwable) r5, (java.lang.String) r7, (java.lang.String) r8)
            goto L_0x0154
        L_0x019f:
            r4 = move-exception
            java.lang.String r5 = "ht"
            java.lang.String r7 = "par"
            com.loc.y.a((java.lang.Throwable) r4, (java.lang.String) r5, (java.lang.String) r7)
            goto L_0x0159
        L_0x01aa:
            r2 = move-exception
            java.lang.String r4 = "ht"
            java.lang.String r5 = "par"
            com.loc.y.a((java.lang.Throwable) r2, (java.lang.String) r4, (java.lang.String) r5)
            goto L_0x015e
        L_0x01b5:
            r2 = move-exception
            java.lang.String r3 = "ht"
            java.lang.String r4 = "par"
            com.loc.y.a((java.lang.Throwable) r2, (java.lang.String) r3, (java.lang.String) r4)
            goto L_0x0163
        L_0x01c0:
            r2 = move-exception
            goto L_0x0097
        L_0x01c3:
            r2 = move-exception
            r12 = r5
            goto L_0x0097
        L_0x01c7:
            r2 = move-exception
            r11 = r4
            r12 = r5
            goto L_0x0097
        L_0x01cc:
            r3 = move-exception
            r9 = r2
            r11 = r4
            r12 = r5
            r2 = r3
            goto L_0x0097
        L_0x01d3:
            r6 = move-exception
            r9 = r2
            r10 = r3
            r11 = r4
            r12 = r5
            r2 = r6
            goto L_0x0097
        L_0x01db:
            r2 = move-exception
            r2 = r3
            r4 = r10
            r5 = r11
            r6 = r12
            r3 = r9
            goto L_0x0085
        L_0x01e3:
            r2 = move-exception
            r2 = r8
            r3 = r9
            r4 = r10
            r6 = r5
            r5 = r11
            goto L_0x0085
        L_0x01eb:
            r2 = move-exception
            r2 = r8
            r3 = r9
            r6 = r5
            r5 = r4
            r4 = r10
            goto L_0x0085
        L_0x01f3:
            r3 = move-exception
            r3 = r2
            r6 = r5
            r5 = r4
            r2 = r8
            r4 = r10
            goto L_0x0085
        L_0x01fb:
            r3 = move-exception
            r3 = r4
            goto L_0x0131
        L_0x01ff:
            r2 = move-exception
            r2 = r3
            r3 = r4
            goto L_0x0131
        L_0x0204:
            r8 = r3
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ay.a(com.loc.ay$a):com.loc.ba");
    }

    private static String a(String str, String str2, int i2) {
        if (!(i2 == 2 || i2 == 4)) {
            return str;
        }
        try {
            return !TextUtils.isEmpty(str2) ? str2 : str;
        } catch (Throwable th) {
            return str;
        }
    }

    static String a(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            String str2 = (String) next.getValue();
            if (str2 == null) {
                str2 = "";
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(str));
            sb.append("=");
            sb.append(URLEncoder.encode(str2));
        }
        return sb.toString();
    }

    private void a(Map<String, String> map, HttpURLConnection httpURLConnection) {
        if (map != null) {
            for (String next : map.keySet()) {
                httpURLConnection.addRequestProperty(next, map.get(next));
            }
        }
        try {
            httpURLConnection.addRequestProperty("csid", this.i);
        } catch (Throwable th) {
            y.a(th, "ht", "adh");
        }
        httpURLConnection.setConnectTimeout(this.a);
        httpURLConnection.setReadTimeout(this.b);
    }

    public static boolean a(int i2) {
        return i2 == 2;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x019f, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x01ac, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x01cd, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x01d0, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x01d3, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x01d4, code lost:
        com.loc.y.a(r2, "ht", "mPt");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:?, code lost:
        com.loc.l.a(r13, r3, r4, true, java.lang.Math.max(0, 0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x01fd, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x01fe, code lost:
        r8 = r2;
        r9 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x011e, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x011f, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0137, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0142, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:?, code lost:
        r9.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x015d, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x016e, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x017b, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x0188, code lost:
        r2 = e;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x019f A[Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd, all -> 0x016a }, ExcHandler: IOException (e java.io.IOException), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01ac A[Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd, all -> 0x016a }, ExcHandler: j (e com.loc.j), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x01cd  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01d0  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x01fd A[ExcHandler: all (r2v0 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x011e A[ExcHandler: ConnectException (e java.net.ConnectException), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0137  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0142  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x014b A[SYNTHETIC, Splitter:B:77:0x014b] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x015d A[ExcHandler: MalformedURLException (e java.net.MalformedURLException), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x016e A[Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd, all -> 0x016a }, ExcHandler: UnknownHostException (e java.net.UnknownHostException), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x017b A[Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd, all -> 0x016a }, ExcHandler: SocketException (e java.net.SocketException), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0188 A[Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd, all -> 0x016a }, ExcHandler: SocketTimeoutException (e java.net.SocketTimeoutException), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0195 A[Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd, all -> 0x016a }, ExcHandler: InterruptedIOException (e java.io.InterruptedIOException), Splitter:B:1:0x0001] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.loc.ba a(java.lang.String r13, java.lang.String r14, boolean r15, java.lang.String r16, java.util.Map<java.lang.String, java.lang.String> r17, byte[] r18, int r19) throws com.loc.j {
        /*
            r12 = this;
            r4 = 0
            com.loc.n.c()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r2 = 0
            r0 = r19
            java.lang.String r3 = a(r13, r14, r0)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            switch(r19) {
                case 2: goto L_0x00f7;
                case 3: goto L_0x0114;
                default: goto L_0x000e;
            }     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x000e:
            if (r17 != 0) goto L_0x0015
            java.util.HashMap r17 = new java.util.HashMap     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r17.<init>()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x0015:
            com.loc.ay$b r5 = r12.j     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            com.loc.ay$c r5 = r5.a()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r15 == 0) goto L_0x0221
            boolean r6 = android.text.TextUtils.isEmpty(r16)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r6 != 0) goto L_0x0221
            com.loc.ay$b r5 = r12.j     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r0 = r16
            com.loc.ay$c r5 = r5.a(r0)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r6 = r5
        L_0x002c:
            int r7 = com.loc.aw.a     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            java.lang.String r5 = ""
            switch(r7) {
                case 1: goto L_0x014f;
                default: goto L_0x0034;
            }     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x0034:
            boolean r7 = android.text.TextUtils.isEmpty(r5)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r7 != 0) goto L_0x0065
            android.net.Uri r3 = android.net.Uri.parse(r3)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            java.lang.String r7 = r3.getHost()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            android.net.Uri$Builder r3 = r3.buildUpon()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            android.net.Uri$Builder r3 = r3.encodedAuthority(r5)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            android.net.Uri r3 = r3.build()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            java.lang.String r3 = r3.toString()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r17 == 0) goto L_0x005c
            java.lang.String r8 = "targetHost"
            r0 = r17
            r0.put(r8, r7)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x005c:
            boolean r7 = r12.c     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r7 == 0) goto L_0x0065
            com.loc.ay$b r7 = r12.j     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r7.b(r5)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x0065:
            boolean r5 = r12.c     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r5 == 0) goto L_0x006d
            java.lang.String r3 = com.loc.p.a((java.lang.String) r3)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x006d:
            java.net.URL r5 = new java.net.URL     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r5.<init>(r3)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            com.loc.aw$a r3 = r12.k     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r3 == 0) goto L_0x007c
            com.loc.aw$a r2 = r12.k     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            java.net.URLConnection r2 = r2.a()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x007c:
            if (r2 != 0) goto L_0x0088
            java.net.Proxy r2 = r12.e     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r2 == 0) goto L_0x0153
            java.net.Proxy r2 = r12.e     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            java.net.URLConnection r2 = r5.openConnection(r2)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x0088:
            boolean r3 = r12.c     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r3 == 0) goto L_0x0159
            javax.net.ssl.HttpsURLConnection r2 = (javax.net.ssl.HttpsURLConnection) r2     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r0 = r2
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r3 = r0
            javax.net.ssl.SSLContext r5 = r12.d     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            javax.net.ssl.SSLSocketFactory r5 = r5.getSocketFactory()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r3.setSSLSocketFactory(r5)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r0 = r2
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r3 = r0
            r3.setHostnameVerifier(r6)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x00a2:
            java.lang.String r3 = android.os.Build.VERSION.SDK     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r3 == 0) goto L_0x00b5
            int r3 = android.os.Build.VERSION.SDK_INT     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r5 = 13
            if (r3 <= r5) goto L_0x00b5
            java.lang.String r3 = "Connection"
            java.lang.String r5 = "close"
            r2.setRequestProperty(r3, r5)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
        L_0x00b5:
            r0 = r17
            r12.a(r0, r2)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            java.lang.String r3 = "POST"
            r2.setRequestMethod(r3)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r3 = 0
            r2.setUseCaches(r3)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r3 = 1
            r2.setDoInput(r3)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r3 = 1
            r2.setDoOutput(r3)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            com.loc.ay$a r5 = new com.loc.ay$a     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            r0 = r19
            r5.<init>(r2, r0)     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            java.net.HttpURLConnection r3 = r5.a     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            if (r18 == 0) goto L_0x00ed
            r0 = r18
            int r2 = r0.length     // Catch:{ ConnectException -> 0x021e, MalformedURLException -> 0x021a, UnknownHostException -> 0x0216, SocketException -> 0x0212, SocketTimeoutException -> 0x020e, InterruptedIOException -> 0x020b, IOException -> 0x0208, j -> 0x0205, Throwable -> 0x0202 }
            if (r2 <= 0) goto L_0x00ed
            java.io.DataOutputStream r2 = new java.io.DataOutputStream     // Catch:{ ConnectException -> 0x021e, MalformedURLException -> 0x021a, UnknownHostException -> 0x0216, SocketException -> 0x0212, SocketTimeoutException -> 0x020e, InterruptedIOException -> 0x020b, IOException -> 0x0208, j -> 0x0205, Throwable -> 0x0202 }
            java.io.OutputStream r4 = r3.getOutputStream()     // Catch:{ ConnectException -> 0x021e, MalformedURLException -> 0x021a, UnknownHostException -> 0x0216, SocketException -> 0x0212, SocketTimeoutException -> 0x020e, InterruptedIOException -> 0x020b, IOException -> 0x0208, j -> 0x0205, Throwable -> 0x0202 }
            r2.<init>(r4)     // Catch:{ ConnectException -> 0x021e, MalformedURLException -> 0x021a, UnknownHostException -> 0x0216, SocketException -> 0x0212, SocketTimeoutException -> 0x020e, InterruptedIOException -> 0x020b, IOException -> 0x0208, j -> 0x0205, Throwable -> 0x0202 }
            r0 = r18
            r2.write(r0)     // Catch:{ ConnectException -> 0x021e, MalformedURLException -> 0x021a, UnknownHostException -> 0x0216, SocketException -> 0x0212, SocketTimeoutException -> 0x020e, InterruptedIOException -> 0x020b, IOException -> 0x0208, j -> 0x0205, Throwable -> 0x0202 }
            r2.close()     // Catch:{ ConnectException -> 0x021e, MalformedURLException -> 0x021a, UnknownHostException -> 0x0216, SocketException -> 0x0212, SocketTimeoutException -> 0x020e, InterruptedIOException -> 0x020b, IOException -> 0x0208, j -> 0x0205, Throwable -> 0x0202 }
        L_0x00ed:
            com.loc.ba r2 = r12.a((com.loc.ay.a) r5)     // Catch:{ ConnectException -> 0x021e, MalformedURLException -> 0x021a, UnknownHostException -> 0x0216, SocketException -> 0x0212, SocketTimeoutException -> 0x020e, InterruptedIOException -> 0x020b, IOException -> 0x0208, j -> 0x0205, Throwable -> 0x0202 }
            if (r3 == 0) goto L_0x00f6
            r3.disconnect()     // Catch:{ Throwable -> 0x01f1 }
        L_0x00f6:
            return r2
        L_0x00f7:
            int r5 = r12.a     // Catch:{ Throwable -> 0x0111, ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, all -> 0x01fd }
            int r5 = r5 + -5000
            r6 = 5000(0x1388, float:7.006E-42)
            int r5 = java.lang.Math.max(r5, r6)     // Catch:{ Throwable -> 0x0111, ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, all -> 0x01fd }
            r12.a = r5     // Catch:{ Throwable -> 0x0111, ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, all -> 0x01fd }
            int r5 = r12.b     // Catch:{ Throwable -> 0x0111, ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, all -> 0x01fd }
            int r5 = r5 + -5000
            r6 = 5000(0x1388, float:7.006E-42)
            int r5 = java.lang.Math.max(r5, r6)     // Catch:{ Throwable -> 0x0111, ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, all -> 0x01fd }
            r12.b = r5     // Catch:{ Throwable -> 0x0111, ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, all -> 0x01fd }
            goto L_0x000e
        L_0x0111:
            r5 = move-exception
            goto L_0x000e
        L_0x0114:
            r5 = 5000(0x1388, float:7.006E-42)
            r12.a = r5     // Catch:{ Throwable -> 0x0111, ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, all -> 0x01fd }
            r5 = 5000(0x1388, float:7.006E-42)
            r12.b = r5     // Catch:{ Throwable -> 0x0111, ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, all -> 0x01fd }
            goto L_0x000e
        L_0x011e:
            r2 = move-exception
            r3 = r4
        L_0x0120:
            r2.printStackTrace()     // Catch:{ all -> 0x012c }
            com.loc.j r2 = new com.loc.j     // Catch:{ all -> 0x012c }
            java.lang.String r4 = "http连接失败 - ConnectionException"
            r2.<init>(r4)     // Catch:{ all -> 0x012c }
            throw r2     // Catch:{ all -> 0x012c }
        L_0x012c:
            r2 = move-exception
            r8 = r2
            r9 = r3
        L_0x012f:
            com.loc.l.a((int) r19)     // Catch:{ Throwable -> 0x01ca }
            r2 = 3
            r0 = r19
            if (r0 != r2) goto L_0x01cd
            r4 = 1
        L_0x0138:
            r2 = 2
            r0 = r19
            if (r0 == r2) goto L_0x0142
            r2 = 4
            r0 = r19
            if (r0 != r2) goto L_0x01d0
        L_0x0142:
            r3 = 1
        L_0x0143:
            boolean r2 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Throwable -> 0x01ee }
            if (r2 == 0) goto L_0x01df
        L_0x0149:
            if (r9 == 0) goto L_0x014e
            r9.disconnect()     // Catch:{ Throwable -> 0x01d3 }
        L_0x014e:
            throw r8
        L_0x014f:
            java.lang.String r5 = com.loc.aw.b     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            goto L_0x0034
        L_0x0153:
            java.net.URLConnection r2 = r5.openConnection()     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            goto L_0x0088
        L_0x0159:
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ ConnectException -> 0x011e, MalformedURLException -> 0x015d, UnknownHostException -> 0x016e, SocketException -> 0x017b, SocketTimeoutException -> 0x0188, InterruptedIOException -> 0x0195, IOException -> 0x019f, j -> 0x01ac, Throwable -> 0x01b7, all -> 0x01fd }
            goto L_0x00a2
        L_0x015d:
            r2 = move-exception
        L_0x015e:
            r2.printStackTrace()     // Catch:{ all -> 0x016a }
            com.loc.j r2 = new com.loc.j     // Catch:{ all -> 0x016a }
            java.lang.String r3 = "url异常 - MalformedURLException"
            r2.<init>(r3)     // Catch:{ all -> 0x016a }
            throw r2     // Catch:{ all -> 0x016a }
        L_0x016a:
            r2 = move-exception
            r8 = r2
            r9 = r4
            goto L_0x012f
        L_0x016e:
            r2 = move-exception
        L_0x016f:
            r2.printStackTrace()     // Catch:{ all -> 0x016a }
            com.loc.j r2 = new com.loc.j     // Catch:{ all -> 0x016a }
            java.lang.String r3 = "未知主机 - UnKnowHostException"
            r2.<init>(r3)     // Catch:{ all -> 0x016a }
            throw r2     // Catch:{ all -> 0x016a }
        L_0x017b:
            r2 = move-exception
        L_0x017c:
            r2.printStackTrace()     // Catch:{ all -> 0x016a }
            com.loc.j r2 = new com.loc.j     // Catch:{ all -> 0x016a }
            java.lang.String r3 = "socket 连接异常 - SocketException"
            r2.<init>(r3)     // Catch:{ all -> 0x016a }
            throw r2     // Catch:{ all -> 0x016a }
        L_0x0188:
            r2 = move-exception
        L_0x0189:
            r2.printStackTrace()     // Catch:{ all -> 0x016a }
            com.loc.j r2 = new com.loc.j     // Catch:{ all -> 0x016a }
            java.lang.String r3 = "socket 连接超时 - SocketTimeoutException"
            r2.<init>(r3)     // Catch:{ all -> 0x016a }
            throw r2     // Catch:{ all -> 0x016a }
        L_0x0195:
            r2 = move-exception
        L_0x0196:
            com.loc.j r2 = new com.loc.j     // Catch:{ all -> 0x016a }
            java.lang.String r3 = "未知的错误"
            r2.<init>(r3)     // Catch:{ all -> 0x016a }
            throw r2     // Catch:{ all -> 0x016a }
        L_0x019f:
            r2 = move-exception
        L_0x01a0:
            r2.printStackTrace()     // Catch:{ all -> 0x016a }
            com.loc.j r2 = new com.loc.j     // Catch:{ all -> 0x016a }
            java.lang.String r3 = "IO 操作异常 - IOException"
            r2.<init>(r3)     // Catch:{ all -> 0x016a }
            throw r2     // Catch:{ all -> 0x016a }
        L_0x01ac:
            r2 = move-exception
        L_0x01ad:
            java.lang.String r3 = "ht"
            java.lang.String r5 = "mPt"
            com.loc.y.a((java.lang.Throwable) r2, (java.lang.String) r3, (java.lang.String) r5)     // Catch:{ all -> 0x016a }
            throw r2     // Catch:{ all -> 0x016a }
        L_0x01b7:
            r2 = move-exception
        L_0x01b8:
            java.lang.String r3 = "ht"
            java.lang.String r5 = "mPt"
            com.loc.y.a((java.lang.Throwable) r2, (java.lang.String) r3, (java.lang.String) r5)     // Catch:{ all -> 0x016a }
            com.loc.j r2 = new com.loc.j     // Catch:{ all -> 0x016a }
            java.lang.String r3 = "未知的错误"
            r2.<init>(r3)     // Catch:{ all -> 0x016a }
            throw r2     // Catch:{ all -> 0x016a }
        L_0x01ca:
            r2 = move-exception
            goto L_0x0149
        L_0x01cd:
            r4 = 0
            goto L_0x0138
        L_0x01d0:
            r3 = 0
            goto L_0x0143
        L_0x01d3:
            r2 = move-exception
            java.lang.String r3 = "ht"
            java.lang.String r4 = "mPt"
            com.loc.y.a((java.lang.Throwable) r2, (java.lang.String) r3, (java.lang.String) r4)
            goto L_0x014e
        L_0x01df:
            r6 = 0
            r10 = 0
            long r6 = java.lang.Math.max(r6, r10)     // Catch:{ Throwable -> 0x01ee }
            r5 = 1
            r2 = r13
            com.loc.l.a(r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x01ee }
            goto L_0x0149
        L_0x01ee:
            r2 = move-exception
            goto L_0x0149
        L_0x01f1:
            r3 = move-exception
            java.lang.String r4 = "ht"
            java.lang.String r5 = "mPt"
            com.loc.y.a((java.lang.Throwable) r3, (java.lang.String) r4, (java.lang.String) r5)
            goto L_0x00f6
        L_0x01fd:
            r2 = move-exception
            r8 = r2
            r9 = r4
            goto L_0x012f
        L_0x0202:
            r2 = move-exception
            r4 = r3
            goto L_0x01b8
        L_0x0205:
            r2 = move-exception
            r4 = r3
            goto L_0x01ad
        L_0x0208:
            r2 = move-exception
            r4 = r3
            goto L_0x01a0
        L_0x020b:
            r2 = move-exception
            r4 = r3
            goto L_0x0196
        L_0x020e:
            r2 = move-exception
            r4 = r3
            goto L_0x0189
        L_0x0212:
            r2 = move-exception
            r4 = r3
            goto L_0x017c
        L_0x0216:
            r2 = move-exception
            r4 = r3
            goto L_0x016f
        L_0x021a:
            r2 = move-exception
            r4 = r3
            goto L_0x015e
        L_0x021e:
            r2 = move-exception
            goto L_0x0120
        L_0x0221:
            r6 = r5
            goto L_0x002c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ay.a(java.lang.String, java.lang.String, boolean, java.lang.String, java.util.Map, byte[], int):com.loc.ba");
    }
}
