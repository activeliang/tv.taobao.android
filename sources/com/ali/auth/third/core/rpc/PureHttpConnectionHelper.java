package com.ali.auth.third.core.rpc;

public class PureHttpConnectionHelper extends AbsHttpConnectionHelper {
    private static final String TAG = PureHttpConnectionHelper.class.getSimpleName();

    /* JADX WARNING: type inference failed for: r5v5, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0084 A[SYNTHETIC, Splitter:B:16:0x0084] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String get(java.lang.String r8) {
        /*
            java.lang.String r5 = TAG
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "get url = "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r8)
            java.lang.String r6 = r6.toString()
            com.ali.auth.third.core.trace.SDKLogger.d(r5, r6)
            r3 = 0
            r2 = 0
            java.net.URL r5 = new java.net.URL     // Catch:{ Throwable -> 0x00ac }
            r5.<init>(r8)     // Catch:{ Throwable -> 0x00ac }
            java.net.URLConnection r5 = r5.openConnection()     // Catch:{ Throwable -> 0x00ac }
            r0 = r5
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x00ac }
            r2 = r0
            r5 = 1
            r2.setDoInput(r5)     // Catch:{ Throwable -> 0x00ac }
            r5 = 1
            r2.setDoOutput(r5)     // Catch:{ Throwable -> 0x00ac }
            java.lang.String r5 = "GET"
            r2.setRequestMethod(r5)     // Catch:{ Throwable -> 0x00ac }
            r5 = 0
            r2.setUseCaches(r5)     // Catch:{ Throwable -> 0x00ac }
            r5 = 15000(0x3a98, float:2.102E-41)
            r2.setConnectTimeout(r5)     // Catch:{ Throwable -> 0x00ac }
            r5 = 15000(0x3a98, float:2.102E-41)
            r2.setReadTimeout(r5)     // Catch:{ Throwable -> 0x00ac }
            java.io.OutputStreamWriter r4 = new java.io.OutputStreamWriter     // Catch:{ Throwable -> 0x00ac }
            java.io.OutputStream r5 = r2.getOutputStream()     // Catch:{ Throwable -> 0x00ac }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x00ac }
            r4.flush()     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            int r5 = r2.getResponseCode()     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            r6 = 200(0xc8, float:2.8E-43)
            if (r5 == r6) goto L_0x0088
            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            r6.<init>()     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            java.lang.String r7 = "http request exception, response code: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            int r7 = r2.getResponseCode()     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            throw r5     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
        L_0x0076:
            r1 = move-exception
            r3 = r4
        L_0x0078:
            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ all -> 0x007e }
            r5.<init>(r1)     // Catch:{ all -> 0x007e }
            throw r5     // Catch:{ all -> 0x007e }
        L_0x007e:
            r5 = move-exception
        L_0x007f:
            com.ali.auth.third.core.util.IOUtils.closeQuietly(r3)
            if (r2 == 0) goto L_0x0087
            r2.disconnect()     // Catch:{ Exception -> 0x00a7 }
        L_0x0087:
            throw r5
        L_0x0088:
            java.io.InputStream r5 = r2.getInputStream()     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            java.io.ByteArrayOutputStream r5 = readResponse(r5)     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            java.lang.String r6 = r2.getContentType()     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            java.lang.String r6 = getCharset(r6)     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            java.lang.String r5 = r5.toString(r6)     // Catch:{ Throwable -> 0x0076, all -> 0x00a9 }
            com.ali.auth.third.core.util.IOUtils.closeQuietly(r4)
            if (r2 == 0) goto L_0x00a4
            r2.disconnect()     // Catch:{ Exception -> 0x00a5 }
        L_0x00a4:
            return r5
        L_0x00a5:
            r6 = move-exception
            goto L_0x00a4
        L_0x00a7:
            r6 = move-exception
            goto L_0x0087
        L_0x00a9:
            r5 = move-exception
            r3 = r4
            goto L_0x007f
        L_0x00ac:
            r1 = move-exception
            goto L_0x0078
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.core.rpc.PureHttpConnectionHelper.get(java.lang.String):java.lang.String");
    }
}
