package com.uc.webview.export.utility.download;

import android.webkit.ValueCallback;

/* compiled from: ProGuard */
final class g implements ValueCallback<DownloadTask> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ ValueCallback b;
    final /* synthetic */ ValueCallback c;
    final /* synthetic */ ValueCallback d;
    final /* synthetic */ UpdateTask e;

    g(UpdateTask updateTask, ValueCallback valueCallback, ValueCallback valueCallback2, ValueCallback valueCallback3, ValueCallback valueCallback4) {
        this.e = updateTask;
        this.a = valueCallback;
        this.b = valueCallback2;
        this.c = valueCallback3;
        this.d = valueCallback4;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:9:0x0044=Splitter:B:9:0x0044, B:21:0x0066=Splitter:B:21:0x0066} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final /* synthetic */ void onReceiveValue(java.lang.Object r13) {
        /*
            r12 = this;
            r10 = 10
            r8 = 1
            r2 = 0
            com.uc.webview.export.utility.download.DownloadTask r13 = (com.uc.webview.export.utility.download.DownloadTask) r13
            com.uc.webview.export.utility.download.UpdateTask r0 = r12.e     // Catch:{ Throwable -> 0x0096 }
            long[] r0 = r0.c     // Catch:{ Throwable -> 0x0096 }
            r1 = 1
            long r4 = r13.getTotalSize()     // Catch:{ Throwable -> 0x0096 }
            r0[r1] = r4     // Catch:{ Throwable -> 0x0096 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r12.e     // Catch:{ Throwable -> 0x0096 }
            long[] r0 = r0.c     // Catch:{ Throwable -> 0x0096 }
            r1 = 2
            long r4 = r13.getLastModified()     // Catch:{ Throwable -> 0x0096 }
            r0[r1] = r4     // Catch:{ Throwable -> 0x0096 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r12.e     // Catch:{ Throwable -> 0x0096 }
            java.io.File r4 = r0.getUpdateDir()     // Catch:{ Throwable -> 0x0096 }
            com.uc.webview.export.utility.download.UpdateTask r0 = r12.e     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r0 = r0.g     // Catch:{ Throwable -> 0x0096 }
            boolean r0 = com.uc.webview.export.utility.download.UpdateTask.isFinished(r4, r0)     // Catch:{ Throwable -> 0x0096 }
            if (r0 == 0) goto L_0x0048
            java.lang.String r0 = "sdk_ucm_e"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x0096 }
            android.webkit.ValueCallback r0 = r12.a     // Catch:{ Throwable -> 0x00d2 }
            if (r0 == 0) goto L_0x0044
            android.webkit.ValueCallback r0 = r12.a     // Catch:{ Throwable -> 0x00d2 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r12.e     // Catch:{ Throwable -> 0x00d2 }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x00d2 }
        L_0x0044:
            r13.stop()     // Catch:{ Throwable -> 0x0096 }
        L_0x0047:
            return
        L_0x0048:
            long r0 = r13.getTotalSize()     // Catch:{ Exception -> 0x00d0 }
            int r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r5 != 0) goto L_0x008b
            r0 = r2
        L_0x0051:
            r6 = 100
            int r5 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r5 > 0) goto L_0x005b
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x0094
        L_0x005b:
            r0 = -1
        L_0x005c:
            java.lang.String r1 = "sdk_ucm_p"
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x00d0 }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r1, r0)     // Catch:{ Exception -> 0x00d0 }
        L_0x0066:
            boolean r0 = r4.exists()     // Catch:{ Throwable -> 0x0096 }
            if (r0 == 0) goto L_0x007d
            java.lang.String r0 = "sdk_ucm_f"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x0096 }
            android.webkit.ValueCallback r0 = r12.b     // Catch:{ Throwable -> 0x00ce }
            if (r0 == 0) goto L_0x007d
            android.webkit.ValueCallback r0 = r12.b     // Catch:{ Throwable -> 0x00ce }
            com.uc.webview.export.utility.download.UpdateTask r1 = r12.e     // Catch:{ Throwable -> 0x00ce }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x00ce }
        L_0x007d:
            android.webkit.ValueCallback r0 = r12.c     // Catch:{ Throwable -> 0x0089 }
            if (r0 == 0) goto L_0x0047
            android.webkit.ValueCallback r0 = r12.c     // Catch:{ Throwable -> 0x0089 }
            com.uc.webview.export.utility.download.UpdateTask r1 = r12.e     // Catch:{ Throwable -> 0x0089 }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0089 }
            goto L_0x0047
        L_0x0089:
            r0 = move-exception
            goto L_0x0047
        L_0x008b:
            long r6 = r13.getCurrentSize()     // Catch:{ Exception -> 0x00d0 }
            long r6 = r6 * r10
            long r0 = r6 / r0
            long r0 = r0 * r10
            goto L_0x0051
        L_0x0094:
            int r0 = (int) r0
            goto L_0x005c
        L_0x0096:
            r0 = move-exception
            r13.stop()
            java.lang.String r1 = "sdk_ucm_en"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)
            java.lang.Class r1 = r0.getClass()
            java.lang.String r1 = r1.getName()
            int r1 = r1.hashCode()
            java.lang.String r2 = "sdk_ucm_le"
            java.lang.String r1 = java.lang.String.valueOf(r1)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r1)
            com.uc.webview.export.utility.download.UpdateTask r1 = r12.e
            java.lang.Object[] r1 = r1.e
            r1[r8] = r0
            android.webkit.ValueCallback r0 = r12.d     // Catch:{ Throwable -> 0x00cb }
            if (r0 == 0) goto L_0x0047
            android.webkit.ValueCallback r0 = r12.d     // Catch:{ Throwable -> 0x00cb }
            com.uc.webview.export.utility.download.UpdateTask r1 = r12.e     // Catch:{ Throwable -> 0x00cb }
            r0.onReceiveValue(r1)     // Catch:{ Throwable -> 0x00cb }
            goto L_0x0047
        L_0x00cb:
            r0 = move-exception
            goto L_0x0047
        L_0x00ce:
            r0 = move-exception
            goto L_0x007d
        L_0x00d0:
            r0 = move-exception
            goto L_0x0066
        L_0x00d2:
            r0 = move-exception
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.utility.download.g.onReceiveValue(java.lang.Object):void");
    }
}
