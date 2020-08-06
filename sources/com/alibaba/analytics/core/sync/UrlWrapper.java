package com.alibaba.analytics.core.sync;

import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;

public class UrlWrapper {
    private static final int ENVIRONMENT_BETA = 1;
    private static final int ENVIRONMENT_DAILY = 3;
    private static final int ENVIRONMENT_ONLINE = 0;
    private static final int ENVIRONMENT_PRE = 2;
    private static final int HTTP_ENVIRONMENT = 0;
    private static final int MAX_CONNECTION_TIME_OUT = 10000;
    private static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 60000;
    public static int mErrorCode = 0;
    public static final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private static UtHostnameVerifier mUtHostnameVerifier = null;
    private static UtSslSocketFactory mUtSslSocketFactory = null;

    static {
        System.setProperty("http.keepAlive", "true");
    }

    /* JADX WARNING: Removed duplicated region for block: B:104:0x0379 A[SYNTHETIC, Splitter:B:104:0x0379] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x0396 A[SYNTHETIC, Splitter:B:110:0x0396] */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x03e4 A[SYNTHETIC, Splitter:B:125:0x03e4] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x01fa A[SYNTHETIC, Splitter:B:62:0x01fa] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0203  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x030d A[Catch:{ all -> 0x0393 }] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0340 A[SYNTHETIC, Splitter:B:95:0x0340] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.analytics.core.sync.BizResponse sendRequest(byte[] r28) {
        /*
            com.alibaba.analytics.utils.Logger.d()
            com.alibaba.analytics.core.sync.BizResponse r4 = new com.alibaba.analytics.core.sync.BizResponse
            r4.<init>()
            java.net.URL r21 = new java.net.URL     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            com.alibaba.analytics.core.sync.HttpsHostPortMgr r22 = com.alibaba.analytics.core.sync.HttpsHostPortMgr.getInstance()     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            java.lang.String r22 = r22.getHttpsUrl()     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r21.<init>(r22)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            java.net.URLConnection r7 = r21.openConnection()     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            java.net.HttpURLConnection r7 = (java.net.HttpURLConnection) r7     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            boolean r0 = r7 instanceof javax.net.ssl.HttpsURLConnection     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r22 = r0
            if (r22 == 0) goto L_0x00a0
            java.lang.String r13 = r21.getHost()     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            boolean r22 = android.text.TextUtils.isEmpty(r13)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            if (r22 == 0) goto L_0x002c
        L_0x002b:
            return r4
        L_0x002c:
            com.alibaba.analytics.core.sync.UtSslSocketFactory r22 = mUtSslSocketFactory     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            if (r22 == 0) goto L_0x003e
            com.alibaba.analytics.core.sync.UtSslSocketFactory r22 = mUtSslSocketFactory     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            java.lang.String r22 = r22.getHost()     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r0 = r22
            boolean r22 = r13.equals(r0)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            if (r22 != 0) goto L_0x005c
        L_0x003e:
            java.lang.String r22 = "UrlWrapper"
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r23 = r0
            r24 = 0
            java.lang.String r25 = "new SslSocketFactory"
            r23[r24] = r25     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            com.alibaba.analytics.core.sync.UtSslSocketFactory r22 = new com.alibaba.analytics.core.sync.UtSslSocketFactory     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r0 = r22
            r0.<init>(r13)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            mUtSslSocketFactory = r22     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
        L_0x005c:
            r0 = r7
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r22 = r0
            com.alibaba.analytics.core.sync.UtSslSocketFactory r23 = mUtSslSocketFactory     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r22.setSSLSocketFactory(r23)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            com.alibaba.analytics.core.sync.UtHostnameVerifier r22 = mUtHostnameVerifier     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            if (r22 == 0) goto L_0x0078
            com.alibaba.analytics.core.sync.UtHostnameVerifier r22 = mUtHostnameVerifier     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            java.lang.String r22 = r22.getHost()     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r0 = r22
            boolean r22 = r13.equals(r0)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            if (r22 != 0) goto L_0x0096
        L_0x0078:
            java.lang.String r22 = "UrlWrapper"
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r23 = r0
            r24 = 0
            java.lang.String r25 = "new HostnameVerifier"
            r23[r24] = r25     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            com.alibaba.analytics.core.sync.UtHostnameVerifier r22 = new com.alibaba.analytics.core.sync.UtHostnameVerifier     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r0 = r22
            r0.<init>(r13)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            mUtHostnameVerifier = r22     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
        L_0x0096:
            r0 = r7
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r22 = r0
            com.alibaba.analytics.core.sync.UtHostnameVerifier r23 = mUtHostnameVerifier     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
            r22.setHostnameVerifier(r23)     // Catch:{ MalformedURLException -> 0x021b, IOException -> 0x0230 }
        L_0x00a0:
            if (r7 == 0) goto L_0x002b
            r22 = 1
            r0 = r22
            r7.setDoOutput(r0)
            r22 = 1
            r0 = r22
            r7.setDoInput(r0)
            java.lang.String r22 = "POST"
            r0 = r22
            r7.setRequestMethod(r0)     // Catch:{ ProtocolException -> 0x0245 }
            r22 = 0
            r0 = r22
            r7.setUseCaches(r0)
            r22 = 10000(0x2710, float:1.4013E-41)
            r0 = r22
            r7.setConnectTimeout(r0)
            r22 = 60000(0xea60, float:8.4078E-41)
            r0 = r22
            r7.setReadTimeout(r0)
            r22 = 1
            r0 = r22
            r7.setInstanceFollowRedirects(r0)
            java.lang.String r22 = "Content-Type"
            java.lang.String r23 = "application/x-www-form-urlencoded"
            r0 = r22
            r1 = r23
            r7.setRequestProperty(r0, r1)
            java.lang.String r22 = "Charset"
            java.lang.String r23 = "UTF-8"
            r0 = r22
            r1 = r23
            r7.setRequestProperty(r0, r1)
            com.alibaba.analytics.core.Variables r22 = com.alibaba.analytics.core.Variables.getInstance()
            java.lang.String r2 = r22.getAppkey()
            boolean r22 = android.text.TextUtils.isEmpty(r2)
            if (r22 != 0) goto L_0x0105
            java.lang.String r22 = "x-k"
            r0 = r22
            r7.setRequestProperty(r0, r2)
        L_0x0105:
            com.alibaba.analytics.core.Variables r22 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Throwable -> 0x0288 }
            com.ut.mini.core.sign.IUTRequestAuthentication r19 = r22.getRequestAuthenticationInstance()     // Catch:{ Throwable -> 0x0288 }
            if (r19 == 0) goto L_0x017a
            java.lang.String r22 = com.alibaba.analytics.utils.MD5Utils.getMd5Hex(r28)     // Catch:{ Throwable -> 0x0288 }
            r0 = r19
            r1 = r22
            java.lang.String r20 = r0.getSign(r1)     // Catch:{ Throwable -> 0x0288 }
            java.lang.String r22 = ""
            r23 = 2
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x0288 }
            r23 = r0
            r24 = 0
            java.lang.String r25 = "signValue"
            r23[r24] = r25     // Catch:{ Throwable -> 0x0288 }
            r24 = 1
            r23[r24] = r20     // Catch:{ Throwable -> 0x0288 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ Throwable -> 0x0288 }
            java.lang.String r22 = "x-s"
            r0 = r22
            r1 = r20
            r7.setRequestProperty(r0, r1)     // Catch:{ Throwable -> 0x0288 }
            r0 = r19
            boolean r0 = r0 instanceof com.ut.mini.core.sign.UTBaseRequestAuthentication     // Catch:{ Throwable -> 0x0288 }
            r22 = r0
            if (r22 == 0) goto L_0x029d
            com.ut.mini.core.sign.UTBaseRequestAuthentication r19 = (com.ut.mini.core.sign.UTBaseRequestAuthentication) r19     // Catch:{ Throwable -> 0x0288 }
            boolean r22 = r19.isEncode()     // Catch:{ Throwable -> 0x0288 }
            if (r22 == 0) goto L_0x025a
            java.lang.String r22 = "x-t"
            java.lang.String r23 = "2"
            r0 = r22
            r1 = r23
            r7.setRequestProperty(r0, r1)     // Catch:{ Throwable -> 0x0288 }
            java.lang.String r22 = ""
            r23 = 2
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x0288 }
            r23 = r0
            r24 = 0
            java.lang.String r25 = "x-t"
            r23[r24] = r25     // Catch:{ Throwable -> 0x0288 }
            r24 = 1
            r25 = 2
            java.lang.Integer r25 = java.lang.Integer.valueOf(r25)     // Catch:{ Throwable -> 0x0288 }
            r23[r24] = r25     // Catch:{ Throwable -> 0x0288 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ Throwable -> 0x0288 }
        L_0x017a:
            long r16 = java.lang.System.currentTimeMillis()
            r15 = 0
            r7.connect()     // Catch:{ SSLHandshakeException -> 0x02f0, Exception -> 0x035a }
            if (r28 == 0) goto L_0x01a4
            r0 = r28
            int r0 = r0.length     // Catch:{ SSLHandshakeException -> 0x02f0, Exception -> 0x035a }
            r22 = r0
            if (r22 <= 0) goto L_0x01a4
            java.io.DataOutputStream r18 = new java.io.DataOutputStream     // Catch:{ SSLHandshakeException -> 0x02f0, Exception -> 0x035a }
            java.io.OutputStream r22 = r7.getOutputStream()     // Catch:{ SSLHandshakeException -> 0x02f0, Exception -> 0x035a }
            r0 = r18
            r1 = r22
            r0.<init>(r1)     // Catch:{ SSLHandshakeException -> 0x02f0, Exception -> 0x035a }
            r0 = r18
            r1 = r28
            r0.write(r1)     // Catch:{ SSLHandshakeException -> 0x040b, Exception -> 0x0406, all -> 0x0402 }
            r18.flush()     // Catch:{ SSLHandshakeException -> 0x040b, Exception -> 0x0406, all -> 0x0402 }
            r15 = r18
        L_0x01a4:
            if (r15 == 0) goto L_0x01a9
            r15.close()     // Catch:{ IOException -> 0x02db }
        L_0x01a9:
            long r22 = java.lang.System.currentTimeMillis()
            long r22 = r22 - r16
            r0 = r22
            r4.rt = r0
            r8 = 0
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream
            r5.<init>()
            java.io.DataInputStream r9 = new java.io.DataInputStream     // Catch:{ IOException -> 0x03ff }
            java.io.InputStream r22 = r7.getInputStream()     // Catch:{ IOException -> 0x03ff }
            r0 = r22
            r9.<init>(r0)     // Catch:{ IOException -> 0x03ff }
            r22 = 2048(0x800, float:2.87E-42)
            r0 = r22
            byte[] r6 = new byte[r0]     // Catch:{ IOException -> 0x01e4, all -> 0x03fc }
        L_0x01ca:
            r22 = 0
            r23 = 2048(0x800, float:2.87E-42)
            r0 = r22
            r1 = r23
            int r14 = r9.read(r6, r0, r1)     // Catch:{ IOException -> 0x01e4, all -> 0x03fc }
            r22 = -1
            r0 = r22
            if (r14 == r0) goto L_0x03ae
            r22 = 0
            r0 = r22
            r5.write(r6, r0, r14)     // Catch:{ IOException -> 0x01e4, all -> 0x03fc }
            goto L_0x01ca
        L_0x01e4:
            r10 = move-exception
            r8 = r9
        L_0x01e6:
            java.lang.String r22 = ""
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x03e1 }
            r23 = r0
            r24 = 0
            r23[r24] = r10     // Catch:{ all -> 0x03e1 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ all -> 0x03e1 }
            if (r8 == 0) goto L_0x01fd
            r8.close()     // Catch:{ Exception -> 0x03cc }
        L_0x01fd:
            int r22 = r5.size()
            if (r22 <= 0) goto L_0x002b
            byte[] r22 = r5.toByteArray()
            int r22 = com.alibaba.analytics.core.sync.BizRequest.parseResult(r22)
            mErrorCode = r22
            int r22 = mErrorCode
            r0 = r22
            r4.errCode = r0
            java.lang.String r22 = com.alibaba.analytics.core.sync.BizRequest.mResponseAdditionalData
            r0 = r22
            r4.data = r0
            goto L_0x002b
        L_0x021b:
            r11 = move-exception
            java.lang.String r22 = ""
            r23 = 0
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r0 = r22
            r1 = r23
            com.alibaba.analytics.utils.Logger.e(r0, r11, r1)
            goto L_0x002b
        L_0x0230:
            r12 = move-exception
            java.lang.String r22 = ""
            r23 = 0
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r0 = r22
            r1 = r23
            com.alibaba.analytics.utils.Logger.e(r0, r12, r1)
            goto L_0x002b
        L_0x0245:
            r10 = move-exception
            java.lang.String r22 = ""
            r23 = 0
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r0 = r22
            r1 = r23
            com.alibaba.analytics.utils.Logger.e(r0, r10, r1)
            goto L_0x002b
        L_0x025a:
            java.lang.String r22 = "x-t"
            java.lang.String r23 = "3"
            r0 = r22
            r1 = r23
            r7.setRequestProperty(r0, r1)     // Catch:{ Throwable -> 0x0288 }
            java.lang.String r22 = ""
            r23 = 2
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x0288 }
            r23 = r0
            r24 = 0
            java.lang.String r25 = "x-t"
            r23[r24] = r25     // Catch:{ Throwable -> 0x0288 }
            r24 = 1
            r25 = 3
            java.lang.Integer r25 = java.lang.Integer.valueOf(r25)     // Catch:{ Throwable -> 0x0288 }
            r23[r24] = r25     // Catch:{ Throwable -> 0x0288 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ Throwable -> 0x0288 }
            goto L_0x017a
        L_0x0288:
            r10 = move-exception
            java.lang.String r22 = ""
            r23 = 0
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r0 = r22
            r1 = r23
            com.alibaba.analytics.utils.Logger.e(r0, r10, r1)
            goto L_0x017a
        L_0x029d:
            r0 = r19
            boolean r0 = r0 instanceof com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication     // Catch:{ Throwable -> 0x0288 }
            r22 = r0
            if (r22 != 0) goto L_0x02ad
            r0 = r19
            boolean r0 = r0 instanceof com.ut.mini.core.sign.UTSecurityThridRequestAuthentication     // Catch:{ Throwable -> 0x0288 }
            r22 = r0
            if (r22 == 0) goto L_0x017a
        L_0x02ad:
            java.lang.String r22 = "x-t"
            java.lang.String r23 = "1"
            r0 = r22
            r1 = r23
            r7.setRequestProperty(r0, r1)     // Catch:{ Throwable -> 0x0288 }
            java.lang.String r22 = ""
            r23 = 2
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x0288 }
            r23 = r0
            r24 = 0
            java.lang.String r25 = "x-t"
            r23[r24] = r25     // Catch:{ Throwable -> 0x0288 }
            r24 = 1
            r25 = 1
            java.lang.Integer r25 = java.lang.Integer.valueOf(r25)     // Catch:{ Throwable -> 0x0288 }
            r23[r24] = r25     // Catch:{ Throwable -> 0x0288 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ Throwable -> 0x0288 }
            goto L_0x017a
        L_0x02db:
            r10 = move-exception
            java.lang.String r22 = ""
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r24 = 0
            r23[r24] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)
            goto L_0x01a9
        L_0x02f0:
            r10 = move-exception
        L_0x02f1:
            java.lang.String r22 = ""
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0393 }
            r23 = r0
            r24 = 0
            r23[r24] = r10     // Catch:{ all -> 0x0393 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ all -> 0x0393 }
            com.alibaba.analytics.core.Variables r22 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ all -> 0x0393 }
            boolean r22 = r22.isSelfMonitorTurnOn()     // Catch:{ all -> 0x0393 }
            if (r22 == 0) goto L_0x0334
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ all -> 0x0393 }
            r3.<init>()     // Catch:{ all -> 0x0393 }
            java.lang.String r22 = "type"
            java.lang.String r23 = "3"
            r0 = r22
            r1 = r23
            r3.put(r0, r1)     // Catch:{ all -> 0x0393 }
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r22 = mMonitor     // Catch:{ all -> 0x0393 }
            int r23 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.UPLOAD_FAILED     // Catch:{ all -> 0x0393 }
            java.lang.String r24 = com.alibaba.fastjson.JSON.toJSONString(r3)     // Catch:{ all -> 0x0393 }
            r26 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Double r25 = java.lang.Double.valueOf(r26)     // Catch:{ all -> 0x0393 }
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r23 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r23, r24, r25)     // Catch:{ all -> 0x0393 }
            r22.onEvent(r23)     // Catch:{ all -> 0x0393 }
        L_0x0334:
            long r22 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0393 }
            long r22 = r22 - r16
            r0 = r22
            r4.rt = r0     // Catch:{ all -> 0x0393 }
            if (r15 == 0) goto L_0x002b
            r15.close()     // Catch:{ IOException -> 0x0345 }
            goto L_0x002b
        L_0x0345:
            r10 = move-exception
            java.lang.String r22 = ""
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r24 = 0
            r23[r24] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)
            goto L_0x002b
        L_0x035a:
            r10 = move-exception
        L_0x035b:
            java.lang.String r22 = ""
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0393 }
            r23 = r0
            r24 = 0
            r23[r24] = r10     // Catch:{ all -> 0x0393 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)     // Catch:{ all -> 0x0393 }
            long r22 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0393 }
            long r22 = r22 - r16
            r0 = r22
            r4.rt = r0     // Catch:{ all -> 0x0393 }
            if (r15 == 0) goto L_0x002b
            r15.close()     // Catch:{ IOException -> 0x037e }
            goto L_0x002b
        L_0x037e:
            r10 = move-exception
            java.lang.String r22 = ""
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r24 = 0
            r23[r24] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)
            goto L_0x002b
        L_0x0393:
            r22 = move-exception
        L_0x0394:
            if (r15 == 0) goto L_0x0399
            r15.close()     // Catch:{ IOException -> 0x039a }
        L_0x0399:
            throw r22
        L_0x039a:
            r10 = move-exception
            java.lang.String r23 = ""
            r24 = 1
            r0 = r24
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r24 = r0
            r25 = 0
            r24[r25] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r23, (java.lang.Object[]) r24)
            goto L_0x0399
        L_0x03ae:
            if (r9 == 0) goto L_0x0410
            r9.close()     // Catch:{ Exception -> 0x03b6 }
            r8 = r9
            goto L_0x01fd
        L_0x03b6:
            r10 = move-exception
            java.lang.String r22 = ""
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r24 = 0
            r23[r24] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)
            r8 = r9
            goto L_0x01fd
        L_0x03cc:
            r10 = move-exception
            java.lang.String r22 = ""
            r23 = 1
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r24 = 0
            r23[r24] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r22, (java.lang.Object[]) r23)
            goto L_0x01fd
        L_0x03e1:
            r22 = move-exception
        L_0x03e2:
            if (r8 == 0) goto L_0x03e7
            r8.close()     // Catch:{ Exception -> 0x03e8 }
        L_0x03e7:
            throw r22
        L_0x03e8:
            r10 = move-exception
            java.lang.String r23 = ""
            r24 = 1
            r0 = r24
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r24 = r0
            r25 = 0
            r24[r25] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r23, (java.lang.Object[]) r24)
            goto L_0x03e7
        L_0x03fc:
            r22 = move-exception
            r8 = r9
            goto L_0x03e2
        L_0x03ff:
            r10 = move-exception
            goto L_0x01e6
        L_0x0402:
            r22 = move-exception
            r15 = r18
            goto L_0x0394
        L_0x0406:
            r10 = move-exception
            r15 = r18
            goto L_0x035b
        L_0x040b:
            r10 = move-exception
            r15 = r18
            goto L_0x02f1
        L_0x0410:
            r8 = r9
            goto L_0x01fd
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.sync.UrlWrapper.sendRequest(byte[]):com.alibaba.analytics.core.sync.BizResponse");
    }
}
