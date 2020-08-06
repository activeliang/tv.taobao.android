package com.edge.pcdn;

public class PcdnReportLogTask implements Runnable {
    private byte[] data = null;
    private String url = null;

    public PcdnReportLogTask(String url2, byte[] data2) {
        this.url = url2;
        this.data = data2;
    }

    /* JADX WARNING: type inference failed for: r8v11, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00e8 A[Catch:{ Exception -> 0x00f1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ed A[Catch:{ Exception -> 0x00f1 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r11 = this;
            r4 = 0
            r3 = 0
            r2 = 0
            java.net.URL r7 = new java.net.URL     // Catch:{ Exception -> 0x0111 }
            java.lang.String r8 = r11.url     // Catch:{ Exception -> 0x0111 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x0111 }
            java.net.URLConnection r8 = r7.openConnection()     // Catch:{ Exception -> 0x0111 }
            r0 = r8
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0111 }
            r2 = r0
            r8 = 1
            r2.setDoInput(r8)     // Catch:{ Exception -> 0x0111 }
            r8 = 1
            r2.setDoOutput(r8)     // Catch:{ Exception -> 0x0111 }
            r8 = 0
            r2.setUseCaches(r8)     // Catch:{ Exception -> 0x0111 }
            java.lang.String r8 = "POST"
            r2.setRequestMethod(r8)     // Catch:{ Exception -> 0x0111 }
            java.lang.String r8 = "Charset"
            java.lang.String r9 = "UTF-8"
            r2.setRequestProperty(r8, r9)     // Catch:{ Exception -> 0x0111 }
            java.lang.String r8 = "Content-Type"
            java.lang.String r9 = "application/octet-stream"
            r2.setRequestProperty(r8, r9)     // Catch:{ Exception -> 0x0111 }
            java.io.DataOutputStream r5 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x0111 }
            java.io.OutputStream r8 = r2.getOutputStream()     // Catch:{ Exception -> 0x0111 }
            r5.<init>(r8)     // Catch:{ Exception -> 0x0111 }
            byte[] r8 = r11.data     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            r5.write(r8)     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            r5.flush()     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            int r6 = r2.getResponseCode()     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            r8 = 200(0xc8, float:2.8E-43)
            if (r6 == r8) goto L_0x0075
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            r8.<init>()     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            java.lang.String r9 = "report logdata fail: "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            java.lang.StringBuilder r8 = r8.append(r6)     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            com.edge.pcdn.PcdnLog.d(r8)     // Catch:{ Exception -> 0x007c, all -> 0x010e }
        L_0x0066:
            r2.disconnect()     // Catch:{ Exception -> 0x00c4 }
            if (r5 == 0) goto L_0x006e
            r5.close()     // Catch:{ Exception -> 0x00c4 }
        L_0x006e:
            if (r3 == 0) goto L_0x0073
            r3.close()     // Catch:{ Exception -> 0x00c4 }
        L_0x0073:
            r4 = r5
        L_0x0074:
            return
        L_0x0075:
            java.lang.String r8 = "report logdata Success!"
            com.edge.pcdn.PcdnLog.d(r8)     // Catch:{ Exception -> 0x007c, all -> 0x010e }
            goto L_0x0066
        L_0x007c:
            r1 = move-exception
            r4 = r5
        L_0x007e:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e2 }
            r8.<init>()     // Catch:{ all -> 0x00e2 }
            java.lang.String r9 = "report exception :"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x00e2 }
            java.lang.String r9 = com.edge.pcdn.PcdnLog.toString(r1)     // Catch:{ all -> 0x00e2 }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x00e2 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x00e2 }
            com.edge.pcdn.PcdnLog.e(r8)     // Catch:{ all -> 0x00e2 }
            r2.disconnect()     // Catch:{ Exception -> 0x00a7 }
            if (r4 == 0) goto L_0x00a1
            r4.close()     // Catch:{ Exception -> 0x00a7 }
        L_0x00a1:
            if (r3 == 0) goto L_0x0074
            r3.close()     // Catch:{ Exception -> 0x00a7 }
            goto L_0x0074
        L_0x00a7:
            r1 = move-exception
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "report close stream exception"
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = com.edge.pcdn.PcdnLog.toString(r1)
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.edge.pcdn.PcdnLog.e(r8)
            goto L_0x0074
        L_0x00c4:
            r1 = move-exception
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "report close stream exception"
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = com.edge.pcdn.PcdnLog.toString(r1)
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.edge.pcdn.PcdnLog.e(r8)
            r4 = r5
            goto L_0x0074
        L_0x00e2:
            r8 = move-exception
        L_0x00e3:
            r2.disconnect()     // Catch:{ Exception -> 0x00f1 }
            if (r4 == 0) goto L_0x00eb
            r4.close()     // Catch:{ Exception -> 0x00f1 }
        L_0x00eb:
            if (r3 == 0) goto L_0x00f0
            r3.close()     // Catch:{ Exception -> 0x00f1 }
        L_0x00f0:
            throw r8
        L_0x00f1:
            r1 = move-exception
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "report close stream exception"
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r10 = com.edge.pcdn.PcdnLog.toString(r1)
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            com.edge.pcdn.PcdnLog.e(r9)
            goto L_0x00f0
        L_0x010e:
            r8 = move-exception
            r4 = r5
            goto L_0x00e3
        L_0x0111:
            r1 = move-exception
            goto L_0x007e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.edge.pcdn.PcdnReportLogTask.run():void");
    }
}
