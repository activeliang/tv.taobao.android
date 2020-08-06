package android.taobao.windvane.connect;

import android.taobao.windvane.WVCookieManager;
import android.taobao.windvane.util.TaoLog;
import anet.channel.util.HttpConstant;
import java.net.HttpURLConnection;
import java.util.Map;

public class HttpConnector {
    public static final String CACHE_CONTROL = "cache-control";
    public static final String CONTENT_LENGTH = "content-length";
    public static final String CONTENT_TYPE = "content-type";
    public static final String DATE = "date";
    public static final String ETAG = "etag";
    public static final String EXPIRES = "expires";
    public static final String IF_MODIFY_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String LAST_MODIFIED = "last-modified";
    public static final String REDIRECT_LOCATION = "location";
    public static final String RESPONSE_CODE = "response-code";
    public static final String SET_COOKIE = "Set-Cookie";
    private static String TAG = "HttpConnector";
    public static final String URL = "url";
    private HttpConnectListener<HttpResponse> mListener = null;
    private int redirectTime = 0;

    static {
        System.setProperty("http.keepAlive", "false");
    }

    public HttpResponse syncConnect(String url) {
        return syncConnect(new HttpRequest(url), (HttpConnectListener<HttpResponse>) null);
    }

    public HttpResponse syncConnect(HttpRequest request) {
        return syncConnect(request, (HttpConnectListener<HttpResponse>) null);
    }

    public HttpResponse syncConnect(HttpRequest request, HttpConnectListener<HttpResponse> listener) {
        if (request == null) {
            throw new NullPointerException("Http connect error, request is null");
        }
        String errorMsg = null;
        this.mListener = listener;
        this.redirectTime = 0;
        int retryTime = request.getRetryTime();
        int i = 0;
        while (i < retryTime) {
            try {
                return dataConnect(request);
            } catch (NetWorkErrorException e) {
                e.printStackTrace();
                errorMsg = e.toString();
                try {
                    Thread.sleep((long) ((i + 1) * 2 * 1000));
                } catch (InterruptedException e2) {
                    TaoLog.e(TAG, "HttpConnector retry Sleep has been interrupted, go ahead");
                }
                i++;
            } catch (HttpOverFlowException e3) {
                e3.printStackTrace();
                errorMsg = e3.toString();
            } catch (RedirectException e4) {
                e4.printStackTrace();
                errorMsg = e4.toString();
            } catch (HttpsErrorException e5) {
                e5.printStackTrace();
                errorMsg = e5.toString();
            }
        }
        HttpResponse result = new HttpResponse();
        result.setErrorMsg(errorMsg);
        return result;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v15, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX WARNING: type inference failed for: r36v20, types: [java.net.URLConnection] */
    /* JADX WARNING: Code restructure failed: missing block: B:274:0x0505, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:275:0x0506, code lost:
        r13.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:276:0x050b, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:277:0x050c, code lost:
        r13.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:278:0x0511, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:279:0x0512, code lost:
        r13.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:280:0x0517, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:281:0x0518, code lost:
        r13.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x018a, code lost:
        r36 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r11.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r31.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        r18.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x01a1, code lost:
        r7.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0206, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0216, code lost:
        throw new android.taobao.windvane.connect.HttpConnector.HttpsErrorException(r40, r14.getMessage());
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:34:0x0175, B:80:0x0205] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x0329 A[Catch:{ Throwable -> 0x0206, all -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x033c A[Catch:{ Throwable -> 0x0206, all -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0341 A[SYNTHETIC, Splitter:B:165:0x0341] */
    /* JADX WARNING: Removed duplicated region for block: B:168:0x0346 A[SYNTHETIC, Splitter:B:168:0x0346] */
    /* JADX WARNING: Removed duplicated region for block: B:171:0x034b A[SYNTHETIC, Splitter:B:171:0x034b] */
    /* JADX WARNING: Removed duplicated region for block: B:174:0x0350 A[SYNTHETIC, Splitter:B:174:0x0350] */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x0355  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0360  */
    /* JADX WARNING: Removed duplicated region for block: B:191:0x03af A[Catch:{ Throwable -> 0x0206, all -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x03d7 A[Catch:{ Throwable -> 0x0206, all -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:244:0x04be  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x018d A[SYNTHETIC, Splitter:B:41:0x018d] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0192 A[SYNTHETIC, Splitter:B:44:0x0192] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0197 A[SYNTHETIC, Splitter:B:47:0x0197] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x019c A[SYNTHETIC, Splitter:B:50:0x019c] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01a1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x01b8 A[Catch:{ Throwable -> 0x0206, all -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01f1 A[Catch:{ Throwable -> 0x0206, all -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0205 A[SYNTHETIC, Splitter:B:80:0x0205] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.taobao.windvane.connect.HttpResponse dataConnect(android.taobao.windvane.connect.HttpRequest r41) throws android.taobao.windvane.connect.HttpConnector.NetWorkErrorException, android.taobao.windvane.connect.HttpConnector.HttpOverFlowException, android.taobao.windvane.connect.HttpConnector.RedirectException, android.taobao.windvane.connect.HttpConnector.HttpsErrorException {
        /*
            r40 = this;
            android.net.Uri r33 = r41.getUri()
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener
            r36 = r0
            if (r36 == 0) goto L_0x0015
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener
            r36 = r0
            r36.onStart()
        L_0x0015:
            android.taobao.windvane.connect.HttpResponse r29 = new android.taobao.windvane.connect.HttpResponse
            r29.<init>()
            java.lang.String r36 = "https"
            java.lang.String r37 = r33.getScheme()
            boolean r20 = r36.equalsIgnoreCase(r37)
            r7 = 0
            r18 = 0
            r31 = 0
            r11 = 0
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream
            r36 = 128(0x80, float:1.794E-43)
            r0 = r36
            r6.<init>(r0)
            java.net.URL r34 = new java.net.URL     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r36 = r33.toString()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r34
            r1 = r36
            r0.<init>(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r15 = r34.getHost()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r20 == 0) goto L_0x01c7
            java.lang.String r36 = TAG     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = "proxy or https"
            android.taobao.windvane.util.TaoLog.i(r36, r37)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r32 = 0
            android.app.Application r36 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            org.apache.http.HttpHost r26 = android.taobao.windvane.util.NetWork.getHttpsProxyInfo(r36)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r26 == 0) goto L_0x01a5
            android.taobao.windvane.security.SSLTunnelSocketFactory r32 = new android.taobao.windvane.security.SSLTunnelSocketFactory     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r36 = r26.getHostName()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            int r37 = r26.getPort()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r38 = 0
            java.lang.String r39 = "taobao_hybrid_8.0.0"
            r0 = r32
            r1 = r36
            r2 = r37
            r3 = r38
            r4 = r39
            r0.<init>(r1, r2, r3, r4)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x0075:
            java.net.URLConnection r16 = r34.openConnection()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            javax.net.ssl.HttpsURLConnection r16 = (javax.net.ssl.HttpsURLConnection) r16     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r32 == 0) goto L_0x0084
            r0 = r16
            r1 = r32
            r0.setSSLSocketFactory(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x0084:
            org.apache.http.conn.ssl.StrictHostnameVerifier r36 = new org.apache.http.conn.ssl.StrictHostnameVerifier     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36.<init>()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r16
            r1 = r36
            r0.setHostnameVerifier(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r7 = r16
            java.lang.String r36 = "Connection"
            java.lang.String r37 = "Keep-Alive"
            r0 = r36
            r1 = r37
            r7.setRequestProperty(r0, r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x009f:
            r0 = r40
            r1 = r41
            r0.setConnectProp(r7, r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            if (r36 == 0) goto L_0x00b9
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            r37 = 0
            r36.onProcess(r37)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x00b9:
            java.lang.String r36 = "post"
            java.lang.String r37 = r41.getMethod()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            boolean r36 = r36.equalsIgnoreCase(r37)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r36 == 0) goto L_0x01d2
            java.lang.String r36 = TAG     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.StringBuilder r37 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r37.<init>()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r38 = "post data: "
            java.lang.StringBuilder r37 = r37.append(r38)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r38 = new java.lang.String     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            byte[] r39 = r41.getPostData()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r38.<init>(r39)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.StringBuilder r37 = r37.append(r38)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = r37.toString()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            android.taobao.windvane.util.TaoLog.d(r36, r37)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = 1
            r0 = r36
            r7.setDoOutput(r0)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = 1
            r0 = r36
            r7.setDoInput(r0)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r36 = "POST"
            r0 = r36
            r7.setRequestMethod(r0)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r7.connect()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.io.OutputStream r24 = r7.getOutputStream()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            byte[] r36 = r41.getPostData()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r24
            r1 = r36
            r0.write(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r24.flush()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r24.close()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x0116:
            int r28 = r7.getResponseCode()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r36 = TAG     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.StringBuilder r37 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r37.<init>()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r38 = "responeCode:"
            java.lang.StringBuilder r37 = r37.append(r38)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r37
            r1 = r28
            java.lang.StringBuilder r37 = r0.append(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = r37.toString()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            android.taobao.windvane.util.TaoLog.d(r36, r37)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = 300(0x12c, float:4.2E-43)
            r0 = r28
            r1 = r36
            if (r0 < r1) goto L_0x02d3
            r36 = 400(0x190, float:5.6E-43)
            r0 = r28
            r1 = r36
            if (r0 >= r1) goto L_0x02d3
            r36 = 304(0x130, float:4.26E-43)
            r0 = r28
            r1 = r36
            if (r0 == r1) goto L_0x02d3
            boolean r36 = r41.isRedirect()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r36 == 0) goto L_0x02d3
            r0 = r40
            int r0 = r0.redirectTime     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            r37 = 5
            r0 = r36
            r1 = r37
            if (r0 <= r1) goto L_0x0217
            android.taobao.windvane.connect.HttpConnector$RedirectException r36 = new android.taobao.windvane.connect.HttpConnector$RedirectException     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = "too many redirect"
            r0 = r36
            r1 = r40
            r2 = r37
            r0.<init>(r2)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            throw r36     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x0172:
            r13 = move-exception
        L_0x0173:
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            if (r36 == 0) goto L_0x0189
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            r37 = -1
            java.lang.String r38 = "too many redirect"
            r36.onError(r37, r38)     // Catch:{ all -> 0x018a }
        L_0x0189:
            throw r13     // Catch:{ all -> 0x018a }
        L_0x018a:
            r36 = move-exception
        L_0x018b:
            if (r11 == 0) goto L_0x0190
            r11.close()     // Catch:{ Exception -> 0x0505 }
        L_0x0190:
            if (r31 == 0) goto L_0x0195
            r31.close()     // Catch:{ Exception -> 0x050b }
        L_0x0195:
            if (r18 == 0) goto L_0x019a
            r18.close()     // Catch:{ Exception -> 0x0511 }
        L_0x019a:
            if (r6 == 0) goto L_0x019f
            r6.close()     // Catch:{ Exception -> 0x0517 }
        L_0x019f:
            if (r7 == 0) goto L_0x01a4
            r7.disconnect()
        L_0x01a4:
            throw r36
        L_0x01a5:
            java.lang.String r36 = TAG     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = "https:proxy: none"
            android.taobao.windvane.util.TaoLog.d(r36, r37)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            goto L_0x0075
        L_0x01af:
            r13 = move-exception
        L_0x01b0:
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            if (r36 == 0) goto L_0x01c6
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            r37 = -2
            java.lang.String r38 = "connect file is too large"
            r36.onError(r37, r38)     // Catch:{ all -> 0x018a }
        L_0x01c6:
            throw r13     // Catch:{ all -> 0x018a }
        L_0x01c7:
            java.net.URLConnection r36 = r34.openConnection()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r36
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r7 = r0
            goto L_0x009f
        L_0x01d2:
            r7.connect()     // Catch:{ AssertionError -> 0x01d7 }
            goto L_0x0116
        L_0x01d7:
            r13 = move-exception
            android.taobao.windvane.connect.HttpConnector$NetWorkErrorException r36 = new android.taobao.windvane.connect.HttpConnector$NetWorkErrorException     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = r13.getMessage()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r36
            r1 = r40
            r2 = r37
            r0.<init>(r2)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            throw r36     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x01e8:
            r13 = move-exception
        L_0x01e9:
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            if (r36 == 0) goto L_0x01ff
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            r37 = -3
            java.lang.String r38 = "ssl handshake exception"
            r36.onError(r37, r38)     // Catch:{ all -> 0x018a }
        L_0x01ff:
            java.lang.Throwable r30 = r13.getCause()     // Catch:{ all -> 0x018a }
            if (r30 == 0) goto L_0x04be
            throw r30     // Catch:{ Throwable -> 0x0206 }
        L_0x0206:
            r14 = move-exception
            android.taobao.windvane.connect.HttpConnector$HttpsErrorException r36 = new android.taobao.windvane.connect.HttpConnector$HttpsErrorException     // Catch:{ all -> 0x018a }
            java.lang.String r37 = r14.getMessage()     // Catch:{ all -> 0x018a }
            r0 = r36
            r1 = r40
            r2 = r37
            r0.<init>(r2)     // Catch:{ all -> 0x018a }
            throw r36     // Catch:{ all -> 0x018a }
        L_0x0217:
            r0 = r40
            int r0 = r0.redirectTime     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            int r36 = r36 + 1
            r0 = r36
            r1 = r40
            r1.redirectTime = r0     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r36 = "location"
            r0 = r36
            java.lang.String r27 = r7.getHeaderField(r0)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r27 == 0) goto L_0x02d3
            java.lang.String r36 = r27.toLowerCase()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = "http"
            boolean r36 = r36.startsWith(r37)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r36 != 0) goto L_0x024f
            java.net.URL r36 = new java.net.URL     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = "http"
            r0 = r36
            r1 = r37
            r2 = r27
            r0.<init>(r1, r15, r2)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r27 = r36.toString()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x024f:
            r36 = 305(0x131, float:4.27E-43)
            r0 = r28
            r1 = r36
            if (r0 == r1) goto L_0x0294
            android.net.Uri r36 = android.net.Uri.parse(r27)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r41
            r1 = r36
            r0.setUri(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            android.taobao.windvane.connect.HttpResponse r29 = r40.dataConnect(r41)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r11 == 0) goto L_0x026b
            r11.close()     // Catch:{ Exception -> 0x0280 }
        L_0x026b:
            if (r31 == 0) goto L_0x0270
            r31.close()     // Catch:{ Exception -> 0x0285 }
        L_0x0270:
            if (r18 == 0) goto L_0x0275
            r18.close()     // Catch:{ Exception -> 0x028a }
        L_0x0275:
            if (r6 == 0) goto L_0x027a
            r6.close()     // Catch:{ Exception -> 0x028f }
        L_0x027a:
            if (r7 == 0) goto L_0x027f
            r7.disconnect()
        L_0x027f:
            return r29
        L_0x0280:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x026b
        L_0x0285:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0270
        L_0x028a:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0275
        L_0x028f:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x027a
        L_0x0294:
            android.taobao.windvane.connect.HttpRequest r36 = new android.taobao.windvane.connect.HttpRequest     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r36
            r1 = r27
            r0.<init>(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r40
            r1 = r36
            android.taobao.windvane.connect.HttpResponse r29 = r0.dataConnect(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r11 == 0) goto L_0x02aa
            r11.close()     // Catch:{ Exception -> 0x02bf }
        L_0x02aa:
            if (r31 == 0) goto L_0x02af
            r31.close()     // Catch:{ Exception -> 0x02c4 }
        L_0x02af:
            if (r18 == 0) goto L_0x02b4
            r18.close()     // Catch:{ Exception -> 0x02c9 }
        L_0x02b4:
            if (r6 == 0) goto L_0x02b9
            r6.close()     // Catch:{ Exception -> 0x02ce }
        L_0x02b9:
            if (r7 == 0) goto L_0x027f
            r7.disconnect()
            goto L_0x027f
        L_0x02bf:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x02aa
        L_0x02c4:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x02af
        L_0x02c9:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x02b4
        L_0x02ce:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x02b9
        L_0x02d3:
            r0 = r29
            r1 = r28
            r0.setHttpCode(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r21 = 1
        L_0x02dc:
            r0 = r21
            java.lang.String r22 = r7.getHeaderFieldKey(r0)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r22 != 0) goto L_0x0377
            r36 = 200(0xc8, float:2.8E-43)
            r0 = r28
            r1 = r36
            if (r0 < r1) goto L_0x0476
            r36 = 300(0x12c, float:4.2E-43)
            r0 = r28
            r1 = r36
            if (r0 >= r1) goto L_0x0476
            int r9 = r7.getContentLength()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = 5242880(0x500000, float:7.34684E-39)
            r0 = r36
            if (r9 <= r0) goto L_0x03e6
            android.taobao.windvane.connect.HttpConnector$HttpOverFlowException r36 = new android.taobao.windvane.connect.HttpConnector$HttpOverFlowException     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.StringBuilder r37 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r37.<init>()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r38 = "The Content-Length is too large:"
            java.lang.StringBuilder r37 = r37.append(r38)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r37
            java.lang.StringBuilder r37 = r0.append(r9)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r37 = r37.toString()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r36
            r1 = r40
            r2 = r37
            r0.<init>(r2)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            throw r36     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x0320:
            r13 = move-exception
        L_0x0321:
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            if (r36 == 0) goto L_0x0337
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            r37 = -5
            java.lang.String r38 = "out of memory error"
            r36.onError(r37, r38)     // Catch:{ all -> 0x018a }
        L_0x0337:
            r13.printStackTrace()     // Catch:{ all -> 0x018a }
            if (r6 == 0) goto L_0x033f
            r6.reset()     // Catch:{ all -> 0x018a }
        L_0x033f:
            if (r11 == 0) goto L_0x0344
            r11.close()     // Catch:{ Exception -> 0x04ed }
        L_0x0344:
            if (r31 == 0) goto L_0x0349
            r31.close()     // Catch:{ Exception -> 0x04f3 }
        L_0x0349:
            if (r18 == 0) goto L_0x034e
            r18.close()     // Catch:{ Exception -> 0x04f9 }
        L_0x034e:
            if (r6 == 0) goto L_0x0353
            r6.close()     // Catch:{ Exception -> 0x04ff }
        L_0x0353:
            if (r7 == 0) goto L_0x0358
            r7.disconnect()
        L_0x0358:
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener
            r36 = r0
            if (r36 == 0) goto L_0x0370
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener
            r36 = r0
            android.taobao.windvane.connect.HttpResponse r37 = new android.taobao.windvane.connect.HttpResponse
            r37.<init>()
            r38 = 0
            r36.onFinish(r37, r38)
        L_0x0370:
            android.taobao.windvane.connect.HttpResponse r29 = new android.taobao.windvane.connect.HttpResponse
            r29.<init>()
            goto L_0x027f
        L_0x0377:
            int r21 = r21 + 1
            r0 = r22
            java.lang.String r35 = r7.getHeaderField(r0)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r29
            r1 = r22
            r2 = r35
            r0.addHeader(r1, r2)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r36 = "Set-Cookie"
            r0 = r36
            r1 = r22
            boolean r36 = r0.equals(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r36 == 0) goto L_0x02dc
            java.lang.String r36 = r33.toString()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r36
            r1 = r35
            android.taobao.windvane.WVCookieManager.setCookie(r0, r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            goto L_0x02dc
        L_0x03a2:
            r13 = move-exception
        L_0x03a3:
            java.lang.String r23 = r13.getMessage()     // Catch:{ all -> 0x018a }
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            if (r36 == 0) goto L_0x03d2
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ all -> 0x018a }
            r36 = r0
            r37 = -4
            java.lang.StringBuilder r38 = new java.lang.StringBuilder     // Catch:{ all -> 0x018a }
            r38.<init>()     // Catch:{ all -> 0x018a }
            java.lang.String r39 = "network exception: "
            java.lang.StringBuilder r38 = r38.append(r39)     // Catch:{ all -> 0x018a }
            r0 = r38
            r1 = r23
            java.lang.StringBuilder r38 = r0.append(r1)     // Catch:{ all -> 0x018a }
            java.lang.String r38 = r38.toString()     // Catch:{ all -> 0x018a }
            r36.onError(r37, r38)     // Catch:{ all -> 0x018a }
        L_0x03d2:
            r13.printStackTrace()     // Catch:{ all -> 0x018a }
            if (r6 == 0) goto L_0x03da
            r6.reset()     // Catch:{ all -> 0x018a }
        L_0x03da:
            android.taobao.windvane.connect.HttpConnector$NetWorkErrorException r36 = new android.taobao.windvane.connect.HttpConnector$NetWorkErrorException     // Catch:{ all -> 0x018a }
            r0 = r36
            r1 = r40
            r2 = r23
            r0.<init>(r2)     // Catch:{ all -> 0x018a }
            throw r36     // Catch:{ all -> 0x018a }
        L_0x03e6:
            java.io.InputStream r31 = r7.getInputStream()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.lang.String r8 = r7.getContentEncoding()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r8 == 0) goto L_0x0462
            java.lang.String r36 = "gzip"
            r0 = r36
            boolean r36 = r0.equals(r8)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            if (r36 == 0) goto L_0x0462
            java.util.zip.GZIPInputStream r19 = new java.util.zip.GZIPInputStream     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r19
            r1 = r31
            r0.<init>(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            java.io.DataInputStream r12 = new java.io.DataInputStream     // Catch:{ RedirectException -> 0x0536, HttpOverFlowException -> 0x0531, SSLHandshakeException -> 0x052c, OutOfMemoryError -> 0x0527, Throwable -> 0x0522, all -> 0x051d }
            r0 = r19
            r12.<init>(r0)     // Catch:{ RedirectException -> 0x0536, HttpOverFlowException -> 0x0531, SSLHandshakeException -> 0x052c, OutOfMemoryError -> 0x0527, Throwable -> 0x0522, all -> 0x051d }
            r11 = r12
            r18 = r19
        L_0x040e:
            r25 = 0
            r10 = 0
            r17 = 0
            r36 = 2048(0x800, float:2.87E-42)
            r0 = r36
            byte[] r5 = new byte[r0]     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x0419:
            r36 = 0
            r37 = 2048(0x800, float:2.87E-42)
            r0 = r36
            r1 = r37
            int r17 = r11.read(r5, r0, r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = -1
            r0 = r17
            r1 = r36
            if (r0 == r1) goto L_0x046b
            r36 = 0
            r0 = r36
            r1 = r17
            r6.write(r5, r0, r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            if (r36 == 0) goto L_0x0419
            int r10 = r10 + r17
            if (r10 <= r9) goto L_0x0443
            r9 = r10
        L_0x0443:
            float r0 = (float) r10     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            float r0 = (float) r9     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r37 = r0
            float r36 = r36 / r37
            r37 = 1120403456(0x42c80000, float:100.0)
            float r36 = r36 * r37
            r0 = r36
            int r0 = (int) r0     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r25 = r0
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            r0 = r36
            r1 = r25
            r0.onProcess(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            goto L_0x0419
        L_0x0462:
            java.io.DataInputStream r12 = new java.io.DataInputStream     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r31
            r12.<init>(r0)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r11 = r12
            goto L_0x040e
        L_0x046b:
            byte[] r36 = r6.toByteArray()     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r0 = r29
            r1 = r36
            r0.setData(r1)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x0476:
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            if (r36 == 0) goto L_0x048f
            r0 = r40
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r0 = r0.mListener     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
            r36 = r0
            r37 = 0
            r0 = r36
            r1 = r29
            r2 = r37
            r0.onFinish(r1, r2)     // Catch:{ RedirectException -> 0x0172, HttpOverFlowException -> 0x01af, SSLHandshakeException -> 0x01e8, OutOfMemoryError -> 0x0320, Throwable -> 0x03a2 }
        L_0x048f:
            if (r11 == 0) goto L_0x0494
            r11.close()     // Catch:{ Exception -> 0x04aa }
        L_0x0494:
            if (r31 == 0) goto L_0x0499
            r31.close()     // Catch:{ Exception -> 0x04af }
        L_0x0499:
            if (r18 == 0) goto L_0x049e
            r18.close()     // Catch:{ Exception -> 0x04b4 }
        L_0x049e:
            if (r6 == 0) goto L_0x04a3
            r6.close()     // Catch:{ Exception -> 0x04b9 }
        L_0x04a3:
            if (r7 == 0) goto L_0x027f
            r7.disconnect()
            goto L_0x027f
        L_0x04aa:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0494
        L_0x04af:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0499
        L_0x04b4:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x049e
        L_0x04b9:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x04a3
        L_0x04be:
            if (r11 == 0) goto L_0x04c3
            r11.close()     // Catch:{ Exception -> 0x04d9 }
        L_0x04c3:
            if (r31 == 0) goto L_0x04c8
            r31.close()     // Catch:{ Exception -> 0x04de }
        L_0x04c8:
            if (r18 == 0) goto L_0x04cd
            r18.close()     // Catch:{ Exception -> 0x04e3 }
        L_0x04cd:
            if (r6 == 0) goto L_0x04d2
            r6.close()     // Catch:{ Exception -> 0x04e8 }
        L_0x04d2:
            if (r7 == 0) goto L_0x0358
            r7.disconnect()
            goto L_0x0358
        L_0x04d9:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x04c3
        L_0x04de:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x04c8
        L_0x04e3:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x04cd
        L_0x04e8:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x04d2
        L_0x04ed:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0344
        L_0x04f3:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0349
        L_0x04f9:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x034e
        L_0x04ff:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0353
        L_0x0505:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0190
        L_0x050b:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0195
        L_0x0511:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x019a
        L_0x0517:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x019f
        L_0x051d:
            r36 = move-exception
            r18 = r19
            goto L_0x018b
        L_0x0522:
            r13 = move-exception
            r18 = r19
            goto L_0x03a3
        L_0x0527:
            r13 = move-exception
            r18 = r19
            goto L_0x0321
        L_0x052c:
            r13 = move-exception
            r18 = r19
            goto L_0x01e9
        L_0x0531:
            r13 = move-exception
            r18 = r19
            goto L_0x01b0
        L_0x0536:
            r13 = move-exception
            r18 = r19
            goto L_0x0173
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.connect.HttpConnector.dataConnect(android.taobao.windvane.connect.HttpRequest):android.taobao.windvane.connect.HttpResponse");
    }

    private void setConnectProp(HttpURLConnection conn, HttpRequest request) {
        int retryTime = request.getRetryTime();
        conn.setConnectTimeout(request.getConnectTimeout() * (retryTime + 1));
        conn.setReadTimeout(request.getReadTimeout() * (retryTime + 1));
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("Host", request.getUri().getHost());
        conn.setRequestProperty("Connection", "close");
        conn.setRequestProperty(HttpConstant.ACCEPT_ENCODING, "gzip");
        String cookieStr = WVCookieManager.getCookie(conn.getURL().toString());
        if (cookieStr != null) {
            conn.setRequestProperty(HttpConstant.COOKIE, cookieStr);
        }
        Map<String, String> headers = request.getHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        conn.setUseCaches(false);
    }

    class HttpsErrorException extends Exception {
        public HttpsErrorException(String message) {
            super(message);
        }
    }

    class NetWorkErrorException extends Exception {
        public NetWorkErrorException(String message) {
            super(message);
        }
    }

    class HttpOverFlowException extends Exception {
        public HttpOverFlowException(String message) {
            super(message);
        }
    }

    class RedirectException extends Exception {
        public RedirectException(String message) {
            super(message);
        }
    }
}
