package com.uc.webview.export.internal.c.a;

/* compiled from: ProGuard */
final class d extends Thread {
    final /* synthetic */ a a;

    d(a aVar) {
        this.a = aVar;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:104:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r18 = this;
            r0 = r18
            com.uc.webview.export.internal.c.a.a r2 = r0.a     // Catch:{ Throwable -> 0x0105 }
            r2.a()     // Catch:{ Throwable -> 0x0105 }
            r0 = r18
            com.uc.webview.export.internal.c.a.a r7 = r0.a     // Catch:{ Throwable -> 0x0105 }
            monitor-enter(r7)     // Catch:{ Throwable -> 0x0105 }
            r0 = r18
            com.uc.webview.export.internal.c.a.a r2 = r0.a     // Catch:{ all -> 0x0102 }
            android.content.Context r2 = r2.b     // Catch:{ all -> 0x0102 }
            java.lang.String r3 = "UC_WA_STAT"
            r4 = 0
            android.content.SharedPreferences r3 = r2.getSharedPreferences(r3, r4)     // Catch:{ all -> 0x0102 }
            java.lang.String r2 = "1"
            r4 = 0
            long r8 = r3.getLong(r2, r4)     // Catch:{ all -> 0x0102 }
            boolean r2 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ all -> 0x0102 }
            if (r2 == 0) goto L_0x004e
            java.lang.String r2 = "SDKWaStat"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0102 }
            java.lang.String r5 = "==handlUpload==last upload time:"
            r4.<init>(r5)     // Catch:{ all -> 0x0102 }
            java.text.SimpleDateFormat r5 = new java.text.SimpleDateFormat     // Catch:{ all -> 0x0102 }
            java.lang.String r6 = "yyyy-MM-dd HH:mm:ss"
            r5.<init>(r6)     // Catch:{ all -> 0x0102 }
            java.util.Date r6 = new java.util.Date     // Catch:{ all -> 0x0102 }
            r6.<init>(r8)     // Catch:{ all -> 0x0102 }
            java.lang.String r5 = r5.format(r6)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0102 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0102 }
            com.uc.webview.export.internal.utility.Log.d(r2, r4)     // Catch:{ all -> 0x0102 }
        L_0x004e:
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0102 }
            r4 = 0
            int r2 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r2 == 0) goto L_0x0098
            long r4 = r10 - r8
            r12 = 43200000(0x2932e00, double:2.1343636E-316)
            int r2 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r2 >= 0) goto L_0x0098
            java.util.Calendar r2 = java.util.Calendar.getInstance()     // Catch:{ all -> 0x0102 }
            r2.setTimeInMillis(r8)     // Catch:{ all -> 0x0102 }
            r4 = 11
            int r4 = r2.get(r4)     // Catch:{ all -> 0x0102 }
            r2.setTimeInMillis(r10)     // Catch:{ all -> 0x0102 }
            r5 = 11
            int r2 = r2.get(r5)     // Catch:{ all -> 0x0102 }
            if (r4 < 0) goto L_0x0081
            r5 = 12
            if (r4 >= r5) goto L_0x0081
            r5 = 12
            if (r2 >= r5) goto L_0x008b
        L_0x0081:
            r5 = 12
            if (r4 < r5) goto L_0x00f3
            if (r2 < 0) goto L_0x00f3
            r4 = 12
            if (r2 >= r4) goto L_0x00f3
        L_0x008b:
            boolean r2 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ all -> 0x0102 }
            if (r2 == 0) goto L_0x0098
            java.lang.String r2 = "SDKWaStat"
            java.lang.String r4 = "跨0点或12点"
            com.uc.webview.export.internal.utility.Log.d(r2, r4)     // Catch:{ all -> 0x0102 }
        L_0x0098:
            r2 = 0
            r4 = 1
            java.lang.String[] r12 = new java.lang.String[r4]     // Catch:{ all -> 0x0102 }
            r4 = 0
            r5 = 0
            r12[r4] = r5     // Catch:{ all -> 0x0102 }
            r4 = 0
            int r4 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r4 == 0) goto L_0x01d4
            r0 = r18
            com.uc.webview.export.internal.c.a.a r5 = r0.a     // Catch:{ all -> 0x0102 }
            java.lang.String r4 = "2"
            java.lang.String r6 = ""
            java.lang.String r3 = r3.getString(r4, r6)     // Catch:{ all -> 0x0102 }
            if (r3 == 0) goto L_0x00c0
            java.lang.String r4 = r3.trim()     // Catch:{ all -> 0x0102 }
            int r4 = r4.length()     // Catch:{ all -> 0x0102 }
            if (r4 != 0) goto L_0x0110
        L_0x00c0:
            r4 = 1
        L_0x00c1:
            if (r4 == 0) goto L_0x00e2
            java.util.UUID r3 = java.util.UUID.randomUUID()     // Catch:{ all -> 0x0102 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0102 }
            android.content.Context r4 = r5.b     // Catch:{ all -> 0x0102 }
            java.lang.String r5 = "UC_WA_STAT"
            r6 = 0
            android.content.SharedPreferences r4 = r4.getSharedPreferences(r5, r6)     // Catch:{ all -> 0x0102 }
            android.content.SharedPreferences$Editor r4 = r4.edit()     // Catch:{ all -> 0x0102 }
            java.lang.String r5 = "2"
            r4.putString(r5, r3)     // Catch:{ all -> 0x0102 }
            r4.commit()     // Catch:{ all -> 0x0102 }
        L_0x00e2:
            r6 = r3
            android.webkit.ValueCallback<java.lang.String> r3 = com.uc.webview.export.internal.d.t     // Catch:{ all -> 0x0102 }
            if (r3 != 0) goto L_0x027d
            r0 = r18
            com.uc.webview.export.internal.c.a.a r3 = r0.a     // Catch:{ all -> 0x0102 }
            byte[] r5 = com.uc.webview.export.internal.c.a.a.a((com.uc.webview.export.internal.c.a.a) r3, (java.lang.String[]) r12)     // Catch:{ all -> 0x0102 }
            if (r5 != 0) goto L_0x0112
            monitor-exit(r7)     // Catch:{ all -> 0x0102 }
        L_0x00f2:
            return
        L_0x00f3:
            boolean r2 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ all -> 0x0102 }
            if (r2 == 0) goto L_0x0100
            java.lang.String r2 = "SDKWaStat"
            java.lang.String r3 = "时间间隔小于12小时,不上传"
            com.uc.webview.export.internal.utility.Log.d(r2, r3)     // Catch:{ all -> 0x0102 }
        L_0x0100:
            monitor-exit(r7)     // Catch:{ all -> 0x0102 }
            goto L_0x00f2
        L_0x0102:
            r2 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0102 }
            throw r2     // Catch:{ Throwable -> 0x0105 }
        L_0x0105:
            r2 = move-exception
            java.lang.String r3 = "SDKWaStat"
            java.lang.String r4 = "handlUpload"
            com.uc.webview.export.internal.utility.Log.i(r3, r4, r2)
            goto L_0x00f2
        L_0x0110:
            r4 = 0
            goto L_0x00c1
        L_0x0112:
            r4 = 0
            byte[] r5 = com.uc.webview.export.internal.c.a.e.a((byte[]) r5)     // Catch:{ Exception -> 0x0271 }
            r4 = 1
            boolean r3 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ Exception -> 0x0271 }
            if (r3 == 0) goto L_0x0137
            java.lang.String r3 = "SDKWaStat"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0271 }
            java.lang.String r14 = "加密后的数据:"
            r13.<init>(r14)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r14 = new java.lang.String     // Catch:{ Exception -> 0x0271 }
            r14.<init>(r5)     // Catch:{ Exception -> 0x0271 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x0271 }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x0271 }
            com.uc.webview.export.internal.utility.Log.d(r3, r13)     // Catch:{ Exception -> 0x0271 }
        L_0x0137:
            java.lang.String r3 = "27120f2b4115"
            long r14 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0102 }
            java.lang.String r13 = java.lang.String.valueOf(r14)     // Catch:{ all -> 0x0102 }
            java.lang.String r14 = "AppChk#2014"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x0102 }
            r15.<init>()     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r15 = r15.append(r3)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r15 = r15.append(r6)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r15 = r15.append(r13)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r14 = r15.append(r14)     // Catch:{ all -> 0x0102 }
            java.lang.String r14 = r14.toString()     // Catch:{ all -> 0x0102 }
            java.lang.String r14 = com.uc.webview.export.internal.c.a.e.a((java.lang.String) r14)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x0102 }
            java.lang.String r16 = "https://applog.uc.cn/collect?uc_param_str=&chk="
            r15.<init>(r16)     // Catch:{ all -> 0x0102 }
            int r16 = r14.length()     // Catch:{ all -> 0x0102 }
            int r16 = r16 + -8
            int r17 = r14.length()     // Catch:{ all -> 0x0102 }
            r0 = r16
            r1 = r17
            java.lang.String r14 = r14.substring(r0, r1)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r14 = r15.append(r14)     // Catch:{ all -> 0x0102 }
            java.lang.String r16 = "&vno="
            r0 = r16
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r13 = r14.append(r13)     // Catch:{ all -> 0x0102 }
            java.lang.String r14 = "&uuid="
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r6 = r13.append(r6)     // Catch:{ all -> 0x0102 }
            java.lang.String r13 = "&app="
            java.lang.StringBuilder r6 = r6.append(r13)     // Catch:{ all -> 0x0102 }
            r6.append(r3)     // Catch:{ all -> 0x0102 }
            if (r4 == 0) goto L_0x01aa
            java.lang.String r3 = "&enc=aes"
            r15.append(r3)     // Catch:{ all -> 0x0102 }
        L_0x01aa:
            java.lang.String r6 = r15.toString()     // Catch:{ all -> 0x0102 }
            boolean r3 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ all -> 0x0102 }
            if (r3 == 0) goto L_0x01c8
            java.lang.String r3 = "SDKWaStat"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0102 }
            java.lang.String r13 = "request url:"
            r4.<init>(r13)     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ all -> 0x0102 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0102 }
            com.uc.webview.export.internal.utility.Log.d(r3, r4)     // Catch:{ all -> 0x0102 }
        L_0x01c8:
            r3 = 3
        L_0x01c9:
            int r4 = r3 + -1
            if (r3 <= 0) goto L_0x01d4
            boolean r3 = com.uc.webview.export.internal.c.a.a.a((java.lang.String) r6, (byte[]) r5)     // Catch:{ all -> 0x0102 }
            if (r3 == 0) goto L_0x02a1
            r2 = 1
        L_0x01d4:
            if (r2 == 0) goto L_0x0239
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x0102 }
            r0 = r18
            com.uc.webview.export.internal.c.a.a r3 = r0.a     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0102 }
            r4.<init>()     // Catch:{ all -> 0x0102 }
            android.content.Context r3 = r3.b     // Catch:{ all -> 0x0102 }
            android.content.Context r3 = r3.getApplicationContext()     // Catch:{ all -> 0x0102 }
            android.content.pm.ApplicationInfo r3 = r3.getApplicationInfo()     // Catch:{ all -> 0x0102 }
            java.lang.String r3 = r3.dataDir     // Catch:{ all -> 0x0102 }
            java.lang.StringBuilder r3 = r4.append(r3)     // Catch:{ all -> 0x0102 }
            java.lang.String r4 = "/ucwa"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0102 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0102 }
            java.io.File r4 = new java.io.File     // Catch:{ all -> 0x0102 }
            r4.<init>(r3)     // Catch:{ all -> 0x0102 }
            boolean r5 = r4.exists()     // Catch:{ all -> 0x0102 }
            if (r5 != 0) goto L_0x020a
            r4.mkdir()     // Catch:{ all -> 0x0102 }
        L_0x020a:
            java.lang.String r4 = "wa_upload_new.wa"
            r2.<init>(r3, r4)     // Catch:{ all -> 0x0102 }
            r2.delete()     // Catch:{ all -> 0x0102 }
            r0 = r18
            com.uc.webview.export.internal.c.a.a r2 = r0.a     // Catch:{ all -> 0x0102 }
            r3 = 0
            r3 = r12[r3]     // Catch:{ all -> 0x0102 }
            android.content.Context r2 = r2.b     // Catch:{ all -> 0x0102 }
            java.lang.String r4 = "UC_WA_STAT"
            r5 = 0
            android.content.SharedPreferences r2 = r2.getSharedPreferences(r4, r5)     // Catch:{ all -> 0x0102 }
            android.content.SharedPreferences$Editor r2 = r2.edit()     // Catch:{ all -> 0x0102 }
            java.lang.String r4 = "1"
            r2.putLong(r4, r10)     // Catch:{ all -> 0x0102 }
            if (r3 == 0) goto L_0x0236
            java.lang.String r4 = "4"
            r2.putString(r4, r3)     // Catch:{ all -> 0x0102 }
        L_0x0236:
            r2.commit()     // Catch:{ all -> 0x0102 }
        L_0x0239:
            r2 = 0
            int r2 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r2 != 0) goto L_0x026e
            java.lang.String r2 = "SDKWaStat"
            java.lang.String r3 = "首次不上传数据"
            com.uc.webview.export.internal.utility.Log.d(r2, r3)     // Catch:{ all -> 0x0102 }
            r0 = r18
            com.uc.webview.export.internal.c.a.a r2 = r0.a     // Catch:{ all -> 0x0102 }
            r3 = 0
            r3 = r12[r3]     // Catch:{ all -> 0x0102 }
            android.content.Context r2 = r2.b     // Catch:{ all -> 0x0102 }
            java.lang.String r4 = "UC_WA_STAT"
            r5 = 0
            android.content.SharedPreferences r2 = r2.getSharedPreferences(r4, r5)     // Catch:{ all -> 0x0102 }
            android.content.SharedPreferences$Editor r2 = r2.edit()     // Catch:{ all -> 0x0102 }
            java.lang.String r4 = "1"
            r2.putLong(r4, r10)     // Catch:{ all -> 0x0102 }
            if (r3 == 0) goto L_0x026b
            java.lang.String r4 = "4"
            r2.putString(r4, r3)     // Catch:{ all -> 0x0102 }
        L_0x026b:
            r2.commit()     // Catch:{ all -> 0x0102 }
        L_0x026e:
            monitor-exit(r7)     // Catch:{ all -> 0x0102 }
            goto L_0x00f2
        L_0x0271:
            r3 = move-exception
            java.lang.String r13 = "SDKWaStat"
            java.lang.String r14 = "data encrypt"
            com.uc.webview.export.internal.utility.Log.e(r13, r14, r3)     // Catch:{ all -> 0x0102 }
            goto L_0x0137
        L_0x027d:
            r0 = r18
            com.uc.webview.export.internal.c.a.a r3 = r0.a     // Catch:{ Exception -> 0x0295 }
            java.lang.String r3 = r3.a((java.lang.String) r6, (java.lang.String[]) r12)     // Catch:{ Exception -> 0x0295 }
            if (r3 == 0) goto L_0x01d4
            java.lang.String r4 = "SDKWaStat"
            com.uc.webview.export.internal.utility.Log.i(r4, r3)     // Catch:{ Exception -> 0x0295 }
            android.webkit.ValueCallback<java.lang.String> r4 = com.uc.webview.export.internal.d.t     // Catch:{ Exception -> 0x0295 }
            r4.onReceiveValue(r3)     // Catch:{ Exception -> 0x0295 }
            r2 = 1
            goto L_0x01d4
        L_0x0295:
            r3 = move-exception
            java.lang.String r4 = "SDKWaStat"
            java.lang.String r5 = "第三方上传数据出错!"
            com.uc.webview.export.internal.utility.Log.d(r4, r5, r3)     // Catch:{ all -> 0x0102 }
            goto L_0x01d4
        L_0x02a1:
            r3 = r4
            goto L_0x01c9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.c.a.d.run():void");
    }
}
