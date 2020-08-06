package com.alibaba.motu.crashreporter.utrestapi;

public final class UTRestHttpUtils {
    public static final int HTTP_REQ_TYPE_GET = 1;
    public static final int HTTP_REQ_TYPE_POST_FORM_DATA = 2;
    public static final int HTTP_REQ_TYPE_POST_URL_ENCODED = 3;
    public static final int MAX_CONNECTION_TIME_OUT = 10000;
    public static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 60000;
    private static final String POST_Field_BOTTOM = "--GJircTeP--\r\n";
    private static final String POST_Field_TOP = "--GJircTeP\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream \r\n\r\n";

    /* JADX WARNING: Removed duplicated region for block: B:105:0x02b3 A[Catch:{ IOException -> 0x02bb, all -> 0x0344 }, LOOP:1: B:103:0x02a1->B:105:0x02b3, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x02fa A[SYNTHETIC, Splitter:B:126:0x02fa] */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x030d A[SYNTHETIC, Splitter:B:132:0x030d] */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x032b A[SYNTHETIC, Splitter:B:141:0x032b] */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x0334  */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x0355 A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:170:0x0329 A[EDGE_INSN: B:170:0x0329->B:140:0x0329 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:176:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] sendRequest(int r26, java.lang.String r27, java.util.Map<java.lang.String, java.lang.Object> r28, boolean r29) {
        /*
            boolean r23 = com.alibaba.motu.crashreporter.utils.StringUtils.isEmpty(r27)
            if (r23 == 0) goto L_0x0009
            r23 = 0
        L_0x0008:
            return r23
        L_0x0009:
            java.net.URL r22 = new java.net.URL     // Catch:{ MalformedURLException -> 0x013c, IOException -> 0x0149 }
            r0 = r22
            r1 = r27
            r0.<init>(r1)     // Catch:{ MalformedURLException -> 0x013c, IOException -> 0x0149 }
            java.net.URLConnection r6 = r22.openConnection()     // Catch:{ MalformedURLException -> 0x013c, IOException -> 0x0149 }
            java.net.HttpURLConnection r6 = (java.net.HttpURLConnection) r6     // Catch:{ MalformedURLException -> 0x013c, IOException -> 0x0149 }
            if (r6 == 0) goto L_0x0359
            r23 = 2
            r0 = r26
            r1 = r23
            if (r0 == r1) goto L_0x002a
            r23 = 3
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x0031
        L_0x002a:
            r23 = 1
            r0 = r23
            r6.setDoOutput(r0)
        L_0x0031:
            r23 = 1
            r0 = r23
            r6.setDoInput(r0)
            r23 = 2
            r0 = r26
            r1 = r23
            if (r0 == r1) goto L_0x0048
            r23 = 3
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x0156
        L_0x0048:
            java.lang.String r23 = "POST"
            r0 = r23
            r6.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x0160 }
        L_0x0050:
            r23 = 0
            r0 = r23
            r6.setUseCaches(r0)
            r23 = 10000(0x2710, float:1.4013E-41)
            r0 = r23
            r6.setConnectTimeout(r0)
            r23 = 60000(0xea60, float:8.4078E-41)
            r0 = r23
            r6.setReadTimeout(r0)
            java.lang.String r23 = "Connection"
            java.lang.String r24 = "close"
            r0 = r23
            r1 = r24
            r6.setRequestProperty(r0, r1)
            if (r29 == 0) goto L_0x0082
            java.lang.String r23 = "Accept-Encoding"
            java.lang.String r24 = "gzip,deflate"
            r0 = r23
            r1 = r24
            r6.setRequestProperty(r0, r1)
        L_0x0082:
            r23 = 1
            r0 = r23
            r6.setInstanceFollowRedirects(r0)
            r8 = 0
            r23 = 2
            r0 = r26
            r1 = r23
            if (r0 == r1) goto L_0x009a
            r23 = 3
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x0242
        L_0x009a:
            r23 = 2
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x016d
            java.lang.String r23 = "Content-Type"
            java.lang.String r24 = "multipart/form-data; boundary=GJircTeP"
            r0 = r23
            r1 = r24
            r6.setRequestProperty(r0, r1)
        L_0x00af:
            r7 = 0
            if (r28 == 0) goto L_0x0231
            int r23 = r28.size()
            if (r23 <= 0) goto L_0x0231
            java.io.ByteArrayOutputStream r16 = new java.io.ByteArrayOutputStream
            r16.<init>()
            java.util.Set r15 = r28.keySet()
            int r23 = r15.size()
            r0 = r23
            java.lang.String[] r0 = new java.lang.String[r0]
            r17 = r0
            r0 = r17
            r15.toArray(r0)
            com.alibaba.motu.crashreporter.utrestapi.UTKeyArraySorter r23 = com.alibaba.motu.crashreporter.utrestapi.UTKeyArraySorter.getInstance()
            r24 = 1
            r0 = r23
            r1 = r17
            r2 = r24
            java.lang.String[] r17 = r0.sortResourcesList(r1, r2)
            r3 = r17
            int r0 = r3.length
            r19 = r0
            r13 = 0
        L_0x00e6:
            r0 = r19
            if (r13 >= r0) goto L_0x0217
            r14 = r3[r13]
            r23 = 2
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x018e
            r0 = r28
            java.lang.Object r23 = r0.get(r14)
            byte[] r23 = (byte[]) r23
            r18 = r23
            byte[] r18 = (byte[]) r18
            if (r18 == 0) goto L_0x0139
            java.lang.String r23 = "--GJircTeP\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream \r\n\r\n"
            r24 = 2
            r0 = r24
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ IOException -> 0x0184 }
            r24 = r0
            r25 = 0
            r24[r25] = r14     // Catch:{ IOException -> 0x0184 }
            r25 = 1
            r24[r25] = r14     // Catch:{ IOException -> 0x0184 }
            java.lang.String r23 = java.lang.String.format(r23, r24)     // Catch:{ IOException -> 0x0184 }
            byte[] r23 = r23.getBytes()     // Catch:{ IOException -> 0x0184 }
            r0 = r16
            r1 = r23
            r0.write(r1)     // Catch:{ IOException -> 0x0184 }
            r0 = r16
            r1 = r18
            r0.write(r1)     // Catch:{ IOException -> 0x0184 }
            java.lang.String r23 = "\r\n"
            byte[] r23 = r23.getBytes()     // Catch:{ IOException -> 0x0184 }
            r0 = r16
            r1 = r23
            r0.write(r1)     // Catch:{ IOException -> 0x0184 }
        L_0x0139:
            int r13 = r13 + 1
            goto L_0x00e6
        L_0x013c:
            r11 = move-exception
            java.lang.String r23 = "connection error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            r23 = 0
            goto L_0x0008
        L_0x0149:
            r11 = move-exception
            java.lang.String r23 = "connection error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            r23 = 0
            goto L_0x0008
        L_0x0156:
            java.lang.String r23 = "GET"
            r0 = r23
            r6.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x0160 }
            goto L_0x0050
        L_0x0160:
            r11 = move-exception
            java.lang.String r23 = "connection error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            r23 = 0
            goto L_0x0008
        L_0x016d:
            r23 = 3
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x00af
            java.lang.String r23 = "Content-Type"
            java.lang.String r24 = "application/x-www-form-urlencoded"
            r0 = r23
            r1 = r24
            r6.setRequestProperty(r0, r1)
            goto L_0x00af
        L_0x0184:
            r11 = move-exception
            java.lang.String r23 = "write lBaos error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x0139
        L_0x018e:
            r23 = 3
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x0139
            r0 = r28
            java.lang.Object r18 = r0.get(r14)
            java.lang.String r18 = (java.lang.String) r18
            int r23 = r16.size()
            if (r23 <= 0) goto L_0x01e1
            java.lang.StringBuilder r23 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x01d6 }
            r23.<init>()     // Catch:{ IOException -> 0x01d6 }
            java.lang.String r24 = "&"
            java.lang.StringBuilder r23 = r23.append(r24)     // Catch:{ IOException -> 0x01d6 }
            r0 = r23
            java.lang.StringBuilder r23 = r0.append(r14)     // Catch:{ IOException -> 0x01d6 }
            java.lang.String r24 = "="
            java.lang.StringBuilder r23 = r23.append(r24)     // Catch:{ IOException -> 0x01d6 }
            r0 = r23
            r1 = r18
            java.lang.StringBuilder r23 = r0.append(r1)     // Catch:{ IOException -> 0x01d6 }
            java.lang.String r23 = r23.toString()     // Catch:{ IOException -> 0x01d6 }
            byte[] r23 = r23.getBytes()     // Catch:{ IOException -> 0x01d6 }
            r0 = r16
            r1 = r23
            r0.write(r1)     // Catch:{ IOException -> 0x01d6 }
            goto L_0x0139
        L_0x01d6:
            r11 = move-exception
            java.lang.String r23 = "write lBaos error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x0139
        L_0x01e1:
            java.lang.StringBuilder r23 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x020c }
            r23.<init>()     // Catch:{ IOException -> 0x020c }
            r0 = r23
            java.lang.StringBuilder r23 = r0.append(r14)     // Catch:{ IOException -> 0x020c }
            java.lang.String r24 = "="
            java.lang.StringBuilder r23 = r23.append(r24)     // Catch:{ IOException -> 0x020c }
            r0 = r23
            r1 = r18
            java.lang.StringBuilder r23 = r0.append(r1)     // Catch:{ IOException -> 0x020c }
            java.lang.String r23 = r23.toString()     // Catch:{ IOException -> 0x020c }
            byte[] r23 = r23.getBytes()     // Catch:{ IOException -> 0x020c }
            r0 = r16
            r1 = r23
            r0.write(r1)     // Catch:{ IOException -> 0x020c }
            goto L_0x0139
        L_0x020c:
            r11 = move-exception
            java.lang.String r23 = "write lBaos error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x0139
        L_0x0217:
            r23 = 2
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x022d
            java.lang.String r23 = "--GJircTeP--\r\n"
            byte[] r23 = r23.getBytes()     // Catch:{ IOException -> 0x02d8 }
            r0 = r16
            r1 = r23
            r0.write(r1)     // Catch:{ IOException -> 0x02d8 }
        L_0x022d:
            byte[] r8 = r16.toByteArray()
        L_0x0231:
            if (r8 == 0) goto L_0x0234
            int r7 = r8.length
        L_0x0234:
            java.lang.String r23 = "Content-Length"
            java.lang.String r24 = java.lang.String.valueOf(r7)
            r0 = r23
            r1 = r24
            r6.setRequestProperty(r0, r1)
        L_0x0242:
            r20 = 0
            r6.connect()     // Catch:{ Exception -> 0x02ed }
            r23 = 2
            r0 = r26
            r1 = r23
            if (r0 == r1) goto L_0x0257
            r23 = 3
            r0 = r26
            r1 = r23
            if (r0 != r1) goto L_0x0275
        L_0x0257:
            if (r8 == 0) goto L_0x0275
            int r0 = r8.length     // Catch:{ Exception -> 0x02ed }
            r23 = r0
            if (r23 <= 0) goto L_0x0275
            java.io.DataOutputStream r21 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x02ed }
            java.io.OutputStream r23 = r6.getOutputStream()     // Catch:{ Exception -> 0x02ed }
            r0 = r21
            r1 = r23
            r0.<init>(r1)     // Catch:{ Exception -> 0x02ed }
            r0 = r21
            r0.write(r8)     // Catch:{ Exception -> 0x0361, all -> 0x035d }
            r21.flush()     // Catch:{ Exception -> 0x0361, all -> 0x035d }
            r20 = r21
        L_0x0275:
            if (r20 == 0) goto L_0x027a
            r20.close()     // Catch:{ IOException -> 0x02e3 }
        L_0x027a:
            r9 = 0
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream
            r4.<init>()
            if (r29 == 0) goto L_0x031b
            java.lang.String r23 = "gzip"
            java.lang.String r24 = r6.getContentEncoding()     // Catch:{ IOException -> 0x02bb }
            boolean r23 = r23.equals(r24)     // Catch:{ IOException -> 0x02bb }
            if (r23 == 0) goto L_0x031b
            java.util.zip.GZIPInputStream r10 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x02bb }
            java.io.InputStream r23 = r6.getInputStream()     // Catch:{ IOException -> 0x02bb }
            r0 = r23
            r10.<init>(r0)     // Catch:{ IOException -> 0x02bb }
            r9 = r10
        L_0x029b:
            r23 = 2048(0x800, float:2.87E-42)
            r0 = r23
            byte[] r5 = new byte[r0]     // Catch:{ IOException -> 0x02bb }
        L_0x02a1:
            r23 = 0
            r24 = 2048(0x800, float:2.87E-42)
            r0 = r23
            r1 = r24
            int r12 = r9.read(r5, r0, r1)     // Catch:{ IOException -> 0x02bb }
            r23 = -1
            r0 = r23
            if (r12 == r0) goto L_0x0329
            r23 = 0
            r0 = r23
            r4.write(r5, r0, r12)     // Catch:{ IOException -> 0x02bb }
            goto L_0x02a1
        L_0x02bb:
            r11 = move-exception
            java.lang.String r23 = "write out error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)     // Catch:{ all -> 0x0344 }
            r23 = 0
            if (r9 == 0) goto L_0x0008
            r9.close()     // Catch:{ Exception -> 0x02cd }
            goto L_0x0008
        L_0x02cd:
            r11 = move-exception
            java.lang.String r24 = "out close error!"
            r0 = r24
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x0008
        L_0x02d8:
            r11 = move-exception
            java.lang.String r23 = "write lBaos error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x022d
        L_0x02e3:
            r11 = move-exception
            java.lang.String r23 = "out close error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x027a
        L_0x02ed:
            r11 = move-exception
        L_0x02ee:
            java.lang.String r23 = "write out error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)     // Catch:{ all -> 0x030a }
            r23 = 0
            if (r20 == 0) goto L_0x0008
            r20.close()     // Catch:{ IOException -> 0x02ff }
            goto L_0x0008
        L_0x02ff:
            r11 = move-exception
            java.lang.String r24 = "out close error!"
            r0 = r24
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x0008
        L_0x030a:
            r23 = move-exception
        L_0x030b:
            if (r20 == 0) goto L_0x0310
            r20.close()     // Catch:{ IOException -> 0x0311 }
        L_0x0310:
            throw r23
        L_0x0311:
            r11 = move-exception
            java.lang.String r24 = "out close error!"
            r0 = r24
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x0310
        L_0x031b:
            java.io.DataInputStream r10 = new java.io.DataInputStream     // Catch:{ IOException -> 0x02bb }
            java.io.InputStream r23 = r6.getInputStream()     // Catch:{ IOException -> 0x02bb }
            r0 = r23
            r10.<init>(r0)     // Catch:{ IOException -> 0x02bb }
            r9 = r10
            goto L_0x029b
        L_0x0329:
            if (r9 == 0) goto L_0x032e
            r9.close()     // Catch:{ Exception -> 0x033a }
        L_0x032e:
            int r23 = r4.size()
            if (r23 <= 0) goto L_0x0355
            byte[] r23 = r4.toByteArray()
            goto L_0x0008
        L_0x033a:
            r11 = move-exception
            java.lang.String r23 = "out close error!"
            r0 = r23
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x032e
        L_0x0344:
            r23 = move-exception
            if (r9 == 0) goto L_0x034a
            r9.close()     // Catch:{ Exception -> 0x034b }
        L_0x034a:
            throw r23
        L_0x034b:
            r11 = move-exception
            java.lang.String r24 = "out close error!"
            r0 = r24
            com.alibaba.motu.crashreporter.LogUtil.e(r0, r11)
            goto L_0x034a
        L_0x0355:
            r23 = 0
            goto L_0x0008
        L_0x0359:
            r23 = 0
            goto L_0x0008
        L_0x035d:
            r23 = move-exception
            r20 = r21
            goto L_0x030b
        L_0x0361:
            r11 = move-exception
            r20 = r21
            goto L_0x02ee
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.utrestapi.UTRestHttpUtils.sendRequest(int, java.lang.String, java.util.Map, boolean):byte[]");
    }
}
