package com.edge.pcdn;

import android.os.Handler;

public class HttpTask implements Runnable {
    private static final int BUFFER_SIZE = 1024;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 30000;
    private Handler handler;
    private String httpMethod;
    private String param;
    private String urlStr;

    public HttpTask(String url, Handler handler2, String param2, String httpMethod2) {
        this.handler = handler2;
        this.urlStr = url;
        this.param = param2;
        this.httpMethod = httpMethod2;
    }

    /* JADX WARNING: type inference failed for: r13v15, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:107:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00e2 A[Catch:{ all -> 0x0184 }] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00f8 A[SYNTHETIC, Splitter:B:31:0x00f8] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00fd A[SYNTHETIC, Splitter:B:34:0x00fd] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0102 A[SYNTHETIC, Splitter:B:37:0x0102] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0107 A[SYNTHETIC, Splitter:B:40:0x0107] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0187 A[SYNTHETIC, Splitter:B:79:0x0187] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x018c A[SYNTHETIC, Splitter:B:82:0x018c] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0191 A[SYNTHETIC, Splitter:B:85:0x0191] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0196 A[SYNTHETIC, Splitter:B:88:0x0196] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r15 = this;
            android.os.Message r8 = new android.os.Message
            r8.<init>()
            java.lang.String r13 = "GET"
            java.lang.String r14 = r15.httpMethod
            boolean r13 = r13.equals(r14)
            if (r13 == 0) goto L_0x0027
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = r15.urlStr
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r14 = r15.param
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            r15.urlStr = r13
        L_0x0027:
            java.lang.String r13 = r15.urlStr
            com.edge.pcdn.PcdnLog.d(r13)
            r6 = 0
            r9 = 0
            r7 = 0
            r1 = 0
            java.net.URL r12 = new java.net.URL     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = r15.urlStr     // Catch:{ Exception -> 0x01c4 }
            r12.<init>(r13)     // Catch:{ Exception -> 0x01c4 }
            java.net.URLConnection r13 = r12.openConnection()     // Catch:{ Exception -> 0x01c4 }
            r0 = r13
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x01c4 }
            r6 = r0
            r13 = 1
            r6.setDoInput(r13)     // Catch:{ Exception -> 0x01c4 }
            r13 = 1
            r6.setDoOutput(r13)     // Catch:{ Exception -> 0x01c4 }
            r13 = 0
            r6.setUseCaches(r13)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = "Connection"
            java.lang.String r14 = "Keep-Alive"
            r6.setRequestProperty(r13, r14)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = "Charset"
            java.lang.String r14 = "UTF-8"
            r6.setRequestProperty(r13, r14)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = "Content-Type"
            java.lang.String r14 = "application/x-www-form-urlencoded"
            r6.setRequestProperty(r13, r14)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = "User-Agent"
            java.lang.String r14 = "PCDN"
            r6.addRequestProperty(r13, r14)     // Catch:{ Exception -> 0x01c4 }
            r13 = 10000(0x2710, float:1.4013E-41)
            r6.setConnectTimeout(r13)     // Catch:{ Exception -> 0x01c4 }
            r13 = 30000(0x7530, float:4.2039E-41)
            r6.setReadTimeout(r13)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = r15.httpMethod     // Catch:{ Exception -> 0x01c4 }
            r6.setRequestMethod(r13)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = "POST"
            java.lang.String r14 = r15.httpMethod     // Catch:{ Exception -> 0x01c4 }
            boolean r13 = r13.equals(r14)     // Catch:{ Exception -> 0x01c4 }
            if (r13 == 0) goto L_0x00b7
            java.lang.String r13 = "POST"
            r6.setRequestMethod(r13)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = r15.param     // Catch:{ Exception -> 0x01c4 }
            if (r13 == 0) goto L_0x00b7
            java.lang.String r13 = r15.param     // Catch:{ Exception -> 0x01c4 }
            byte[] r4 = r13.getBytes()     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r13 = "Content-Length"
            int r14 = r4.length     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r14 = java.lang.String.valueOf(r14)     // Catch:{ Exception -> 0x01c4 }
            r6.setRequestProperty(r13, r14)     // Catch:{ Exception -> 0x01c4 }
            java.io.DataOutputStream r10 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x01c4 }
            java.io.OutputStream r13 = r6.getOutputStream()     // Catch:{ Exception -> 0x01c4 }
            r10.<init>(r13)     // Catch:{ Exception -> 0x01c4 }
            r10.write(r4)     // Catch:{ Exception -> 0x01c7, all -> 0x01be }
            r10.flush()     // Catch:{ Exception -> 0x01c7, all -> 0x01be }
            r10.close()     // Catch:{ Exception -> 0x01c7, all -> 0x01be }
            r9 = r10
        L_0x00b7:
            int r11 = r6.getResponseCode()     // Catch:{ Exception -> 0x01c4 }
            r8.what = r11     // Catch:{ Exception -> 0x01c4 }
            java.io.InputStream r7 = r6.getInputStream()     // Catch:{ Exception -> 0x01c4 }
            if (r7 == 0) goto L_0x011b
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x01c4 }
            r2.<init>()     // Catch:{ Exception -> 0x01c4 }
            r13 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r13]     // Catch:{ Exception -> 0x00dc, all -> 0x01c1 }
            r3 = -1
        L_0x00cd:
            r13 = 0
            r14 = 1024(0x400, float:1.435E-42)
            int r3 = r7.read(r4, r13, r14)     // Catch:{ Exception -> 0x00dc, all -> 0x01c1 }
            r13 = -1
            if (r3 == r13) goto L_0x010b
            r13 = 0
            r2.write(r4, r13, r3)     // Catch:{ Exception -> 0x00dc, all -> 0x01c1 }
            goto L_0x00cd
        L_0x00dc:
            r5 = move-exception
            r1 = r2
        L_0x00de:
            android.os.Handler r13 = r15.handler     // Catch:{ all -> 0x0184 }
            if (r13 == 0) goto L_0x00ef
            r13 = -2
            r8.what = r13     // Catch:{ all -> 0x0184 }
            java.lang.String r13 = "网络请求过程中产生异常"
            r8.obj = r13     // Catch:{ all -> 0x0184 }
            android.os.Handler r13 = r15.handler     // Catch:{ all -> 0x0184 }
            r13.sendMessage(r8)     // Catch:{ all -> 0x0184 }
        L_0x00ef:
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)     // Catch:{ all -> 0x0184 }
            com.edge.pcdn.PcdnLog.d(r13)     // Catch:{ all -> 0x0184 }
            if (r9 == 0) goto L_0x00fb
            r9.close()     // Catch:{ Exception -> 0x0160 }
        L_0x00fb:
            if (r7 == 0) goto L_0x0100
            r7.close()     // Catch:{ IOException -> 0x0169 }
        L_0x0100:
            if (r1 == 0) goto L_0x0105
            r1.close()     // Catch:{ IOException -> 0x0172 }
        L_0x0105:
            if (r6 == 0) goto L_0x010a
            r6.disconnect()     // Catch:{ Exception -> 0x017b }
        L_0x010a:
            return
        L_0x010b:
            r7.close()     // Catch:{ Exception -> 0x00dc, all -> 0x01c1 }
            r2.close()     // Catch:{ Exception -> 0x00dc, all -> 0x01c1 }
            java.lang.String r13 = "UTF-8"
            java.lang.String r13 = r2.toString(r13)     // Catch:{ Exception -> 0x00dc, all -> 0x01c1 }
            r8.obj = r13     // Catch:{ Exception -> 0x00dc, all -> 0x01c1 }
            r1 = r2
        L_0x011b:
            r6.disconnect()     // Catch:{ Exception -> 0x01c4 }
            android.os.Handler r13 = r15.handler     // Catch:{ Exception -> 0x01c4 }
            if (r13 == 0) goto L_0x0127
            android.os.Handler r13 = r15.handler     // Catch:{ Exception -> 0x01c4 }
            r13.sendMessage(r8)     // Catch:{ Exception -> 0x01c4 }
        L_0x0127:
            if (r9 == 0) goto L_0x012c
            r9.close()     // Catch:{ Exception -> 0x0145 }
        L_0x012c:
            if (r7 == 0) goto L_0x0131
            r7.close()     // Catch:{ IOException -> 0x014e }
        L_0x0131:
            if (r1 == 0) goto L_0x0136
            r1.close()     // Catch:{ IOException -> 0x0157 }
        L_0x0136:
            if (r6 == 0) goto L_0x010a
            r6.disconnect()     // Catch:{ Exception -> 0x013c }
            goto L_0x010a
        L_0x013c:
            r5 = move-exception
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r13)
            goto L_0x010a
        L_0x0145:
            r5 = move-exception
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r13)
            goto L_0x012c
        L_0x014e:
            r5 = move-exception
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r13)
            goto L_0x0131
        L_0x0157:
            r5 = move-exception
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r13)
            goto L_0x0136
        L_0x0160:
            r5 = move-exception
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r13)
            goto L_0x00fb
        L_0x0169:
            r5 = move-exception
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r13)
            goto L_0x0100
        L_0x0172:
            r5 = move-exception
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r13)
            goto L_0x0105
        L_0x017b:
            r5 = move-exception
            java.lang.String r13 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r13)
            goto L_0x010a
        L_0x0184:
            r13 = move-exception
        L_0x0185:
            if (r9 == 0) goto L_0x018a
            r9.close()     // Catch:{ Exception -> 0x019a }
        L_0x018a:
            if (r7 == 0) goto L_0x018f
            r7.close()     // Catch:{ IOException -> 0x01a3 }
        L_0x018f:
            if (r1 == 0) goto L_0x0194
            r1.close()     // Catch:{ IOException -> 0x01ac }
        L_0x0194:
            if (r6 == 0) goto L_0x0199
            r6.disconnect()     // Catch:{ Exception -> 0x01b5 }
        L_0x0199:
            throw r13
        L_0x019a:
            r5 = move-exception
            java.lang.String r14 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r14)
            goto L_0x018a
        L_0x01a3:
            r5 = move-exception
            java.lang.String r14 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r14)
            goto L_0x018f
        L_0x01ac:
            r5 = move-exception
            java.lang.String r14 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r14)
            goto L_0x0194
        L_0x01b5:
            r5 = move-exception
            java.lang.String r14 = com.edge.pcdn.PcdnLog.toString(r5)
            com.edge.pcdn.PcdnLog.e(r14)
            goto L_0x0199
        L_0x01be:
            r13 = move-exception
            r9 = r10
            goto L_0x0185
        L_0x01c1:
            r13 = move-exception
            r1 = r2
            goto L_0x0185
        L_0x01c4:
            r5 = move-exception
            goto L_0x00de
        L_0x01c7:
            r5 = move-exception
            r9 = r10
            goto L_0x00de
        */
        throw new UnsupportedOperationException("Method not decompiled: com.edge.pcdn.HttpTask.run():void");
    }
}
