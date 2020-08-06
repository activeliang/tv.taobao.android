package com.uc.webview.export.utility.download;

import android.content.Context;
import android.webkit.ValueCallback;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.codec.language.Soundex;

@Api
/* compiled from: ProGuard */
public class DownloadTask implements Runnable {
    private static final ConcurrentHashMap<Integer, Integer> a = new ConcurrentHashMap<>();
    private final Object[] b = new Object[3];
    private final ValueCallback<DownloadTask>[] c = new ValueCallback[9];
    /* access modifiers changed from: private */
    public final String[] d = new String[3];
    private final long[] e = new long[3];
    private ValueCallback<Object[]> f;

    public DownloadTask(Context context, String str, ValueCallback<Object[]> valueCallback) {
        int hashCode = str.hashCode();
        this.b[2] = context;
        synchronized (a) {
            if (a.containsKey(Integer.valueOf(hashCode))) {
                throw new RuntimeException("Duplicate task.");
            }
            a.put(Integer.valueOf(hashCode), Integer.valueOf(hashCode));
        }
        String valueOf = hashCode >= 0 ? String.valueOf(hashCode) : String.valueOf(hashCode).replace(Soundex.SILENT_MARKER, '_');
        this.f = valueCallback;
        IWaStat.WaStat.stat(IWaStat.DOWNLOAD);
        this.d[0] = str;
        this.d[1] = context.getApplicationContext().getCacheDir().getAbsolutePath() + "/ucdown" + valueOf;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        try {
            int hashCode = this.d[0].hashCode();
            synchronized (a) {
                a.remove(Integer.valueOf(hashCode));
            }
        } catch (Throwable th) {
        }
    }

    public DownloadTask onEvent(String str, ValueCallback<DownloadTask> valueCallback) {
        if (str.equals(BlitzServiceUtils.CSUCCESS)) {
            this.c[0] = valueCallback;
        } else if (str.equals("failed")) {
            this.c[1] = valueCallback;
        } else if (str.equals("recovered")) {
            this.c[2] = valueCallback;
        } else if (str.equals("progress")) {
            this.c[3] = valueCallback;
        } else if (str.equals("exception")) {
            this.c[4] = valueCallback;
        } else if (str.equals("check")) {
            this.c[5] = valueCallback;
        } else if (str.equals("header")) {
            this.c[6] = valueCallback;
        } else if (str.equals("exists")) {
            this.c[7] = valueCallback;
        } else if (str.equals("beginDownload")) {
            this.c[8] = valueCallback;
        } else {
            throw new RuntimeException("The given event:" + str + " is unknown.");
        }
        return this;
    }

    public Throwable getException() {
        return (Throwable) this.b[1];
    }

    public String getFilePath() {
        return this.d[2];
    }

    public long getTotalSize() {
        return this.e[0];
    }

    public long getCurrentSize() {
        return this.e[1];
    }

    public long getLastModified() {
        return this.e[2];
    }

    public DownloadTask start() {
        this.b[0] = new Thread(this);
        ((Thread) this.b[0]).start();
        return this;
    }

    public DownloadTask stop() {
        this.b[0] = null;
        return this;
    }

    public DownloadTask stopWith(Runnable runnable) {
        this.b[0] = new Thread(new a(this, runnable));
        ((Thread) this.b[0]).start();
        return this;
    }

    public DownloadTask planWith(Runnable runnable) {
        new Thread(new b(this, runnable)).start();
        return this;
    }

    public DownloadTask delete() {
        this.b[0] = new Thread(new c(this));
        ((Thread) this.b[0]).start();
        return this;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0229, code lost:
        if (r22.e[1] > r16) goto L_0x022b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x0220  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0205 A[Catch:{ all -> 0x02ad, Throwable -> 0x030a, all -> 0x02b2 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:113:0x0241=Splitter:B:113:0x0241, B:47:0x0144=Splitter:B:47:0x0144, B:143:0x02b9=Splitter:B:143:0x02b9, B:86:0x01eb=Splitter:B:86:0x01eb, B:78:0x019d=Splitter:B:78:0x019d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void run() {
        /*
            r22 = this;
            monitor-enter(r22)
            android.os.Looper r2 = android.os.Looper.myLooper()     // Catch:{ all -> 0x0014 }
            android.os.Looper r3 = android.os.Looper.getMainLooper()     // Catch:{ all -> 0x0014 }
            if (r2 != r3) goto L_0x0017
            java.lang.RuntimeException r2 = new java.lang.RuntimeException     // Catch:{ all -> 0x0014 }
            java.lang.String r3 = "Download should not run in UI thread."
            r2.<init>(r3)     // Catch:{ all -> 0x0014 }
            throw r2     // Catch:{ all -> 0x0014 }
        L_0x0014:
            r2 = move-exception
            monitor-exit(r22)
            throw r2
        L_0x0017:
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r2 = r0.c     // Catch:{ all -> 0x0014 }
            r3 = 0
            r4 = r2[r3]     // Catch:{ all -> 0x0014 }
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r2 = r0.c     // Catch:{ all -> 0x0014 }
            r3 = 1
            r5 = r2[r3]     // Catch:{ all -> 0x0014 }
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r2 = r0.c     // Catch:{ all -> 0x0014 }
            r3 = 2
            r6 = r2[r3]     // Catch:{ all -> 0x0014 }
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r2 = r0.c     // Catch:{ all -> 0x0014 }
            r3 = 3
            r7 = r2[r3]     // Catch:{ all -> 0x0014 }
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r2 = r0.c     // Catch:{ all -> 0x0014 }
            r3 = 4
            r8 = r2[r3]     // Catch:{ all -> 0x0014 }
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r2 = r0.c     // Catch:{ all -> 0x0014 }
            r3 = 5
            r2 = r2[r3]     // Catch:{ all -> 0x0014 }
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r3 = r0.c     // Catch:{ all -> 0x0014 }
            r9 = 6
            r9 = r3[r9]     // Catch:{ all -> 0x0014 }
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r3 = r0.c     // Catch:{ all -> 0x0014 }
            r10 = 7
            r10 = r3[r10]     // Catch:{ all -> 0x0014 }
            r0 = r22
            android.webkit.ValueCallback<com.uc.webview.export.utility.download.DownloadTask>[] r3 = r0.c     // Catch:{ all -> 0x0014 }
            r11 = 8
            r11 = r3[r11]     // Catch:{ all -> 0x0014 }
            if (r2 == 0) goto L_0x005e
            r0 = r22
            r2.onReceiveValue(r0)     // Catch:{ Throwable -> 0x0145 }
        L_0x005e:
            r0 = r22
            java.lang.Object[] r2 = r0.b     // Catch:{ Throwable -> 0x0145 }
            r3 = 1
            r12 = 0
            r2[r3] = r12     // Catch:{ Throwable -> 0x0145 }
            r0 = r22
            java.lang.String[] r2 = r0.d     // Catch:{ Throwable -> 0x0145 }
            r3 = 0
            r12 = r2[r3]     // Catch:{ Throwable -> 0x0145 }
            r0 = r22
            java.lang.String[] r2 = r0.d     // Catch:{ Throwable -> 0x0145 }
            r3 = 1
            r13 = r2[r3]     // Catch:{ Throwable -> 0x0145 }
            r0 = r22
            android.webkit.ValueCallback<java.lang.Object[]> r2 = r0.f     // Catch:{ Throwable -> 0x031b, Exception -> 0x0118 }
            if (r2 == 0) goto L_0x008c
            r0 = r22
            android.webkit.ValueCallback<java.lang.Object[]> r2 = r0.f     // Catch:{ Throwable -> 0x031b, Exception -> 0x0118 }
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x031b, Exception -> 0x0118 }
            r14 = 0
            r15 = 1
            java.lang.Integer r15 = java.lang.Integer.valueOf(r15)     // Catch:{ Throwable -> 0x031b, Exception -> 0x0118 }
            r3[r14] = r15     // Catch:{ Throwable -> 0x031b, Exception -> 0x0118 }
            r2.onReceiveValue(r3)     // Catch:{ Throwable -> 0x031b, Exception -> 0x0118 }
        L_0x008c:
            r2 = 0
            android.util.Pair r3 = com.uc.webview.export.internal.utility.c.a((java.lang.String) r12, (java.net.URL) r2)     // Catch:{ Exception -> 0x0118 }
            r0 = r22
            long[] r14 = r0.e     // Catch:{ Throwable -> 0x0145 }
            r15 = 0
            java.lang.Object r2 = r3.first     // Catch:{ Throwable -> 0x0145 }
            java.lang.Long r2 = (java.lang.Long) r2     // Catch:{ Throwable -> 0x0145 }
            long r16 = r2.longValue()     // Catch:{ Throwable -> 0x0145 }
            r14[r15] = r16     // Catch:{ Throwable -> 0x0145 }
            r0 = r22
            long[] r14 = r0.e     // Catch:{ Throwable -> 0x0145 }
            r15 = 2
            java.lang.Object r2 = r3.second     // Catch:{ Throwable -> 0x0145 }
            java.lang.Long r2 = (java.lang.Long) r2     // Catch:{ Throwable -> 0x0145 }
            long r2 = r2.longValue()     // Catch:{ Throwable -> 0x0145 }
            r14[r15] = r2     // Catch:{ Throwable -> 0x0145 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0145 }
            r14.<init>()     // Catch:{ Throwable -> 0x0145 }
            r0 = r16
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r15 = "_"
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x0145 }
            java.lang.StringBuilder r2 = r14.append(r2)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r14 = r2.toString()     // Catch:{ Throwable -> 0x0145 }
            r0 = r22
            java.lang.String[] r2 = r0.d     // Catch:{ Throwable -> 0x0145 }
            r3 = 2
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0145 }
            r15.<init>()     // Catch:{ Throwable -> 0x0145 }
            java.lang.StringBuilder r15 = r15.append(r13)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r18 = "/"
            r0 = r18
            java.lang.StringBuilder r15 = r15.append(r0)     // Catch:{ Throwable -> 0x0145 }
            java.lang.StringBuilder r15 = r15.append(r14)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r15 = r15.toString()     // Catch:{ Throwable -> 0x0145 }
            r2[r3] = r15     // Catch:{ Throwable -> 0x0145 }
            java.io.File r18 = new java.io.File     // Catch:{ Throwable -> 0x0145 }
            r0 = r18
            r0.<init>(r15)     // Catch:{ Throwable -> 0x0145 }
            r0 = r22
            long[] r2 = r0.e     // Catch:{ Throwable -> 0x0145 }
            r3 = 1
            long r20 = r18.length()     // Catch:{ Throwable -> 0x0145 }
            r2[r3] = r20     // Catch:{ Throwable -> 0x0145 }
            int r2 = (r20 > r16 ? 1 : (r20 == r16 ? 0 : -1))
            if (r2 == 0) goto L_0x015d
            r2 = 1
            r3 = r2
        L_0x0102:
            if (r9 == 0) goto L_0x0109
            r0 = r22
            r9.onReceiveValue(r0)     // Catch:{ Throwable -> 0x02fb }
        L_0x0109:
            r0 = r22
            java.lang.Object[] r2 = r0.b     // Catch:{ Throwable -> 0x0145 }
            r9 = 0
            r2 = r2[r9]     // Catch:{ Throwable -> 0x0145 }
            java.lang.Thread r9 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0145 }
            if (r2 == r9) goto L_0x0160
        L_0x0116:
            monitor-exit(r22)
            return
        L_0x0118:
            r2 = move-exception
            r0 = r22
            android.webkit.ValueCallback<java.lang.Object[]> r3 = r0.f     // Catch:{ Throwable -> 0x0318 }
            if (r3 == 0) goto L_0x0144
            r0 = r22
            android.webkit.ValueCallback<java.lang.Object[]> r3 = r0.f     // Catch:{ Throwable -> 0x0318 }
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0318 }
            r5 = 0
            r6 = 6
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0318 }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0318 }
            r5 = 1
            java.lang.Class r6 = r2.getClass()     // Catch:{ Throwable -> 0x0318 }
            java.lang.String r6 = r6.getName()     // Catch:{ Throwable -> 0x0318 }
            int r6 = r6.hashCode()     // Catch:{ Throwable -> 0x0318 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0318 }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0318 }
            r3.onReceiveValue(r4)     // Catch:{ Throwable -> 0x0318 }
        L_0x0144:
            throw r2     // Catch:{ Throwable -> 0x0145 }
        L_0x0145:
            r2 = move-exception
            java.lang.String r3 = "sdk_dl_e"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r3)     // Catch:{ all -> 0x0014 }
            r0 = r22
            java.lang.Object[] r3 = r0.b     // Catch:{ all -> 0x0014 }
            r4 = 1
            r3[r4] = r2     // Catch:{ all -> 0x0014 }
            if (r8 == 0) goto L_0x0116
            r0 = r22
            r8.onReceiveValue(r0)     // Catch:{ Throwable -> 0x015b }
            goto L_0x0116
        L_0x015b:
            r2 = move-exception
            goto L_0x0116
        L_0x015d:
            r2 = 0
            r3 = r2
            goto L_0x0102
        L_0x0160:
            r0 = r22
            android.webkit.ValueCallback<java.lang.Object[]> r2 = r0.f     // Catch:{ Throwable -> 0x0315 }
            if (r2 == 0) goto L_0x0179
            r0 = r22
            android.webkit.ValueCallback<java.lang.Object[]> r2 = r0.f     // Catch:{ Throwable -> 0x0315 }
            r9 = 1
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0315 }
            r15 = 0
            r19 = 2
            java.lang.Integer r19 = java.lang.Integer.valueOf(r19)     // Catch:{ Throwable -> 0x0315 }
            r9[r15] = r19     // Catch:{ Throwable -> 0x0315 }
            r2.onReceiveValue(r9)     // Catch:{ Throwable -> 0x0315 }
        L_0x0179:
            if (r3 == 0) goto L_0x02e9
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x0145 }
            r2.<init>(r13)     // Catch:{ Throwable -> 0x0145 }
            boolean r9 = r2.exists()     // Catch:{ Throwable -> 0x0145 }
            if (r9 != 0) goto L_0x028e
            r2.mkdirs()     // Catch:{ Throwable -> 0x0145 }
        L_0x0189:
            boolean r2 = r18.exists()     // Catch:{ Throwable -> 0x0145 }
            if (r2 != 0) goto L_0x0192
            r18.createNewFile()     // Catch:{ Throwable -> 0x0145 }
        L_0x0192:
            int r2 = (r20 > r16 ? 1 : (r20 == r16 ? 0 : -1))
            if (r2 >= 0) goto L_0x02bd
            if (r11 == 0) goto L_0x019d
            r0 = r22
            r11.onReceiveValue(r0)     // Catch:{ Exception -> 0x02fe }
        L_0x019d:
            java.net.URL r2 = new java.net.URL     // Catch:{ Throwable -> 0x0145 }
            r2.<init>(r12)     // Catch:{ Throwable -> 0x0145 }
            java.net.URLConnection r2 = r2.openConnection()     // Catch:{ Throwable -> 0x0145 }
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ Throwable -> 0x0145 }
            r9 = 3000(0xbb8, float:4.204E-42)
            r2.setConnectTimeout(r9)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r9 = "GET"
            r2.setRequestMethod(r9)     // Catch:{ Throwable -> 0x0145 }
            r10 = 0
            int r9 = (r20 > r10 ? 1 : (r20 == r10 ? 0 : -1))
            if (r9 <= 0) goto L_0x01eb
            java.lang.String r9 = "Range"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r11 = "bytes="
            r10.<init>(r11)     // Catch:{ Throwable -> 0x0145 }
            r0 = r20
            java.lang.StringBuilder r10 = r10.append(r0)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r11 = "-"
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ Throwable -> 0x0145 }
            r0 = r16
            java.lang.StringBuilder r10 = r10.append(r0)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x0145 }
            r2.setRequestProperty(r9, r10)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r9 = "sdk_dl_r"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r9)     // Catch:{ Throwable -> 0x0145 }
            if (r6 == 0) goto L_0x01eb
            r0 = r22
            r6.onReceiveValue(r0)     // Catch:{ Throwable -> 0x0301 }
        L_0x01eb:
            r2.connect()     // Catch:{ Throwable -> 0x0145 }
            java.io.InputStream r6 = r2.getInputStream()     // Catch:{ Throwable -> 0x0145 }
            java.io.FileOutputStream r9 = new java.io.FileOutputStream     // Catch:{ all -> 0x02b2 }
            r10 = 1
            r0 = r18
            r9.<init>(r0, r10)     // Catch:{ all -> 0x02b2 }
            r10 = 51200(0xc800, float:7.1746E-41)
            byte[] r10 = new byte[r10]     // Catch:{ all -> 0x02ad }
        L_0x01ff:
            int r11 = r6.read(r10)     // Catch:{ all -> 0x02ad }
            if (r11 <= 0) goto L_0x021e
            r12 = 0
            r9.write(r10, r12, r11)     // Catch:{ all -> 0x02ad }
            r0 = r22
            long[] r12 = r0.e     // Catch:{ all -> 0x02ad }
            r13 = 1
            r14 = r12[r13]     // Catch:{ all -> 0x02ad }
            long r0 = (long) r11     // Catch:{ all -> 0x02ad }
            r20 = r0
            long r14 = r14 + r20
            r12[r13] = r14     // Catch:{ all -> 0x02ad }
            if (r7 == 0) goto L_0x021e
            r0 = r22
            r7.onReceiveValue(r0)     // Catch:{ Throwable -> 0x0304 }
        L_0x021e:
            if (r11 <= 0) goto L_0x022b
            r0 = r22
            long[] r11 = r0.e     // Catch:{ all -> 0x02ad }
            r12 = 1
            r12 = r11[r12]     // Catch:{ all -> 0x02ad }
            int r11 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1))
            if (r11 <= 0) goto L_0x022c
        L_0x022b:
            r3 = 0
        L_0x022c:
            if (r3 == 0) goto L_0x023b
            r0 = r22
            java.lang.Object[] r11 = r0.b     // Catch:{ all -> 0x02ad }
            r12 = 0
            r11 = r11[r12]     // Catch:{ all -> 0x02ad }
            java.lang.Thread r12 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x02ad }
            if (r11 == r12) goto L_0x01ff
        L_0x023b:
            r9.close()     // Catch:{ Throwable -> 0x0307 }
        L_0x023e:
            r6.close()     // Catch:{ Throwable -> 0x030c }
        L_0x0241:
            r2.disconnect()     // Catch:{ Throwable -> 0x02ba }
            r2 = r3
        L_0x0245:
            if (r2 != 0) goto L_0x0116
            long r2 = r18.length()     // Catch:{ Throwable -> 0x0145 }
            int r2 = (r16 > r2 ? 1 : (r16 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x02bf
            java.lang.String r2 = "sdk_dl_f"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r2)     // Catch:{ Throwable -> 0x0145 }
            r0 = r22
            java.lang.Object[] r2 = r0.b     // Catch:{ Throwable -> 0x0145 }
            r3 = 1
            java.lang.RuntimeException r4 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x0145 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r7 = "Size mismatch:"
            r6.<init>(r7)     // Catch:{ Throwable -> 0x0145 }
            long r10 = r18.length()     // Catch:{ Throwable -> 0x0145 }
            java.lang.StringBuilder r6 = r6.append(r10)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r7 = "/"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Throwable -> 0x0145 }
            r0 = r16
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0145 }
            r4.<init>(r6)     // Catch:{ Throwable -> 0x0145 }
            r2[r3] = r4     // Catch:{ Throwable -> 0x0145 }
            if (r5 == 0) goto L_0x0116
            r0 = r22
            r5.onReceiveValue(r0)     // Catch:{ Throwable -> 0x028b }
            goto L_0x0116
        L_0x028b:
            r2 = move-exception
            goto L_0x0116
        L_0x028e:
            java.io.File[] r9 = r2.listFiles()     // Catch:{ Throwable -> 0x0145 }
            int r10 = r9.length     // Catch:{ Throwable -> 0x0145 }
            r2 = 0
        L_0x0294:
            if (r2 >= r10) goto L_0x0189
            r13 = r9[r2]     // Catch:{ Throwable -> 0x0145 }
            java.lang.String r15 = r13.getName()     // Catch:{ Throwable -> 0x0145 }
            boolean r15 = r15.equals(r14)     // Catch:{ Throwable -> 0x0145 }
            if (r15 != 0) goto L_0x02aa
            r15 = 0
            r19 = 0
            r0 = r19
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r13, r15, r0)     // Catch:{ Throwable -> 0x0145 }
        L_0x02aa:
            int r2 = r2 + 1
            goto L_0x0294
        L_0x02ad:
            r3 = move-exception
            r9.close()     // Catch:{ Throwable -> 0x030a }
        L_0x02b1:
            throw r3     // Catch:{ all -> 0x02b2 }
        L_0x02b2:
            r3 = move-exception
            r6.close()     // Catch:{ Throwable -> 0x030f }
        L_0x02b6:
            r2.disconnect()     // Catch:{ Throwable -> 0x0311 }
        L_0x02b9:
            throw r3     // Catch:{ Throwable -> 0x0145 }
        L_0x02ba:
            r2 = move-exception
            r2 = r3
            goto L_0x0245
        L_0x02bd:
            r2 = 0
            goto L_0x0245
        L_0x02bf:
            java.lang.String r2 = "sdk_dl_s"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r2)     // Catch:{ Throwable -> 0x0145 }
            if (r4 == 0) goto L_0x02cc
            r0 = r22
            r4.onReceiveValue(r0)     // Catch:{ Throwable -> 0x0313 }
        L_0x02cc:
            r0 = r22
            android.webkit.ValueCallback<java.lang.Object[]> r2 = r0.f     // Catch:{ Throwable -> 0x02e6 }
            if (r2 == 0) goto L_0x0116
            r0 = r22
            android.webkit.ValueCallback<java.lang.Object[]> r2 = r0.f     // Catch:{ Throwable -> 0x02e6 }
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x02e6 }
            r4 = 0
            r5 = 5
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x02e6 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x02e6 }
            r2.onReceiveValue(r3)     // Catch:{ Throwable -> 0x02e6 }
            goto L_0x0116
        L_0x02e6:
            r2 = move-exception
            goto L_0x0116
        L_0x02e9:
            java.lang.String r2 = "sdk_dl_x"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r2)     // Catch:{ Throwable -> 0x0145 }
            if (r10 == 0) goto L_0x0116
            r0 = r22
            r10.onReceiveValue(r0)     // Catch:{ Throwable -> 0x02f8 }
            goto L_0x0116
        L_0x02f8:
            r2 = move-exception
            goto L_0x0116
        L_0x02fb:
            r2 = move-exception
            goto L_0x0109
        L_0x02fe:
            r2 = move-exception
            goto L_0x019d
        L_0x0301:
            r6 = move-exception
            goto L_0x01eb
        L_0x0304:
            r12 = move-exception
            goto L_0x021e
        L_0x0307:
            r7 = move-exception
            goto L_0x023e
        L_0x030a:
            r4 = move-exception
            goto L_0x02b1
        L_0x030c:
            r6 = move-exception
            goto L_0x0241
        L_0x030f:
            r4 = move-exception
            goto L_0x02b6
        L_0x0311:
            r2 = move-exception
            goto L_0x02b9
        L_0x0313:
            r2 = move-exception
            goto L_0x02cc
        L_0x0315:
            r2 = move-exception
            goto L_0x0179
        L_0x0318:
            r3 = move-exception
            goto L_0x0144
        L_0x031b:
            r2 = move-exception
            goto L_0x008c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.utility.download.DownloadTask.run():void");
    }
}
