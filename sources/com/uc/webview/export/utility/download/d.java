package com.uc.webview.export.utility.download;

import android.webkit.ValueCallback;

/* compiled from: ProGuard */
final class d implements Runnable {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ String b;
    final /* synthetic */ DownloadTask c;
    final /* synthetic */ ValueCallback d;
    final /* synthetic */ ValueCallback e;
    final /* synthetic */ ValueCallback f;
    final /* synthetic */ ValueCallback g;
    final /* synthetic */ ValueCallback h;
    final /* synthetic */ UpdateTask i;

    d(UpdateTask updateTask, ValueCallback valueCallback, String str, DownloadTask downloadTask, ValueCallback valueCallback2, ValueCallback valueCallback3, ValueCallback valueCallback4, ValueCallback valueCallback5, ValueCallback valueCallback6) {
        this.i = updateTask;
        this.a = valueCallback;
        this.b = str;
        this.c = downloadTask;
        this.d = valueCallback2;
        this.e = valueCallback3;
        this.f = valueCallback4;
        this.g = valueCallback5;
        this.h = valueCallback6;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0123, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x012a, code lost:
        if (r13.i.h != null) goto L_0x012c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x012c, code lost:
        r13.i.h.onReceiveValue(new java.lang.Object[]{9, java.lang.Integer.valueOf(r0.getClass().getName().hashCode())});
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0123 A[ExcHandler: Exception (r0v13 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:28:0x00ab] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r13 = this;
            r12 = 1
            android.webkit.ValueCallback r0 = r13.a     // Catch:{ Throwable -> 0x019d }
            if (r0 == 0) goto L_0x000c
            android.webkit.ValueCallback r0 = r13.a     // Catch:{ Throwable -> 0x019d }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x019d }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x019d }
        L_0x000c:
            com.uc.webview.export.utility.download.UpdateTask r0 = r13.i     // Catch:{ Throwable -> 0x019a }
            android.webkit.ValueCallback r0 = r0.h     // Catch:{ Throwable -> 0x019a }
            if (r0 == 0) goto L_0x0028
            com.uc.webview.export.utility.download.UpdateTask r0 = r13.i     // Catch:{ Throwable -> 0x019a }
            android.webkit.ValueCallback r0 = r0.h     // Catch:{ Throwable -> 0x019a }
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x019a }
            r2 = 0
            r3 = 3
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x019a }
            r1[r2] = r3     // Catch:{ Throwable -> 0x019a }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x019a }
        L_0x0028:
            java.lang.String r0 = r13.b     // Catch:{ Throwable -> 0x0155 }
            java.lang.String r0 = r0.toLowerCase()     // Catch:{ Throwable -> 0x0155 }
            java.lang.String r1 = ".7z"
            boolean r0 = r0.endsWith(r1)     // Catch:{ Throwable -> 0x0155 }
            if (r0 != 0) goto L_0x0095
            com.uc.webview.export.utility.download.DownloadTask r0 = r13.c     // Catch:{ Throwable -> 0x0155 }
            java.lang.String r2 = r0.getFilePath()     // Catch:{ Throwable -> 0x0155 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r13.i     // Catch:{ Throwable -> 0x0155 }
            java.lang.Object[] r0 = r0.e     // Catch:{ Throwable -> 0x0155 }
            r1 = 0
            r0 = r0[r1]     // Catch:{ Throwable -> 0x0155 }
            android.content.Context r0 = (android.content.Context) r0     // Catch:{ Throwable -> 0x0155 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x0155 }
            java.lang.Object[] r1 = r1.e     // Catch:{ Throwable -> 0x0155 }
            r3 = 0
            r1 = r1[r3]     // Catch:{ Throwable -> 0x0155 }
            android.content.Context r1 = (android.content.Context) r1     // Catch:{ Throwable -> 0x0155 }
            java.lang.String r3 = "com.UCMobile"
            com.uc.webview.export.utility.download.UpdateTask r4 = r13.i     // Catch:{ Throwable -> 0x0155 }
            android.webkit.ValueCallback r4 = r4.h     // Catch:{ Throwable -> 0x0155 }
            boolean r0 = com.uc.webview.export.internal.utility.b.a(r2, r0, r1, r3, r4)     // Catch:{ Throwable -> 0x0155 }
            if (r0 != 0) goto L_0x008a
            java.lang.String r0 = "sdk_dec7z_ls"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x0155 }
            com.uc.webview.export.utility.download.DownloadTask r0 = r13.c     // Catch:{ Throwable -> 0x0155 }
            r0.delete()     // Catch:{ Throwable -> 0x0155 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r13.i     // Catch:{ Throwable -> 0x0155 }
            java.lang.Object[] r0 = r0.e     // Catch:{ Throwable -> 0x0155 }
            r1 = 1
            java.lang.RuntimeException r2 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x0155 }
            java.lang.String r3 = "Archive verify failed."
            r2.<init>(r3)     // Catch:{ Throwable -> 0x0155 }
            r0[r1] = r2     // Catch:{ Throwable -> 0x0155 }
            android.webkit.ValueCallback r0 = r13.d     // Catch:{ Throwable -> 0x0197 }
            if (r0 == 0) goto L_0x0089
            android.webkit.ValueCallback r0 = r13.d     // Catch:{ Throwable -> 0x0197 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x0197 }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0197 }
        L_0x0089:
            return
        L_0x008a:
            android.webkit.ValueCallback r0 = r13.a     // Catch:{ Throwable -> 0x0194 }
            if (r0 == 0) goto L_0x0095
            android.webkit.ValueCallback r0 = r13.a     // Catch:{ Throwable -> 0x0194 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x0194 }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0194 }
        L_0x0095:
            com.uc.webview.export.utility.download.UpdateTask r0 = r13.i     // Catch:{ Throwable -> 0x0155 }
            java.io.File r9 = r0.getUpdateDir()     // Catch:{ Throwable -> 0x0155 }
            boolean r0 = r9.exists()     // Catch:{ Throwable -> 0x0155 }
            if (r0 != 0) goto L_0x00a4
            r9.mkdirs()     // Catch:{ Throwable -> 0x0155 }
        L_0x00a4:
            new java.io.File(r9, com.uc.webview.export.utility.download.UpdateTask.STOP_FLG_FILE_NAME).delete()     // Catch:{ Throwable -> 0x0155 }
            r0 = 0
            com.uc.webview.export.utility.download.UpdateTask.a(r9, r0)     // Catch:{ Throwable -> 0x0155 }
            android.webkit.ValueCallback r0 = r13.e     // Catch:{ Throwable -> 0x0191, Exception -> 0x0123 }
            if (r0 == 0) goto L_0x00b6
            android.webkit.ValueCallback r0 = r13.e     // Catch:{ Throwable -> 0x0191, Exception -> 0x0123 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x0191, Exception -> 0x0123 }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0191, Exception -> 0x0123 }
        L_0x00b6:
            com.uc.webview.export.utility.download.UpdateTask r0 = r13.i     // Catch:{ Exception -> 0x0123 }
            java.lang.Object[] r0 = r0.e     // Catch:{ Exception -> 0x0123 }
            r1 = 0
            r1 = r0[r1]     // Catch:{ Exception -> 0x0123 }
            android.content.Context r1 = (android.content.Context) r1     // Catch:{ Exception -> 0x0123 }
            java.lang.String r0 = r13.b     // Catch:{ Exception -> 0x0123 }
            java.lang.String r2 = ".7z"
            boolean r2 = r0.endsWith(r2)     // Catch:{ Exception -> 0x0123 }
            java.lang.String r3 = r13.b     // Catch:{ Exception -> 0x0123 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r13.i     // Catch:{ Exception -> 0x0123 }
            long[] r0 = r0.c     // Catch:{ Exception -> 0x0123 }
            r4 = 1
            r4 = r0[r4]     // Catch:{ Exception -> 0x0123 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r13.i     // Catch:{ Exception -> 0x0123 }
            long[] r0 = r0.c     // Catch:{ Exception -> 0x0123 }
            r6 = 2
            r6 = r0[r6]     // Catch:{ Exception -> 0x0123 }
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x0123 }
            com.uc.webview.export.utility.download.DownloadTask r0 = r13.c     // Catch:{ Exception -> 0x0123 }
            java.lang.String r0 = r0.getFilePath()     // Catch:{ Exception -> 0x0123 }
            r8.<init>(r0)     // Catch:{ Exception -> 0x0123 }
            r10 = 0
            r11 = 0
            com.uc.webview.export.cyclone.UCCyclone.decompressIfNeeded(r1, r2, r3, r4, r6, r8, r9, r10, r11)     // Catch:{ Exception -> 0x0123 }
            android.webkit.ValueCallback r0 = r13.f     // Catch:{ Throwable -> 0x018e, Exception -> 0x0123 }
            if (r0 == 0) goto L_0x00f9
            android.webkit.ValueCallback r0 = r13.f     // Catch:{ Throwable -> 0x018e, Exception -> 0x0123 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x018e, Exception -> 0x0123 }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x018e, Exception -> 0x0123 }
        L_0x00f9:
            r0 = 1
            com.uc.webview.export.utility.download.UpdateTask.a(r9, r0)     // Catch:{ Throwable -> 0x0155 }
            com.uc.webview.export.utility.download.DownloadTask r0 = r13.c     // Catch:{ Throwable -> 0x0155 }
            r0.delete()     // Catch:{ Throwable -> 0x0155 }
            android.webkit.ValueCallback r0 = r13.a     // Catch:{ Throwable -> 0x018a }
            if (r0 == 0) goto L_0x010d
            android.webkit.ValueCallback r0 = r13.a     // Catch:{ Throwable -> 0x018a }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x018a }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x018a }
        L_0x010d:
            java.lang.String r0 = "sdk_ucm_s"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x0155 }
            android.webkit.ValueCallback r0 = r13.g     // Catch:{ Throwable -> 0x0120 }
            if (r0 == 0) goto L_0x0089
            android.webkit.ValueCallback r0 = r13.g     // Catch:{ Throwable -> 0x0120 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x0120 }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0120 }
            goto L_0x0089
        L_0x0120:
            r0 = move-exception
            goto L_0x0089
        L_0x0123:
            r0 = move-exception
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x018c }
            android.webkit.ValueCallback r1 = r1.h     // Catch:{ Throwable -> 0x018c }
            if (r1 == 0) goto L_0x0154
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x018c }
            android.webkit.ValueCallback r1 = r1.h     // Catch:{ Throwable -> 0x018c }
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x018c }
            r3 = 0
            r4 = 9
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x018c }
            r2[r3] = r4     // Catch:{ Throwable -> 0x018c }
            r3 = 1
            java.lang.Class r4 = r0.getClass()     // Catch:{ Throwable -> 0x018c }
            java.lang.String r4 = r4.getName()     // Catch:{ Throwable -> 0x018c }
            int r4 = r4.hashCode()     // Catch:{ Throwable -> 0x018c }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x018c }
            r2[r3] = r4     // Catch:{ Throwable -> 0x018c }
            r1.onReceiveValue(r2)     // Catch:{ Throwable -> 0x018c }
        L_0x0154:
            throw r0     // Catch:{ Throwable -> 0x0155 }
        L_0x0155:
            r0 = move-exception
            java.lang.String r1 = "sdk_dec7z"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)
            java.lang.Class r1 = r0.getClass()
            java.lang.String r1 = r1.getName()
            int r1 = r1.hashCode()
            java.lang.String r2 = "sdk_ucm_le"
            java.lang.String r1 = java.lang.String.valueOf(r1)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r1)
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i
            java.lang.Object[] r1 = r1.e
            r1[r12] = r0
            android.webkit.ValueCallback r0 = r13.h     // Catch:{ Throwable -> 0x0187 }
            if (r0 == 0) goto L_0x0089
            android.webkit.ValueCallback r0 = r13.h     // Catch:{ Throwable -> 0x0187 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r13.i     // Catch:{ Throwable -> 0x0187 }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0187 }
            goto L_0x0089
        L_0x0187:
            r0 = move-exception
            goto L_0x0089
        L_0x018a:
            r0 = move-exception
            goto L_0x010d
        L_0x018c:
            r1 = move-exception
            goto L_0x0154
        L_0x018e:
            r0 = move-exception
            goto L_0x00f9
        L_0x0191:
            r0 = move-exception
            goto L_0x00b6
        L_0x0194:
            r0 = move-exception
            goto L_0x0095
        L_0x0197:
            r0 = move-exception
            goto L_0x0089
        L_0x019a:
            r0 = move-exception
            goto L_0x0028
        L_0x019d:
            r0 = move-exception
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.utility.download.d.run():void");
    }
}
