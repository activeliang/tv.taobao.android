package anet.channel.session;

import android.os.Build;
import anet.channel.RequestCb;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.bytes.ByteArrayPool;
import anet.channel.request.BodyEntry;
import anet.channel.request.Request;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.SchemeGuesser;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.utils.ByteCounterInputStream;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.HttpSslUtil;
import anet.channel.util.HttpUrl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.conn.ConnectTimeoutException;

public class HttpConnector {
    private static final String TAG = "awcn.HttpConnector";

    public static class Response {
        public int contentLength;
        public Map<String, List<String>> header;
        public int httpCode;
        public boolean isGZip;
        public byte[] out;
    }

    private HttpConnector() {
    }

    public static Response connect(Request request) {
        return connectImpl(request, (RequestCb) null);
    }

    public static void connect(Request request, RequestCb requestCb) {
        connectImpl(request, requestCb);
    }

    private static Response connectImpl(Request request, RequestCb requestCb) {
        String location;
        Response response = new Response();
        if (request != null && request.getUrl() != null) {
            HttpURLConnection conn = null;
            while (NetworkStatusHelper.isConnected()) {
                try {
                    if (ALog.isPrintLog(2)) {
                        ALog.i(TAG, "", request.getSeq(), "request URL", request.getUrl().toString());
                        ALog.i(TAG, "", request.getSeq(), "request Method", request.getMethod());
                        ALog.i(TAG, "", request.getSeq(), "request headers", request.getHeaders());
                    }
                    conn = getConnection(request);
                    if (conn != null) {
                        request.rs.sendStart = System.currentTimeMillis();
                        request.rs.processTime = request.rs.sendStart - request.rs.start;
                        conn.connect();
                        postData(conn, request);
                        request.rs.sendEnd = System.currentTimeMillis();
                        request.rs.sendDataTime = request.rs.sendEnd - request.rs.sendStart;
                        response.httpCode = conn.getResponseCode();
                        response.header = HttpHelper.cloneMap(conn.getHeaderFields());
                        ALog.i(TAG, "", request.getSeq(), "response code", Integer.valueOf(response.httpCode));
                        ALog.i(TAG, "", request.getSeq(), "response headers", response.header);
                        if (HttpHelper.checkRedirect(request, response.httpCode) && (location = HttpHelper.getSingleHeaderFieldByKey(response.header, "Location")) != null) {
                            HttpUrl httpUrl = HttpUrl.parse(location);
                            if (httpUrl != null) {
                                if (httpUrl.scheme() == null) {
                                    httpUrl.setScheme(StrategyCenter.getInstance().getSchemeByHost(httpUrl.host(), (String) null));
                                }
                                request = request.newBuilder().setMethod("GET").setBody((BodyEntry) null).setUrl(httpUrl).setRedirectTimes(request.getRedirectTimes() + 1).setSslSocketFactory((SSLSocketFactory) null).setHostnameVerifier((HostnameVerifier) null).build();
                                request.rs.url = httpUrl.simpleUrlString();
                                if (conn != null) {
                                    try {
                                        conn.disconnect();
                                    } catch (Exception e) {
                                        ALog.e(TAG, "http disconnect", (String) null, e, new Object[0]);
                                    }
                                }
                            } else {
                                ALog.e(TAG, "redirect url is invalid!", request.getSeq(), "redirect url", location);
                            }
                        }
                        request.rs.contentEncoding = HttpHelper.getSingleHeaderFieldByKey(response.header, "Content-Encoding");
                        if (!Request.Method.HEAD.equals(request.getMethod()) && response.httpCode != 304 && response.httpCode != 204 && (response.httpCode < 100 || response.httpCode >= 200)) {
                            response.contentLength = HttpHelper.parseContentLength(response.header);
                            response.isGZip = HttpHelper.checkContentEncodingGZip(response.header);
                            if (response.isGZip) {
                                response.header.remove("Content-Encoding");
                                response.header.remove("Content-Length");
                            }
                            if (requestCb != null) {
                                requestCb.onResponseCode(response.httpCode, response.header);
                            }
                            parseBody(conn, request, response, requestCb);
                        } else if (requestCb != null) {
                            requestCb.onResponseCode(response.httpCode, response.header);
                        }
                        request.rs.oneWayTime = System.currentTimeMillis() - request.rs.start;
                        request.rs.statusCode = response.httpCode;
                        request.rs.ret = 1;
                        if (requestCb != null) {
                            requestCb.onFinish(response.httpCode, "SUCCESS", request.rs);
                        }
                    } else {
                        onException(request, response, requestCb, ErrorConstant.ERROR_OPEN_CONNECTION_NULL, (Throwable) null);
                    }
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e2) {
                            ALog.e(TAG, "http disconnect", (String) null, e2, new Object[0]);
                        }
                    }
                } catch (UnknownHostException e3) {
                    onException(request, response, requestCb, ErrorConstant.ERROR_UNKNOWN_HOST_EXCEPTION, e3);
                    ALog.e(TAG, "Unknown Host Exception", request.getSeq(), "host", request.getHost(), e3);
                    NetworkStatusHelper.printNetworkDetail();
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e4) {
                            ALog.e(TAG, "http disconnect", (String) null, e4, new Object[0]);
                        }
                    }
                } catch (SocketTimeoutException e5) {
                    onException(request, response, requestCb, ErrorConstant.ERROR_SOCKET_TIME_OUT, e5);
                    ALog.e(TAG, "HTTP Socket Timeout", request.getSeq(), e5, new Object[0]);
                    NetworkStatusHelper.printNetworkDetail();
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e6) {
                            ALog.e(TAG, "http disconnect", (String) null, e6, new Object[0]);
                        }
                    }
                } catch (ConnectTimeoutException e7) {
                    onException(request, response, requestCb, -400, e7);
                    ALog.e(TAG, "HTTP Connect Timeout", request.getSeq(), e7, new Object[0]);
                    NetworkStatusHelper.printNetworkDetail();
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e8) {
                            ALog.e(TAG, "http disconnect", (String) null, e8, new Object[0]);
                        }
                    }
                } catch (ConnectException e9) {
                    onException(request, response, requestCb, ErrorConstant.ERROR_CONNECT_EXCEPTION, e9);
                    ALog.e(TAG, "HTTP Connect Exception", request.getSeq(), e9, new Object[0]);
                    NetworkStatusHelper.printNetworkDetail();
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e10) {
                            ALog.e(TAG, "http disconnect", (String) null, e10, new Object[0]);
                        }
                    }
                } catch (SSLHandshakeException e11) {
                    SchemeGuesser.getInstance().onSslFail(request.getHost());
                    onException(request, response, requestCb, ErrorConstant.ERROR_SSL_ERROR, e11);
                    ALog.e(TAG, "HTTP Connect SSLHandshakeException", request.getSeq(), "host", request.getHost(), e11);
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e12) {
                            ALog.e(TAG, "http disconnect", (String) null, e12, new Object[0]);
                        }
                    }
                } catch (SSLException e13) {
                    SchemeGuesser.getInstance().onSslFail(request.getHost());
                    onException(request, response, requestCb, ErrorConstant.ERROR_SSL_ERROR, e13);
                    ALog.e(TAG, "connect SSLException", request.getSeq(), "host", request.getHost(), e13);
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e14) {
                            ALog.e(TAG, "http disconnect", (String) null, e14, new Object[0]);
                        }
                    }
                } catch (CancellationException e15) {
                    onException(request, response, requestCb, ErrorConstant.ERROR_REQUEST_CANCEL, e15);
                    ALog.e(TAG, "HTTP Request Cancel", request.getSeq(), e15, new Object[0]);
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e16) {
                            ALog.e(TAG, "http disconnect", (String) null, e16, new Object[0]);
                        }
                    }
                } catch (Exception e17) {
                    String s = e17.getMessage();
                    if (s == null || !s.contains("not verified")) {
                        onException(request, response, requestCb, -101, e17);
                    } else {
                        SchemeGuesser.getInstance().onSslFail(request.getHost());
                        onException(request, response, requestCb, ErrorConstant.ERROR_HOST_NOT_VERIFY_ERROR, e17);
                    }
                    ALog.e(TAG, "HTTP Connect Exception", request.getSeq(), e17, new Object[0]);
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e18) {
                            ALog.e(TAG, "http disconnect", (String) null, e18, new Object[0]);
                        }
                    }
                } catch (Throwable th) {
                    if (conn != null) {
                        try {
                            conn.disconnect();
                        } catch (Exception e19) {
                            ALog.e(TAG, "http disconnect", (String) null, e19, new Object[0]);
                        }
                    }
                    throw th;
                }
            }
            onException(request, response, requestCb, ErrorConstant.ERROR_NO_NETWORK, (Throwable) null);
        } else if (requestCb != null) {
            requestCb.onFinish(-102, ErrorConstant.getErrMsg(-102), new RequestStatistic((String) null, (String) null));
        }
        return response;
    }

    private static void onException(Request request, Response response, RequestCb requestCb, int errCode, Throwable e) {
        String errMsg = ErrorConstant.getErrMsg(errCode);
        ALog.e(TAG, "onException", request.getSeq(), "errorCode", Integer.valueOf(errCode), "errMsg", errMsg, "url", request.getUrlString(), "host", request.getHost());
        if (response != null) {
            response.httpCode = errCode;
        }
        request.rs.statusCode = errCode;
        request.rs.oneWayTime = System.currentTimeMillis() - request.rs.start;
        if (requestCb != null) {
            requestCb.onFinish(errCode, errMsg, request.rs);
        }
        if (errCode != -204) {
            AppMonitor.getInstance().commitStat(new ExceptionStatistic(errCode, errMsg, request.rs, e));
        }
    }

    /* JADX WARNING: type inference failed for: r11v3, types: [java.net.URLConnection] */
    /* JADX WARNING: type inference failed for: r11v23, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.net.HttpURLConnection getConnection(anet.channel.request.Request r15) throws java.io.IOException {
        /*
            android.util.Pair r8 = anet.channel.status.NetworkStatusHelper.getWifiProxy()
            r7 = 0
            if (r8 == 0) goto L_0x001f
            java.net.Proxy r7 = new java.net.Proxy
            java.net.Proxy$Type r13 = java.net.Proxy.Type.HTTP
            java.net.InetSocketAddress r14 = new java.net.InetSocketAddress
            java.lang.Object r11 = r8.first
            java.lang.String r11 = (java.lang.String) r11
            java.lang.Object r12 = r8.second
            java.lang.Integer r12 = (java.lang.Integer) r12
            int r12 = r12.intValue()
            r14.<init>(r11, r12)
            r7.<init>(r13, r14)
        L_0x001f:
            r9 = 0
            anet.channel.status.NetworkStatusHelper$NetworkStatus r11 = anet.channel.status.NetworkStatusHelper.getStatus()
            boolean r11 = r11.isMobile()
            if (r11 == 0) goto L_0x0032
            anet.channel.util.ProxySetting r9 = anet.channel.GlobalAppRuntimeInfo.getProxySetting()
            if (r9 == 0) goto L_0x0032
            java.net.Proxy r7 = r9.proxy
        L_0x0032:
            r1 = 0
            java.net.URL r10 = r15.getUrl()     // Catch:{ Exception -> 0x0088 }
            if (r7 == 0) goto L_0x008a
            java.net.URLConnection r11 = r10.openConnection(r7)     // Catch:{ Exception -> 0x0088 }
            r0 = r11
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0088 }
            r1 = r0
        L_0x0041:
            int r11 = r15.getConnectTimeout()     // Catch:{ Exception -> 0x0088 }
            r1.setConnectTimeout(r11)     // Catch:{ Exception -> 0x0088 }
            int r11 = r15.getReadTimeout()     // Catch:{ Exception -> 0x0088 }
            r1.setReadTimeout(r11)     // Catch:{ Exception -> 0x0088 }
            java.lang.String r6 = r15.getMethod()     // Catch:{ Exception -> 0x0088 }
            r1.setRequestMethod(r6)     // Catch:{ Exception -> 0x0088 }
            boolean r11 = r15.containsBody()     // Catch:{ Exception -> 0x0088 }
            if (r11 == 0) goto L_0x0060
            r11 = 1
            r1.setDoOutput(r11)     // Catch:{ Exception -> 0x0088 }
        L_0x0060:
            java.util.Map r3 = r15.getHeaders()     // Catch:{ Exception -> 0x0088 }
            java.util.Set r11 = r3.entrySet()     // Catch:{ Exception -> 0x0088 }
            java.util.Iterator r5 = r11.iterator()     // Catch:{ Exception -> 0x0088 }
        L_0x006c:
            boolean r11 = r5.hasNext()     // Catch:{ Exception -> 0x0088 }
            if (r11 == 0) goto L_0x0093
            java.lang.Object r2 = r5.next()     // Catch:{ Exception -> 0x0088 }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ Exception -> 0x0088 }
            java.lang.Object r11 = r2.getKey()     // Catch:{ Exception -> 0x0088 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Exception -> 0x0088 }
            java.lang.Object r12 = r2.getValue()     // Catch:{ Exception -> 0x0088 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ Exception -> 0x0088 }
            r1.addRequestProperty(r11, r12)     // Catch:{ Exception -> 0x0088 }
            goto L_0x006c
        L_0x0088:
            r11 = move-exception
        L_0x0089:
            return r1
        L_0x008a:
            java.net.URLConnection r11 = r10.openConnection()     // Catch:{ Exception -> 0x0088 }
            r0 = r11
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0088 }
            r1 = r0
            goto L_0x0041
        L_0x0093:
            java.lang.String r11 = "Host"
            java.lang.Object r4 = r3.get(r11)     // Catch:{ Exception -> 0x0088 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Exception -> 0x0088 }
            if (r4 != 0) goto L_0x00a2
            java.lang.String r4 = r15.getHost()     // Catch:{ Exception -> 0x0088 }
        L_0x00a2:
            java.lang.String r11 = "Host"
            r1.setRequestProperty(r11, r4)     // Catch:{ Exception -> 0x0088 }
            java.lang.String r11 = anet.channel.status.NetworkStatusHelper.getApn()     // Catch:{ Exception -> 0x0088 }
            java.lang.String r12 = "cmwap"
            boolean r11 = r11.equals(r12)     // Catch:{ Exception -> 0x0088 }
            if (r11 == 0) goto L_0x00bb
            java.lang.String r11 = "x-online-host"
            r1.setRequestProperty(r11, r4)     // Catch:{ Exception -> 0x0088 }
        L_0x00bb:
            java.lang.String r11 = "Accept-Encoding"
            boolean r11 = r3.containsKey(r11)     // Catch:{ Exception -> 0x0088 }
            if (r11 != 0) goto L_0x00cd
            java.lang.String r11 = "Accept-Encoding"
            java.lang.String r12 = "gzip"
            r1.addRequestProperty(r11, r12)     // Catch:{ Exception -> 0x0088 }
        L_0x00cd:
            java.lang.String r11 = r10.getProtocol()     // Catch:{ Exception -> 0x0088 }
            java.lang.String r12 = "https"
            boolean r11 = r11.equalsIgnoreCase(r12)     // Catch:{ Exception -> 0x0088 }
            if (r11 == 0) goto L_0x00dd
            supportHttps(r1, r15, r4)     // Catch:{ Exception -> 0x0088 }
        L_0x00dd:
            r11 = 0
            r1.setInstanceFollowRedirects(r11)     // Catch:{ Exception -> 0x0088 }
            goto L_0x0089
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.session.HttpConnector.getConnection(anet.channel.request.Request):java.net.HttpURLConnection");
    }

    private static void supportHttps(HttpURLConnection conn, Request request, final String host) {
        if (Integer.parseInt(Build.VERSION.SDK) < 8) {
            ALog.e(TAG, "supportHttps", "[supportHttps]Froyo 以下版本不支持https", new Object[0]);
            return;
        }
        HttpsURLConnection connection = (HttpsURLConnection) conn;
        if (request.getSslSocketFactory() != null) {
            connection.setSSLSocketFactory(request.getSslSocketFactory());
        } else if (HttpSslUtil.getSSLSocketFactory() != null) {
            connection.setSSLSocketFactory(HttpSslUtil.getSSLSocketFactory());
        }
        if (request.getHostnameVerifier() != null) {
            connection.setHostnameVerifier(request.getHostnameVerifier());
        } else if (HttpSslUtil.getHostnameVerifier() != null) {
            connection.setHostnameVerifier(HttpSslUtil.getHostnameVerifier());
        } else {
            connection.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslSession) {
                    return HttpsURLConnection.getDefaultHostnameVerifier().verify(host, sslSession);
                }
            });
        }
    }

    private static int postData(HttpURLConnection conn, Request request) {
        int cnt = 0;
        if (request.containsBody()) {
            OutputStream os = null;
            try {
                os = conn.getOutputStream();
                cnt = request.postBody(os);
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        ALog.e(TAG, "postData", request.getSeq(), e, new Object[0]);
                    }
                }
            } catch (Exception e2) {
                ALog.e(TAG, "postData error", request.getSeq(), e2, new Object[0]);
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e3) {
                        ALog.e(TAG, "postData", request.getSeq(), e3, new Object[0]);
                    }
                }
            } catch (Throwable th) {
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e4) {
                        ALog.e(TAG, "postData", request.getSeq(), e4, new Object[0]);
                    }
                }
                throw th;
            }
            request.rs.reqBodyInflateSize = (long) cnt;
            request.rs.reqBodyDeflateSize = (long) cnt;
            request.rs.sendDataSize = (long) cnt;
        }
        return cnt;
    }

    private static void parseBody(HttpURLConnection conn, Request request, Response response, RequestCb requestCb) throws IOException, CancellationException {
        InputStream is;
        InputStream is2;
        InputStream is3 = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            try {
                is3 = conn.getErrorStream();
            } catch (Exception e2) {
                ALog.e(TAG, "get error stream failed." + conn.getURL().toString(), request.getSeq(), e2, new Object[0]);
            }
            ALog.w(TAG, conn.getURL().toString(), (String) null, e, new Object[0]);
            is = is3;
        }
        if (is == null) {
            onException(request, response, requestCb, ErrorConstant.ERROR_IO_EXCEPTION, (Throwable) null);
            InputStream inputStream = is;
            return;
        }
        ByteArrayOutputStream bos = null;
        if (requestCb == null) {
            bos = new ByteArrayOutputStream(response.contentLength <= 0 ? 1024 : response.isGZip ? response.contentLength * 2 : response.contentLength);
        }
        try {
            ByteCounterInputStream bis = new ByteCounterInputStream(is);
            if (response.isGZip) {
                is2 = new GZIPInputStream(bis);
            } else {
                is2 = bis;
            }
            int readCount = 0;
            ByteArray buffer = null;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (buffer == null) {
                        buffer = ByteArrayPool.getInstance().retrieve(2048);
                    }
                    int readLen = buffer.readFrom(is2);
                    if (readLen != -1) {
                        if (readCount == 0) {
                            request.rs.rspStart = System.currentTimeMillis();
                        }
                        if (bos != null) {
                            buffer.writeTo(bos);
                        } else {
                            requestCb.onDataReceive(buffer, false);
                            buffer = null;
                        }
                        readCount += readLen;
                    } else {
                        if (bos != null) {
                            buffer.recycle();
                        } else {
                            requestCb.onDataReceive(buffer, true);
                        }
                        if (request.rs.rspStart == 0) {
                            request.rs.rspStart = System.currentTimeMillis();
                        }
                        request.rs.firstDataTime = request.rs.rspStart - request.rs.sendEnd;
                        request.rs.recDataTime = System.currentTimeMillis() - request.rs.rspStart;
                        request.rs.recDataSize = (long) readCount;
                        request.rs.rspBodyDeflateSize = bis.getReadByteCount();
                        request.rs.rspBodyInflateSize = (long) readCount;
                        if (bos != null) {
                            response.out = bos.toByteArray();
                        }
                        if (is2 != null) {
                            try {
                                is2.close();
                                return;
                            } catch (IOException e3) {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                }
            }
            throw new CancellationException("task cancelled");
        } catch (Throwable th2) {
            th = th2;
            is2 = is;
            if (is2 != null) {
                try {
                    is2.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }
}
