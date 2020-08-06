package com.alibaba.analytics.utils;

public final class HttpUtils {
    public static final int HTTP_REQ_TYPE_GET = 1;
    public static final int HTTP_REQ_TYPE_POST_FORM_DATA = 2;
    public static final int HTTP_REQ_TYPE_POST_URL_ENCODED = 3;
    private static final int MAX_CONNECTION_TIME_OUT = 10000;
    public static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 60000;
    private static final String POST_Field_BOTTOM = "--GJircTeP--\r\n";
    private static final String POST_Field_TOP = "--GJircTeP\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream \r\n\r\n";

    public static class HttpResponse {
        public byte[] data = null;
        public int httpResponseCode = -1;
        public long rt = 0;
    }

    static {
        System.setProperty("http.keepAlive", "true");
    }

    /* JADX WARNING: Removed duplicated region for block: B:111:0x02bb A[Catch:{ IOException -> 0x02c3, all -> 0x033a }, LOOP:1: B:109:0x02a9->B:111:0x02bb, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x02f1 A[SYNTHETIC, Splitter:B:128:0x02f1] */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0300 A[SYNTHETIC, Splitter:B:135:0x0300] */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x0320 A[SYNTHETIC, Splitter:B:147:0x0320] */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x0329  */
    /* JADX WARNING: Removed duplicated region for block: B:178:0x031e A[EDGE_INSN: B:178:0x031e->B:146:0x031e ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.analytics.utils.HttpUtils.HttpResponse sendRequest(int r32, java.lang.String r33, java.util.Map<java.lang.String, java.lang.Object> r34, boolean r35) {
        /*
            com.alibaba.analytics.utils.HttpUtils$HttpResponse r26 = new com.alibaba.analytics.utils.HttpUtils$HttpResponse
            r26.<init>()
            boolean r29 = android.text.TextUtils.isEmpty(r33)
            if (r29 == 0) goto L_0x000c
        L_0x000b:
            return r26
        L_0x000c:
            r27 = 0
            r7 = 0
            java.net.URL r28 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0146, IOException -> 0x014c }
            r0 = r28
            r1 = r33
            r0.<init>(r1)     // Catch:{ MalformedURLException -> 0x0146, IOException -> 0x014c }
            java.net.URLConnection r7 = r28.openConnection()     // Catch:{ MalformedURLException -> 0x0353, IOException -> 0x034e }
            java.net.HttpURLConnection r7 = (java.net.HttpURLConnection) r7     // Catch:{ MalformedURLException -> 0x0353, IOException -> 0x034e }
            if (r7 == 0) goto L_0x000b
            r29 = 2
            r0 = r32
            r1 = r29
            if (r0 == r1) goto L_0x0030
            r29 = 3
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x0037
        L_0x0030:
            r29 = 1
            r0 = r29
            r7.setDoOutput(r0)
        L_0x0037:
            r29 = 1
            r0 = r29
            r7.setDoInput(r0)
            r29 = 2
            r0 = r32
            r1 = r29
            if (r0 == r1) goto L_0x004e
            r29 = 3
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x0152
        L_0x004e:
            java.lang.String r29 = "POST"
            r0 = r29
            r7.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x015c }
        L_0x0056:
            r29 = 0
            r0 = r29
            r7.setUseCaches(r0)
            r29 = 10000(0x2710, float:1.4013E-41)
            r0 = r29
            r7.setConnectTimeout(r0)
            r29 = 60000(0xea60, float:8.4078E-41)
            r0 = r29
            r7.setReadTimeout(r0)
            java.lang.String r29 = "Connection"
            java.lang.String r30 = "close"
            r0 = r29
            r1 = r30
            r7.setRequestProperty(r0, r1)
            if (r35 == 0) goto L_0x0088
            java.lang.String r29 = "Accept-Encoding"
            java.lang.String r30 = "gzip,deflate"
            r0 = r29
            r1 = r30
            r7.setRequestProperty(r0, r1)
        L_0x0088:
            r29 = 1
            r0 = r29
            r7.setInstanceFollowRedirects(r0)
            r9 = 0
            r29 = 2
            r0 = r32
            r1 = r29
            if (r0 == r1) goto L_0x00a0
            r29 = 3
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x022e
        L_0x00a0:
            r29 = 2
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x0162
            java.lang.String r29 = "Content-Type"
            java.lang.String r30 = "multipart/form-data; boundary=GJircTeP"
            r0 = r29
            r1 = r30
            r7.setRequestProperty(r0, r1)
        L_0x00b5:
            r8 = 0
            if (r34 == 0) goto L_0x021d
            int r29 = r34.size()
            if (r29 <= 0) goto L_0x021d
            java.io.ByteArrayOutputStream r18 = new java.io.ByteArrayOutputStream
            r18.<init>()
            java.util.Set r17 = r34.keySet()
            int r29 = r17.size()
            r0 = r29
            java.lang.String[] r0 = new java.lang.String[r0]
            r19 = r0
            r0 = r17
            r1 = r19
            r0.toArray(r1)
            com.alibaba.analytics.utils.KeyArraySorter r29 = com.alibaba.analytics.utils.KeyArraySorter.getInstance()
            r30 = 1
            r0 = r29
            r1 = r19
            r2 = r30
            java.lang.String[] r19 = r0.sortResourcesList(r1, r2)
            r4 = r19
            int r0 = r4.length
            r21 = r0
            r15 = 0
        L_0x00ee:
            r0 = r21
            if (r15 >= r0) goto L_0x0203
            r16 = r4[r15]
            r29 = 2
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x017e
            r0 = r34
            r1 = r16
            java.lang.Object r29 = r0.get(r1)
            byte[] r29 = (byte[]) r29
            r20 = r29
            byte[] r20 = (byte[]) r20
            if (r20 == 0) goto L_0x0143
            java.lang.String r29 = "--GJircTeP\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream \r\n\r\n"
            r30 = 2
            r0 = r30
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ IOException -> 0x0179 }
            r30 = r0
            r31 = 0
            r30[r31] = r16     // Catch:{ IOException -> 0x0179 }
            r31 = 1
            r30[r31] = r16     // Catch:{ IOException -> 0x0179 }
            java.lang.String r29 = java.lang.String.format(r29, r30)     // Catch:{ IOException -> 0x0179 }
            byte[] r29 = r29.getBytes()     // Catch:{ IOException -> 0x0179 }
            r0 = r18
            r1 = r29
            r0.write(r1)     // Catch:{ IOException -> 0x0179 }
            r0 = r18
            r1 = r20
            r0.write(r1)     // Catch:{ IOException -> 0x0179 }
            java.lang.String r29 = "\r\n"
            byte[] r29 = r29.getBytes()     // Catch:{ IOException -> 0x0179 }
            r0 = r18
            r1 = r29
            r0.write(r1)     // Catch:{ IOException -> 0x0179 }
        L_0x0143:
            int r15 = r15 + 1
            goto L_0x00ee
        L_0x0146:
            r13 = move-exception
        L_0x0147:
            r13.printStackTrace()
            goto L_0x000b
        L_0x014c:
            r13 = move-exception
        L_0x014d:
            r13.printStackTrace()
            goto L_0x000b
        L_0x0152:
            java.lang.String r29 = "GET"
            r0 = r29
            r7.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x015c }
            goto L_0x0056
        L_0x015c:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x000b
        L_0x0162:
            r29 = 3
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x00b5
            java.lang.String r29 = "Content-Type"
            java.lang.String r30 = "application/x-www-form-urlencoded"
            r0 = r29
            r1 = r30
            r7.setRequestProperty(r0, r1)
            goto L_0x00b5
        L_0x0179:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0143
        L_0x017e:
            r29 = 3
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x0143
            r0 = r34
            r1 = r16
            java.lang.Object r20 = r0.get(r1)
            java.lang.String r20 = (java.lang.String) r20
            int r29 = r18.size()
            if (r29 <= 0) goto L_0x01d0
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x01ca }
            r29.<init>()     // Catch:{ IOException -> 0x01ca }
            java.lang.String r30 = "&"
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x01ca }
            r0 = r29
            r1 = r16
            java.lang.StringBuilder r29 = r0.append(r1)     // Catch:{ IOException -> 0x01ca }
            java.lang.String r30 = "="
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x01ca }
            r0 = r29
            r1 = r20
            java.lang.StringBuilder r29 = r0.append(r1)     // Catch:{ IOException -> 0x01ca }
            java.lang.String r29 = r29.toString()     // Catch:{ IOException -> 0x01ca }
            byte[] r29 = r29.getBytes()     // Catch:{ IOException -> 0x01ca }
            r0 = r18
            r1 = r29
            r0.write(r1)     // Catch:{ IOException -> 0x01ca }
            goto L_0x0143
        L_0x01ca:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0143
        L_0x01d0:
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x01fd }
            r29.<init>()     // Catch:{ IOException -> 0x01fd }
            r0 = r29
            r1 = r16
            java.lang.StringBuilder r29 = r0.append(r1)     // Catch:{ IOException -> 0x01fd }
            java.lang.String r30 = "="
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x01fd }
            r0 = r29
            r1 = r20
            java.lang.StringBuilder r29 = r0.append(r1)     // Catch:{ IOException -> 0x01fd }
            java.lang.String r29 = r29.toString()     // Catch:{ IOException -> 0x01fd }
            byte[] r29 = r29.getBytes()     // Catch:{ IOException -> 0x01fd }
            r0 = r18
            r1 = r29
            r0.write(r1)     // Catch:{ IOException -> 0x01fd }
            goto L_0x0143
        L_0x01fd:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0143
        L_0x0203:
            r29 = 2
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x0219
            java.lang.String r29 = "--GJircTeP--\r\n"
            byte[] r29 = r29.getBytes()     // Catch:{ IOException -> 0x02d4 }
            r0 = r18
            r1 = r29
            r0.write(r1)     // Catch:{ IOException -> 0x02d4 }
        L_0x0219:
            byte[] r9 = r18.toByteArray()
        L_0x021d:
            if (r9 == 0) goto L_0x0220
            int r8 = r9.length
        L_0x0220:
            java.lang.String r29 = "Content-Length"
            java.lang.String r30 = java.lang.String.valueOf(r8)
            r0 = r29
            r1 = r30
            r7.setRequestProperty(r0, r1)
        L_0x022e:
            long r22 = java.lang.System.currentTimeMillis()
            r24 = 0
            r7.connect()     // Catch:{ Exception -> 0x02df }
            r29 = 2
            r0 = r32
            r1 = r29
            if (r0 == r1) goto L_0x0247
            r29 = 3
            r0 = r32
            r1 = r29
            if (r0 != r1) goto L_0x0265
        L_0x0247:
            if (r9 == 0) goto L_0x0265
            int r0 = r9.length     // Catch:{ Exception -> 0x02df }
            r29 = r0
            if (r29 <= 0) goto L_0x0265
            java.io.DataOutputStream r25 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x02df }
            java.io.OutputStream r29 = r7.getOutputStream()     // Catch:{ Exception -> 0x02df }
            r0 = r25
            r1 = r29
            r0.<init>(r1)     // Catch:{ Exception -> 0x02df }
            r0 = r25
            r0.write(r9)     // Catch:{ Exception -> 0x034a, all -> 0x0346 }
            r25.flush()     // Catch:{ Exception -> 0x034a, all -> 0x0346 }
            r24 = r25
        L_0x0265:
            if (r24 == 0) goto L_0x026c
            r24.close()     // Catch:{ IOException -> 0x02da }
        L_0x026a:
            r24 = 0
        L_0x026c:
            int r29 = r7.getResponseCode()     // Catch:{ IOException -> 0x030b }
            r0 = r29
            r1 = r26
            r1.httpResponseCode = r0     // Catch:{ IOException -> 0x030b }
        L_0x0276:
            long r30 = java.lang.System.currentTimeMillis()
            long r30 = r30 - r22
            r0 = r30
            r2 = r26
            r2.rt = r0
            r10 = 0
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream
            r5.<init>()
            if (r35 == 0) goto L_0x0311
            java.lang.String r29 = "gzip"
            java.lang.String r30 = r7.getContentEncoding()     // Catch:{ IOException -> 0x02c3 }
            boolean r29 = r29.equals(r30)     // Catch:{ IOException -> 0x02c3 }
            if (r29 == 0) goto L_0x0311
            java.util.zip.GZIPInputStream r11 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x02c3 }
            java.io.InputStream r29 = r7.getInputStream()     // Catch:{ IOException -> 0x02c3 }
            r0 = r29
            r11.<init>(r0)     // Catch:{ IOException -> 0x02c3 }
            r10 = r11
        L_0x02a3:
            r29 = 2048(0x800, float:2.87E-42)
            r0 = r29
            byte[] r6 = new byte[r0]     // Catch:{ IOException -> 0x02c3 }
        L_0x02a9:
            r29 = 0
            r30 = 2048(0x800, float:2.87E-42)
            r0 = r29
            r1 = r30
            int r14 = r10.read(r6, r0, r1)     // Catch:{ IOException -> 0x02c3 }
            r29 = -1
            r0 = r29
            if (r14 == r0) goto L_0x031e
            r29 = 0
            r0 = r29
            r5.write(r6, r0, r14)     // Catch:{ IOException -> 0x02c3 }
            goto L_0x02a9
        L_0x02c3:
            r12 = move-exception
            r12.printStackTrace()     // Catch:{ all -> 0x033a }
            if (r10 == 0) goto L_0x000b
            r10.close()     // Catch:{ Exception -> 0x02ce }
            goto L_0x000b
        L_0x02ce:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x000b
        L_0x02d4:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0219
        L_0x02da:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x026a
        L_0x02df:
            r12 = move-exception
        L_0x02e0:
            r12.printStackTrace()     // Catch:{ all -> 0x02fd }
            long r30 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02fd }
            long r30 = r30 - r22
            r0 = r30
            r2 = r26
            r2.rt = r0     // Catch:{ all -> 0x02fd }
            if (r24 == 0) goto L_0x000b
            r24.close()     // Catch:{ IOException -> 0x02f8 }
        L_0x02f4:
            r24 = 0
            goto L_0x000b
        L_0x02f8:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x02f4
        L_0x02fd:
            r29 = move-exception
        L_0x02fe:
            if (r24 == 0) goto L_0x0305
            r24.close()     // Catch:{ IOException -> 0x0306 }
        L_0x0303:
            r24 = 0
        L_0x0305:
            throw r29
        L_0x0306:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0303
        L_0x030b:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0276
        L_0x0311:
            java.io.DataInputStream r11 = new java.io.DataInputStream     // Catch:{ IOException -> 0x02c3 }
            java.io.InputStream r29 = r7.getInputStream()     // Catch:{ IOException -> 0x02c3 }
            r0 = r29
            r11.<init>(r0)     // Catch:{ IOException -> 0x02c3 }
            r10 = r11
            goto L_0x02a3
        L_0x031e:
            if (r10 == 0) goto L_0x0323
            r10.close()     // Catch:{ Exception -> 0x0335 }
        L_0x0323:
            int r29 = r5.size()
            if (r29 <= 0) goto L_0x000b
            byte[] r29 = r5.toByteArray()
            r0 = r29
            r1 = r26
            r1.data = r0
            goto L_0x000b
        L_0x0335:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0323
        L_0x033a:
            r29 = move-exception
            if (r10 == 0) goto L_0x0340
            r10.close()     // Catch:{ Exception -> 0x0341 }
        L_0x0340:
            throw r29
        L_0x0341:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0340
        L_0x0346:
            r29 = move-exception
            r24 = r25
            goto L_0x02fe
        L_0x034a:
            r12 = move-exception
            r24 = r25
            goto L_0x02e0
        L_0x034e:
            r13 = move-exception
            r27 = r28
            goto L_0x014d
        L_0x0353:
            r13 = move-exception
            r27 = r28
            goto L_0x0147
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.HttpUtils.sendRequest(int, java.lang.String, java.util.Map, boolean):com.alibaba.analytics.utils.HttpUtils$HttpResponse");
    }
}
