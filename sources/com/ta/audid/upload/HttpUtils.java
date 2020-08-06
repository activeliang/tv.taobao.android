package com.ta.audid.upload;

public class HttpUtils {
    private static final int MAX_CONNECTION_TIME_OUT = 10000;
    private static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 10000;
    private static final long TIME_SCOPE = 1800000;
    private static UtdidSslSocketFactory mUtdidSslSocketFactory = null;

    static {
        System.setProperty("http.keepAlive", "true");
    }

    /* JADX WARNING: Removed duplicated region for block: B:101:0x0384 A[SYNTHETIC, Splitter:B:101:0x0384] */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x03a1 A[SYNTHETIC, Splitter:B:107:0x03a1] */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x03da  */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x03fd A[SYNTHETIC, Splitter:B:124:0x03fd] */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x0418 A[SYNTHETIC, Splitter:B:130:0x0418] */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x03fb A[EDGE_INSN: B:149:0x03fb->B:123:0x03fb ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x029e A[Catch:{ Exception -> 0x02a8 }, LOOP:1: B:69:0x028a->B:71:0x029e, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x02bd A[SYNTHETIC, Splitter:B:77:0x02bd] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.ta.audid.upload.HttpResponse sendRequest(java.lang.String r34, java.lang.String r35, boolean r36) {
        /*
            com.ta.audid.upload.HttpResponse r23 = new com.ta.audid.upload.HttpResponse
            r23.<init>()
            boolean r28 = android.text.TextUtils.isEmpty(r34)
            if (r28 == 0) goto L_0x000c
        L_0x000b:
            return r23
        L_0x000c:
            java.net.URL r27 = new java.net.URL     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            r0 = r27
            r1 = r34
            r0.<init>(r1)     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            java.net.URLConnection r9 = r27.openConnection()     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            java.net.HttpURLConnection r9 = (java.net.HttpURLConnection) r9     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            boolean r0 = r9 instanceof javax.net.ssl.HttpsURLConnection     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            r28 = r0
            if (r28 == 0) goto L_0x0044
            com.ta.audid.upload.UtdidSslSocketFactory r28 = mUtdidSslSocketFactory     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            if (r28 != 0) goto L_0x003a
            java.lang.String r28 = r27.getHost()     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            boolean r28 = android.text.TextUtils.isEmpty(r28)     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            if (r28 != 0) goto L_0x003a
            com.ta.audid.upload.UtdidSslSocketFactory r28 = new com.ta.audid.upload.UtdidSslSocketFactory     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            java.lang.String r29 = r27.getHost()     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            r28.<init>(r29)     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            mUtdidSslSocketFactory = r28     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
        L_0x003a:
            r0 = r9
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            r28 = r0
            com.ta.audid.upload.UtdidSslSocketFactory r29 = mUtdidSslSocketFactory     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
            r28.setSSLSocketFactory(r29)     // Catch:{ MalformedURLException -> 0x02d7, IOException -> 0x02ec, Throwable -> 0x0303 }
        L_0x0044:
            if (r9 == 0) goto L_0x000b
            r28 = 1
            r0 = r28
            r9.setDoInput(r0)
            if (r36 == 0) goto L_0x032f
            r28 = 1
            r0 = r28
            r9.setDoOutput(r0)
            java.lang.String r28 = "POST"
            r0 = r28
            r9.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x031a }
        L_0x005e:
            r28 = 0
            r0 = r28
            r9.setUseCaches(r0)
            r28 = 10000(0x2710, float:1.4013E-41)
            r0 = r28
            r9.setConnectTimeout(r0)
            r28 = 10000(0x2710, float:1.4013E-41)
            r0 = r28
            r9.setReadTimeout(r0)
            r28 = 1
            r0 = r28
            r9.setInstanceFollowRedirects(r0)
            java.lang.String r28 = "Content-Type"
            java.lang.String r29 = "application/x-www-form-urlencoded"
            r0 = r28
            r1 = r29
            r9.setRequestProperty(r0, r1)
            java.lang.String r28 = "Charset"
            java.lang.String r29 = "UTF-8"
            r0 = r28
            r1 = r29
            r9.setRequestProperty(r0, r1)
            java.lang.StringBuilder r24 = new java.lang.StringBuilder
            r24.<init>()
            com.ta.audid.Variables r28 = com.ta.audid.Variables.getInstance()
            java.lang.String r4 = r28.getAppkey()
            boolean r28 = android.text.TextUtils.isEmpty(r4)
            if (r28 != 0) goto L_0x00b4
            java.lang.String r28 = "x-audid-appkey"
            r0 = r28
            r9.setRequestProperty(r0, r4)
            r0 = r24
            r0.append(r4)
        L_0x00b4:
            com.ta.audid.Variables r28 = com.ta.audid.Variables.getInstance()
            android.content.Context r28 = r28.getContext()
            java.lang.String r5 = r28.getPackageName()
            boolean r28 = android.text.TextUtils.isEmpty(r5)
            if (r28 != 0) goto L_0x00de
            java.lang.String r28 = "x-audid-appname"
            java.lang.String r29 = "UTF-8"
            r0 = r29
            java.lang.String r29 = java.net.URLEncoder.encode(r5, r0)     // Catch:{ Exception -> 0x0448 }
            r0 = r28
            r1 = r29
            r9.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x0448 }
            r0 = r24
            r0.append(r5)     // Catch:{ Exception -> 0x0448 }
        L_0x00de:
            java.lang.String r28 = "x-audid-sdk"
            java.lang.String r29 = "2.0.5"
            r0 = r28
            r1 = r29
            r9.setRequestProperty(r0, r1)
            java.lang.String r28 = "2.0.5"
            r0 = r24
            r1 = r28
            r0.append(r1)
            com.ta.audid.Variables r28 = com.ta.audid.Variables.getInstance()
            java.lang.String r26 = r28.getCurrentTimeMillisString()
            java.lang.String r28 = "x-audid-timestamp"
            r0 = r28
            r1 = r26
            r9.setRequestProperty(r0, r1)
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            java.lang.StringBuilder r31 = new java.lang.StringBuilder
            r31.<init>()
            java.lang.String r32 = "timestamp:"
            java.lang.StringBuilder r31 = r31.append(r32)
            r0 = r31
            r1 = r26
            java.lang.StringBuilder r31 = r0.append(r1)
            java.lang.String r31 = r31.toString()
            r29[r30] = r31
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)
            r0 = r24
            r1 = r26
            r0.append(r1)
            r0 = r24
            r1 = r35
            r0.append(r1)
            java.lang.String r28 = r24.toString()
            java.lang.String r18 = com.ta.audid.utils.MD5Utils.getHmacMd5Hex(r28)
            java.lang.String r28 = "signature"
            byte[] r29 = r18.getBytes()
            r30 = 2
            java.lang.String r29 = com.ta.utdid2.android.utils.Base64.encodeToString(r29, r30)
            r0 = r28
            r1 = r29
            r9.setRequestProperty(r0, r1)
            long r20 = java.lang.System.currentTimeMillis()
            r19 = 0
            r9.connect()     // Catch:{ Throwable -> 0x0363 }
            if (r35 == 0) goto L_0x0185
            int r28 = r35.length()     // Catch:{ Throwable -> 0x0363 }
            if (r28 <= 0) goto L_0x0185
            java.io.DataOutputStream r22 = new java.io.DataOutputStream     // Catch:{ Throwable -> 0x0363 }
            java.io.OutputStream r28 = r9.getOutputStream()     // Catch:{ Throwable -> 0x0363 }
            r0 = r22
            r1 = r28
            r0.<init>(r1)     // Catch:{ Throwable -> 0x0363 }
            r0 = r22
            r1 = r35
            r0.writeBytes(r1)     // Catch:{ Throwable -> 0x0443, all -> 0x043e }
            r22.flush()     // Catch:{ Throwable -> 0x0443, all -> 0x043e }
            r19 = r22
        L_0x0185:
            if (r19 == 0) goto L_0x018a
            r19.close()     // Catch:{ IOException -> 0x034e }
        L_0x018a:
            int r28 = r9.getResponseCode()     // Catch:{ Exception -> 0x03b9 }
            r0 = r28
            r1 = r23
            r1.httpResponseCode = r0     // Catch:{ Exception -> 0x03b9 }
            java.lang.String r28 = "signature"
            r0 = r28
            java.lang.String r28 = r9.getHeaderField(r0)     // Catch:{ Exception -> 0x03b9 }
            r0 = r28
            r1 = r23
            r1.signature = r0     // Catch:{ Exception -> 0x03b9 }
        L_0x01a3:
            java.lang.String r28 = "x-audid-timestamp"
            r0 = r28
            java.lang.String r6 = r9.getHeaderField(r0)     // Catch:{ Exception -> 0x043b }
            long r28 = java.lang.Long.parseLong(r6)     // Catch:{ Exception -> 0x043b }
            r0 = r28
            r2 = r23
            r2.timestamp = r0     // Catch:{ Exception -> 0x043b }
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x043b }
            r29 = r0
            r30 = 0
            java.lang.StringBuilder r31 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x043b }
            r31.<init>()     // Catch:{ Exception -> 0x043b }
            java.lang.String r32 = "repsonse.timestamp:"
            java.lang.StringBuilder r31 = r31.append(r32)     // Catch:{ Exception -> 0x043b }
            r0 = r23
            long r0 = r0.timestamp     // Catch:{ Exception -> 0x043b }
            r32 = r0
            java.lang.StringBuilder r31 = r31.append(r32)     // Catch:{ Exception -> 0x043b }
            java.lang.String r31 = r31.toString()     // Catch:{ Exception -> 0x043b }
            r29[r30] = r31     // Catch:{ Exception -> 0x043b }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)     // Catch:{ Exception -> 0x043b }
            com.ta.audid.Variables r28 = com.ta.audid.Variables.getInstance()     // Catch:{ Exception -> 0x043b }
            long r10 = r28.getCurrentTimeMillis()     // Catch:{ Exception -> 0x043b }
            r0 = r23
            long r0 = r0.timestamp     // Catch:{ Exception -> 0x043b }
            r28 = r0
            r30 = 0
            int r28 = (r28 > r30 ? 1 : (r28 == r30 ? 0 : -1))
            if (r28 <= 0) goto L_0x0225
            r0 = r23
            long r0 = r0.timestamp     // Catch:{ Exception -> 0x043b }
            r28 = r0
            r30 = 1800000(0x1b7740, double:8.89318E-318)
            long r30 = r30 + r10
            int r28 = (r28 > r30 ? 1 : (r28 == r30 ? 0 : -1))
            if (r28 > 0) goto L_0x0214
            r0 = r23
            long r0 = r0.timestamp     // Catch:{ Exception -> 0x043b }
            r28 = r0
            r30 = 1800000(0x1b7740, double:8.89318E-318)
            long r30 = r10 - r30
            int r28 = (r28 > r30 ? 1 : (r28 == r30 ? 0 : -1))
            if (r28 >= 0) goto L_0x0225
        L_0x0214:
            com.ta.audid.Variables r28 = com.ta.audid.Variables.getInstance()     // Catch:{ Exception -> 0x043b }
            r0 = r23
            long r0 = r0.timestamp     // Catch:{ Exception -> 0x043b }
            r30 = r0
            r0 = r28
            r1 = r30
            r0.setSystemTime(r1)     // Catch:{ Exception -> 0x043b }
        L_0x0225:
            long r28 = java.lang.System.currentTimeMillis()
            long r28 = r28 - r20
            r0 = r28
            r2 = r23
            r2.rt = r0
            r12 = 0
            java.io.ByteArrayOutputStream r7 = new java.io.ByteArrayOutputStream
            r7.<init>()
            java.io.DataInputStream r13 = new java.io.DataInputStream     // Catch:{ IOException -> 0x0437 }
            java.io.InputStream r28 = r9.getInputStream()     // Catch:{ IOException -> 0x0437 }
            r0 = r28
            r13.<init>(r0)     // Catch:{ IOException -> 0x0437 }
            r28 = 2048(0x800, float:2.87E-42)
            r0 = r28
            byte[] r8 = new byte[r0]     // Catch:{ IOException -> 0x0266 }
        L_0x0248:
            r28 = 0
            r29 = 2048(0x800, float:2.87E-42)
            r0 = r28
            r1 = r29
            int r17 = r13.read(r8, r0, r1)     // Catch:{ IOException -> 0x0266 }
            r28 = -1
            r0 = r17
            r1 = r28
            if (r0 == r1) goto L_0x03ce
            r28 = 0
            r0 = r28
            r1 = r17
            r7.write(r8, r0, r1)     // Catch:{ IOException -> 0x0266 }
            goto L_0x0248
        L_0x0266:
            r14 = move-exception
        L_0x0267:
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0430 }
            r29 = r0
            r30 = 0
            r29[r30] = r14     // Catch:{ all -> 0x0430 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)     // Catch:{ all -> 0x0430 }
            java.io.DataInputStream r12 = new java.io.DataInputStream     // Catch:{ Exception -> 0x0433 }
            java.io.InputStream r28 = r9.getErrorStream()     // Catch:{ Exception -> 0x0433 }
            r0 = r28
            r12.<init>(r0)     // Catch:{ Exception -> 0x0433 }
            r28 = 2048(0x800, float:2.87E-42)
            r0 = r28
            byte[] r8 = new byte[r0]     // Catch:{ Exception -> 0x02a8 }
        L_0x028a:
            r28 = 0
            r29 = 2048(0x800, float:2.87E-42)
            r0 = r28
            r1 = r29
            int r17 = r12.read(r8, r0, r1)     // Catch:{ Exception -> 0x02a8 }
            r28 = -1
            r0 = r17
            r1 = r28
            if (r0 == r1) goto L_0x03fb
            r28 = 0
            r0 = r28
            r1 = r17
            r7.write(r8, r0, r1)     // Catch:{ Exception -> 0x02a8 }
            goto L_0x028a
        L_0x02a8:
            r16 = move-exception
        L_0x02a9:
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0415 }
            r29 = r0
            r30 = 0
            r29[r30] = r16     // Catch:{ all -> 0x0415 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)     // Catch:{ all -> 0x0415 }
            if (r12 == 0) goto L_0x000b
            r12.close()     // Catch:{ Exception -> 0x02c2 }
            goto L_0x000b
        L_0x02c2:
            r14 = move-exception
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            r29[r30] = r14
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)
            goto L_0x000b
        L_0x02d7:
            r15 = move-exception
            java.lang.String r28 = ""
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r28
            r1 = r29
            com.ta.audid.utils.UtdidLogger.e(r0, r15, r1)
            goto L_0x000b
        L_0x02ec:
            r16 = move-exception
            java.lang.String r28 = ""
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r28
            r1 = r16
            r2 = r29
            com.ta.audid.utils.UtdidLogger.e(r0, r1, r2)
            goto L_0x000b
        L_0x0303:
            r25 = move-exception
            java.lang.String r28 = ""
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r28
            r1 = r25
            r2 = r29
            com.ta.audid.utils.UtdidLogger.e(r0, r1, r2)
            goto L_0x000b
        L_0x031a:
            r14 = move-exception
            java.lang.String r28 = ""
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r28
            r1 = r29
            com.ta.audid.utils.UtdidLogger.e(r0, r14, r1)
            goto L_0x000b
        L_0x032f:
            java.lang.String r28 = "GET"
            r0 = r28
            r9.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x0339 }
            goto L_0x005e
        L_0x0339:
            r14 = move-exception
            java.lang.String r28 = ""
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r28
            r1 = r29
            com.ta.audid.utils.UtdidLogger.e(r0, r14, r1)
            goto L_0x000b
        L_0x034e:
            r14 = move-exception
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            r29[r30] = r14
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)
            goto L_0x018a
        L_0x0363:
            r14 = move-exception
        L_0x0364:
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x039e }
            r29 = r0
            r30 = 0
            r29[r30] = r14     // Catch:{ all -> 0x039e }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)     // Catch:{ all -> 0x039e }
            long r28 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x039e }
            long r28 = r28 - r20
            r0 = r28
            r2 = r23
            r2.rt = r0     // Catch:{ all -> 0x039e }
            if (r19 == 0) goto L_0x000b
            r19.close()     // Catch:{ IOException -> 0x0389 }
            goto L_0x000b
        L_0x0389:
            r14 = move-exception
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            r29[r30] = r14
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)
            goto L_0x000b
        L_0x039e:
            r28 = move-exception
        L_0x039f:
            if (r19 == 0) goto L_0x03a4
            r19.close()     // Catch:{ IOException -> 0x03a5 }
        L_0x03a4:
            throw r28
        L_0x03a5:
            r14 = move-exception
            java.lang.String r29 = ""
            r30 = 1
            r0 = r30
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r30 = r0
            r31 = 0
            r30[r31] = r14
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r29, (java.lang.Object[]) r30)
            goto L_0x03a4
        L_0x03b9:
            r14 = move-exception
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            r29[r30] = r14
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)
            goto L_0x01a3
        L_0x03ce:
            if (r13 == 0) goto L_0x044b
            r13.close()     // Catch:{ Exception -> 0x03e6 }
            r12 = r13
        L_0x03d4:
            int r28 = r7.size()
            if (r28 <= 0) goto L_0x000b
            byte[] r28 = r7.toByteArray()
            r0 = r28
            r1 = r23
            r1.data = r0
            goto L_0x000b
        L_0x03e6:
            r14 = move-exception
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            r29[r30] = r14
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)
            r12 = r13
            goto L_0x03d4
        L_0x03fb:
            if (r12 == 0) goto L_0x03d4
            r12.close()     // Catch:{ Exception -> 0x0401 }
            goto L_0x03d4
        L_0x0401:
            r14 = move-exception
            java.lang.String r28 = ""
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            r29[r30] = r14
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r28, (java.lang.Object[]) r29)
            goto L_0x03d4
        L_0x0415:
            r28 = move-exception
        L_0x0416:
            if (r12 == 0) goto L_0x041b
            r12.close()     // Catch:{ Exception -> 0x041c }
        L_0x041b:
            throw r28
        L_0x041c:
            r14 = move-exception
            java.lang.String r29 = ""
            r30 = 1
            r0 = r30
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r30 = r0
            r31 = 0
            r30[r31] = r14
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r29, (java.lang.Object[]) r30)
            goto L_0x041b
        L_0x0430:
            r28 = move-exception
            r12 = r13
            goto L_0x0416
        L_0x0433:
            r16 = move-exception
            r12 = r13
            goto L_0x02a9
        L_0x0437:
            r14 = move-exception
            r13 = r12
            goto L_0x0267
        L_0x043b:
            r28 = move-exception
            goto L_0x0225
        L_0x043e:
            r28 = move-exception
            r19 = r22
            goto L_0x039f
        L_0x0443:
            r14 = move-exception
            r19 = r22
            goto L_0x0364
        L_0x0448:
            r28 = move-exception
            goto L_0x00de
        L_0x044b:
            r12 = r13
            goto L_0x03d4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.upload.HttpUtils.sendRequest(java.lang.String, java.lang.String, boolean):com.ta.audid.upload.HttpResponse");
    }
}
