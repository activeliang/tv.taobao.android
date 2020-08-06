package com.yunos.tvtaobao.biz.request.core;

import com.zhiping.dev.android.logger.ZpLogger;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class ApacheHttpClient {
    private static final String CHARSET = "UTF-8";
    public static final int MAX_ROUTE_CONNECTIONS = 400;
    public static final int MAX_TOTAL_CONNECTIONS = 800;
    private static final String TAG = "ApacheHttpClient";
    private static HttpClient client = initHttpClient();

    public enum HTTP_REQUEST_ERROR {
        HTTP_NONE_ERROR,
        HTTP_SSL_ERROR,
        HTTP_CONNECT_ERROR,
        HTTP_SOCKET_ERROR,
        HTTP_SOCKET_TIMEOUT_ERROR,
        HTTP_CONNECT_TIMEOUT_ERROR,
        HTTP_UNKNOWN_HOST_ERROR,
        HTTP_UNSUPPORTED_ENCODING,
        HTTP_OTHER_ERROR
    }

    private static synchronized HttpClient initHttpClient() {
        DefaultHttpClient defaultHttpClient;
        synchronized (ApacheHttpClient.class) {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            HttpProtocolParams.setUseExpectContinue(params, true);
            ConnManagerParams.setMaxTotalConnections(params, 800);
            ConnManagerParams.setTimeout(params, 15000);
            ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(400));
            HttpConnectionParams.setConnectionTimeout(params, 15000);
            HttpConnectionParams.setSoTimeout(params, 15000);
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load((InputStream) null, (char[]) null);
                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                schReg.register(new Scheme("https", sf, 443));
            } catch (Exception e) {
                e.printStackTrace();
            }
            defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schReg), params);
        }
        return defaultHttpClient;
    }

    public static void shutdown() {
        if (client != null && client.getConnectionManager() != null) {
            client.getConnectionManager().shutdown();
        }
    }

    public static void onStartHttpClient() {
        client = null;
        client = initHttpClient();
    }

    public static String executeHttpGet(String host, String url, String encode) {
        return executeHttpGet(host, url, encode, true);
    }

    public static String executeHttpPost(String host, String url, String encode, List<? extends NameValuePair> parameters) {
        return executeHttpPost(host, url, encode, parameters, true);
    }

    /* JADX WARNING: type inference failed for: r21v6, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x01ed A[SYNTHETIC, Splitter:B:63:0x01ed] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01f2 A[Catch:{ IOException -> 0x023b }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x01f7 A[Catch:{ IOException -> 0x023b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String executeHttpPost(java.lang.String r25, java.lang.String r26, java.lang.String r27, java.util.List<? extends org.apache.http.NameValuePair> r28, boolean r29) {
        /*
            if (r26 != 0) goto L_0x000b
            java.lang.RuntimeException r21 = new java.lang.RuntimeException
            java.lang.String r22 = "url is null"
            r21.<init>(r22)
            throw r21
        L_0x000b:
            java.lang.String r21 = "HttpClient-request-post"
            r0 = r21
            r1 = r26
            com.zhiping.dev.android.logger.ZpLogger.i(r0, r1)
            r11 = 0
            r16 = 0
            r18 = 0
            r6 = 0
            java.net.URL r20 = new java.net.URL     // Catch:{ Exception -> 0x00a3 }
            r0 = r20
            r1 = r26
            r0.<init>(r1)     // Catch:{ Exception -> 0x00a3 }
            java.net.URLConnection r21 = r20.openConnection()     // Catch:{ Exception -> 0x00a3 }
            r0 = r21
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x00a3 }
            r6 = r0
            r21 = 10000(0x2710, float:1.4013E-41)
            r0 = r21
            r6.setReadTimeout(r0)     // Catch:{ Exception -> 0x00a3 }
            r21 = 10000(0x2710, float:1.4013E-41)
            r0 = r21
            r6.setConnectTimeout(r0)     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r21 = "POST"
            r0 = r21
            r6.setRequestMethod(r0)     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r21 = "Connection"
            java.lang.String r22 = "keep-alive"
            r0 = r21
            r1 = r22
            r6.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x00a3 }
            r21 = 1
            r0 = r21
            r6.setDoOutput(r0)     // Catch:{ Exception -> 0x00a3 }
            r21 = 1
            r0 = r21
            r6.setDoInput(r0)     // Catch:{ Exception -> 0x00a3 }
            boolean r21 = android.text.TextUtils.isEmpty(r25)     // Catch:{ Exception -> 0x00a3 }
            if (r21 != 0) goto L_0x006e
            java.lang.String r21 = "host"
            r0 = r21
            r1 = r25
            r6.addRequestProperty(r0, r1)     // Catch:{ Exception -> 0x00a3 }
        L_0x006e:
            java.lang.StringBuffer r7 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x00a3 }
            r7.<init>()     // Catch:{ Exception -> 0x00a3 }
            java.util.Iterator r21 = r28.iterator()     // Catch:{ Exception -> 0x00a3 }
        L_0x0077:
            boolean r22 = r21.hasNext()     // Catch:{ Exception -> 0x00a3 }
            if (r22 == 0) goto L_0x00fb
            java.lang.Object r15 = r21.next()     // Catch:{ Exception -> 0x00a3 }
            org.apache.http.NameValuePair r15 = (org.apache.http.NameValuePair) r15     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r22 = r15.getName()     // Catch:{ Exception -> 0x00a3 }
            r0 = r22
            java.lang.StringBuffer r22 = r7.append(r0)     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r23 = "="
            java.lang.StringBuffer r22 = r22.append(r23)     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r23 = r15.getValue()     // Catch:{ Exception -> 0x00a3 }
            java.lang.StringBuffer r22 = r22.append(r23)     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r23 = "&"
            r22.append(r23)     // Catch:{ Exception -> 0x00a3 }
            goto L_0x0077
        L_0x00a3:
            r8 = move-exception
        L_0x00a4:
            com.yunos.tvtaobao.biz.request.core.ApacheHttpClient$HTTP_REQUEST_ERROR r10 = getRequestException(r8)     // Catch:{ all -> 0x0259 }
            java.lang.String r21 = "ApacheHttpClient"
            java.lang.StringBuilder r22 = new java.lang.StringBuilder     // Catch:{ all -> 0x0259 }
            r22.<init>()     // Catch:{ all -> 0x0259 }
            java.lang.String r23 = "executeHttpPost error="
            java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ all -> 0x0259 }
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r10)     // Catch:{ all -> 0x0259 }
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r8)     // Catch:{ all -> 0x0259 }
            java.lang.String r23 = " isRetry="
            java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ all -> 0x0259 }
            r0 = r22
            r1 = r29
            java.lang.StringBuilder r22 = r0.append(r1)     // Catch:{ all -> 0x0259 }
            java.lang.String r22 = r22.toString()     // Catch:{ all -> 0x0259 }
            com.zhiping.dev.android.logger.ZpLogger.e(r21, r22)     // Catch:{ all -> 0x0259 }
            if (r29 == 0) goto L_0x00eb
            r21 = 0
            r0 = r25
            r1 = r26
            r2 = r27
            r3 = r28
            r4 = r21
            java.lang.String r18 = executeHttpPost(r0, r1, r2, r3, r4)     // Catch:{ all -> 0x0259 }
        L_0x00eb:
            if (r6 == 0) goto L_0x00f0
            r6.disconnect()     // Catch:{ IOException -> 0x021c }
        L_0x00f0:
            if (r11 == 0) goto L_0x00f5
            r11.close()     // Catch:{ IOException -> 0x021c }
        L_0x00f5:
            if (r16 == 0) goto L_0x00fa
            r16.close()     // Catch:{ IOException -> 0x021c }
        L_0x00fa:
            return r18
        L_0x00fb:
            java.lang.String r21 = "HttpClient-request-post"
            java.lang.StringBuilder r22 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a3 }
            r22.<init>()     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r23 = "params="
            java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x00a3 }
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r7)     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r22 = r22.toString()     // Catch:{ Exception -> 0x00a3 }
            com.zhiping.dev.android.logger.ZpLogger.i(r21, r22)     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r21 = r7.toString()     // Catch:{ Exception -> 0x00a3 }
            byte[] r5 = r21.getBytes()     // Catch:{ Exception -> 0x00a3 }
            java.io.OutputStream r14 = r6.getOutputStream()     // Catch:{ Exception -> 0x00a3 }
            r14.write(r5)     // Catch:{ Exception -> 0x00a3 }
            r14.flush()     // Catch:{ Exception -> 0x00a3 }
            java.io.InputStream r11 = r6.getInputStream()     // Catch:{ Exception -> 0x00a3 }
            java.lang.String r9 = r6.getContentEncoding()     // Catch:{ Exception -> 0x00a3 }
            if (r9 == 0) goto L_0x0144
            java.lang.String r21 = "gzip"
            r0 = r21
            boolean r21 = r9.contains(r0)     // Catch:{ Exception -> 0x00a3 }
            if (r21 == 0) goto L_0x0144
            java.util.zip.GZIPInputStream r12 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x00a3 }
            r12.<init>(r11)     // Catch:{ Exception -> 0x00a3 }
            r11 = r12
        L_0x0144:
            java.io.BufferedReader r17 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00a3 }
            java.io.InputStreamReader r21 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00a3 }
            r0 = r21
            r1 = r27
            r0.<init>(r11, r1)     // Catch:{ Exception -> 0x00a3 }
            r0 = r17
            r1 = r21
            r0.<init>(r1)     // Catch:{ Exception -> 0x00a3 }
            int r21 = r6.getResponseCode()     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            r22 = 200(0xc8, float:2.8E-43)
            r0 = r21
            r1 = r22
            if (r0 != r1) goto L_0x017c
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            r19.<init>()     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
        L_0x0167:
            java.lang.String r13 = r17.readLine()     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            if (r13 == 0) goto L_0x0178
            r0 = r19
            r0.append(r13)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            goto L_0x0167
        L_0x0173:
            r8 = move-exception
            r16 = r17
            goto L_0x00a4
        L_0x0178:
            java.lang.String r18 = r19.toString()     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
        L_0x017c:
            if (r18 == 0) goto L_0x01c9
            java.lang.String r21 = "SUCCESS"
            r0 = r18
            r1 = r21
            boolean r21 = r0.contains(r1)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            if (r21 != 0) goto L_0x01c9
            java.lang.String r21 = "api.m.taobao.com"
            r0 = r26
            r1 = r21
            boolean r21 = r0.contains(r1)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            if (r21 == 0) goto L_0x01c9
            java.lang.String r21 = "ApacheHttpClient"
            java.lang.StringBuilder r22 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            r22.<init>()     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            java.lang.String r23 = "executeHttpPost"
            java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            r0 = r22
            r1 = r18
            java.lang.StringBuilder r22 = r0.append(r1)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            java.lang.String r22 = r22.toString()     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            com.zhiping.dev.android.logger.ZpLogger.e(r21, r22)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
        L_0x01b6:
            if (r6 == 0) goto L_0x01bb
            r6.disconnect()     // Catch:{ IOException -> 0x01fb }
        L_0x01bb:
            if (r11 == 0) goto L_0x01c0
            r11.close()     // Catch:{ IOException -> 0x01fb }
        L_0x01c0:
            if (r17 == 0) goto L_0x01c5
            r17.close()     // Catch:{ IOException -> 0x01fb }
        L_0x01c5:
            r16 = r17
            goto L_0x00fa
        L_0x01c9:
            java.lang.String r21 = "ApacheHttpClient"
            java.lang.StringBuilder r22 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            r22.<init>()     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            java.lang.String r23 = "executeHttpPost"
            java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            r0 = r22
            r1 = r18
            java.lang.StringBuilder r22 = r0.append(r1)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            java.lang.String r22 = r22.toString()     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            com.zhiping.dev.android.logger.ZpLogger.i(r21, r22)     // Catch:{ Exception -> 0x0173, all -> 0x01e8 }
            goto L_0x01b6
        L_0x01e8:
            r21 = move-exception
            r16 = r17
        L_0x01eb:
            if (r6 == 0) goto L_0x01f0
            r6.disconnect()     // Catch:{ IOException -> 0x023b }
        L_0x01f0:
            if (r11 == 0) goto L_0x01f5
            r11.close()     // Catch:{ IOException -> 0x023b }
        L_0x01f5:
            if (r16 == 0) goto L_0x01fa
            r16.close()     // Catch:{ IOException -> 0x023b }
        L_0x01fa:
            throw r21
        L_0x01fb:
            r8 = move-exception
            java.lang.String r21 = "ApacheHttpClient"
            java.lang.StringBuilder r22 = new java.lang.StringBuilder
            r22.<init>()
            java.lang.String r23 = "executeHttpPost IO error="
            java.lang.StringBuilder r22 = r22.append(r23)
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r8)
            java.lang.String r22 = r22.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r21, r22)
            r16 = r17
            goto L_0x00fa
        L_0x021c:
            r8 = move-exception
            java.lang.String r21 = "ApacheHttpClient"
            java.lang.StringBuilder r22 = new java.lang.StringBuilder
            r22.<init>()
            java.lang.String r23 = "executeHttpPost IO error="
            java.lang.StringBuilder r22 = r22.append(r23)
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r8)
            java.lang.String r22 = r22.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r21, r22)
            goto L_0x00fa
        L_0x023b:
            r8 = move-exception
            java.lang.String r22 = "ApacheHttpClient"
            java.lang.StringBuilder r23 = new java.lang.StringBuilder
            r23.<init>()
            java.lang.String r24 = "executeHttpPost IO error="
            java.lang.StringBuilder r23 = r23.append(r24)
            r0 = r23
            java.lang.StringBuilder r23 = r0.append(r8)
            java.lang.String r23 = r23.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r22, r23)
            goto L_0x01fa
        L_0x0259:
            r21 = move-exception
            goto L_0x01eb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.request.core.ApacheHttpClient.executeHttpPost(java.lang.String, java.lang.String, java.lang.String, java.util.List, boolean):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:63:0x016a A[SYNTHETIC, Splitter:B:63:0x016a] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x016f A[Catch:{ IOException -> 0x01c0 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String executeHttpGet(java.lang.String r21, java.lang.String r22, java.lang.String r23, boolean r24) {
        /*
            if (r22 != 0) goto L_0x000b
            java.lang.RuntimeException r17 = new java.lang.RuntimeException
            java.lang.String r18 = "url is null"
            r17.<init>(r18)
            throw r17
        L_0x000b:
            java.lang.String r17 = "HttpClient-request"
            r0 = r17
            r1 = r22
            com.zhiping.dev.android.logger.ZpLogger.i(r0, r1)
            r7 = 0
            r11 = 0
            r15 = 0
            r13 = 0
            org.apache.http.client.methods.HttpGet r14 = new org.apache.http.client.methods.HttpGet     // Catch:{ Exception -> 0x01e3 }
            r0 = r22
            r14.<init>(r0)     // Catch:{ Exception -> 0x01e3 }
            boolean r17 = android.text.TextUtils.isEmpty(r21)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            if (r17 != 0) goto L_0x0030
            java.lang.String r17 = "host"
            r0 = r17
            r1 = r21
            r14.addHeader(r0, r1)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
        L_0x0030:
            org.apache.http.client.HttpClient r17 = client     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            r0 = r17
            org.apache.http.HttpResponse r10 = r0.execute(r14)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            if (r10 == 0) goto L_0x0173
            org.apache.http.HttpEntity r17 = r10.getEntity()     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            if (r17 == 0) goto L_0x0173
            org.apache.http.HttpEntity r17 = r10.getEntity()     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            java.io.InputStream r7 = r17.getContent()     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            java.lang.String r17 = "Content-Encoding"
            r0 = r17
            org.apache.http.Header r4 = r10.getFirstHeader(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            if (r4 == 0) goto L_0x0078
            java.lang.String r17 = r4.getValue()     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            java.lang.String r18 = "gzip"
            boolean r17 = r17.equalsIgnoreCase(r18)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            if (r17 == 0) goto L_0x0078
            java.lang.String r17 = "ApacheHttpClient"
            java.lang.String r18 = "gzip"
            com.zhiping.dev.android.logger.ZpLogger.i(r17, r18)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            java.util.zip.GZIPInputStream r8 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            java.io.BufferedInputStream r17 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            r0 = r17
            r0.<init>(r7)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            r0 = r17
            r8.<init>(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            r7 = r8
        L_0x0078:
            java.io.BufferedReader r12 = new java.io.BufferedReader     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            java.io.InputStreamReader r17 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            r0 = r17
            r1 = r23
            r0.<init>(r7, r1)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            r0 = r17
            r12.<init>(r0)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            org.apache.http.StatusLine r17 = r10.getStatusLine()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            int r17 = r17.getStatusCode()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            r18 = 200(0xc8, float:2.8E-43)
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x0105
            java.lang.StringBuilder r16 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            r16.<init>()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
        L_0x009d:
            java.lang.String r9 = r12.readLine()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            if (r9 == 0) goto L_0x0101
            r0 = r16
            r0.append(r9)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            goto L_0x009d
        L_0x00a9:
            r5 = move-exception
            r13 = r14
            r11 = r12
        L_0x00ac:
            com.yunos.tvtaobao.biz.request.core.ApacheHttpClient$HTTP_REQUEST_ERROR r6 = getRequestException(r5)     // Catch:{ all -> 0x01de }
            if (r13 == 0) goto L_0x00b5
            r13.abort()     // Catch:{ all -> 0x01de }
        L_0x00b5:
            java.lang.String r17 = "ApacheHttpClient"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ all -> 0x01de }
            r18.<init>()     // Catch:{ all -> 0x01de }
            java.lang.String r19 = "executeHttpGet error="
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x01de }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r6)     // Catch:{ all -> 0x01de }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r5)     // Catch:{ all -> 0x01de }
            java.lang.String r19 = " isRetry="
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x01de }
            r0 = r18
            r1 = r24
            java.lang.StringBuilder r18 = r0.append(r1)     // Catch:{ all -> 0x01de }
            java.lang.String r18 = r18.toString()     // Catch:{ all -> 0x01de }
            com.zhiping.dev.android.logger.ZpLogger.e(r17, r18)     // Catch:{ all -> 0x01de }
            if (r24 == 0) goto L_0x00f6
            r17 = 0
            r0 = r21
            r1 = r22
            r2 = r23
            r3 = r17
            java.lang.String r15 = executeHttpGet(r0, r1, r2, r3)     // Catch:{ all -> 0x01de }
        L_0x00f6:
            if (r7 == 0) goto L_0x00fb
            r7.close()     // Catch:{ IOException -> 0x01a1 }
        L_0x00fb:
            if (r11 == 0) goto L_0x0100
            r11.close()     // Catch:{ IOException -> 0x01a1 }
        L_0x0100:
            return r15
        L_0x0101:
            java.lang.String r15 = r16.toString()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
        L_0x0105:
            if (r15 == 0) goto L_0x0148
            java.lang.String r17 = "SUCCESS"
            r0 = r17
            boolean r17 = r15.contains(r0)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            if (r17 != 0) goto L_0x0148
            java.lang.String r17 = "api.m.taobao.com"
            r0 = r22
            r1 = r17
            boolean r17 = r0.contains(r1)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            if (r17 == 0) goto L_0x0148
            java.lang.String r17 = "ApacheHttpClient"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            r18.<init>()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            java.lang.String r19 = "executeHttpGet"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r15)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            java.lang.String r18 = r18.toString()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            com.zhiping.dev.android.logger.ZpLogger.e(r17, r18)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
        L_0x013b:
            r11 = r12
        L_0x013c:
            if (r7 == 0) goto L_0x0141
            r7.close()     // Catch:{ IOException -> 0x0181 }
        L_0x0141:
            if (r11 == 0) goto L_0x0146
            r11.close()     // Catch:{ IOException -> 0x0181 }
        L_0x0146:
            r13 = r14
            goto L_0x0100
        L_0x0148:
            java.lang.String r17 = "ApacheHttpClient"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            r18.<init>()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            java.lang.String r19 = "executeHttpGet"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r15)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            java.lang.String r18 = r18.toString()     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            com.zhiping.dev.android.logger.ZpLogger.i(r17, r18)     // Catch:{ Exception -> 0x00a9, all -> 0x0165 }
            goto L_0x013b
        L_0x0165:
            r17 = move-exception
            r13 = r14
            r11 = r12
        L_0x0168:
            if (r7 == 0) goto L_0x016d
            r7.close()     // Catch:{ IOException -> 0x01c0 }
        L_0x016d:
            if (r11 == 0) goto L_0x0172
            r11.close()     // Catch:{ IOException -> 0x01c0 }
        L_0x0172:
            throw r17
        L_0x0173:
            java.lang.String r17 = "ApacheHttpClient"
            java.lang.String r18 = "response = NULL"
            com.zhiping.dev.android.logger.ZpLogger.i(r17, r18)     // Catch:{ Exception -> 0x017d, all -> 0x01e0 }
            goto L_0x013c
        L_0x017d:
            r5 = move-exception
            r13 = r14
            goto L_0x00ac
        L_0x0181:
            r5 = move-exception
            java.lang.String r17 = "ApacheHttpClient"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "executeHttpGet IO error="
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r5)
            java.lang.String r18 = r18.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r17, r18)
            r13 = r14
            goto L_0x0100
        L_0x01a1:
            r5 = move-exception
            java.lang.String r17 = "ApacheHttpClient"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "executeHttpGet IO error="
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r5)
            java.lang.String r18 = r18.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r17, r18)
            goto L_0x0100
        L_0x01c0:
            r5 = move-exception
            java.lang.String r18 = "ApacheHttpClient"
            java.lang.StringBuilder r19 = new java.lang.StringBuilder
            r19.<init>()
            java.lang.String r20 = "executeHttpGet IO error="
            java.lang.StringBuilder r19 = r19.append(r20)
            r0 = r19
            java.lang.StringBuilder r19 = r0.append(r5)
            java.lang.String r19 = r19.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r18, r19)
            goto L_0x0172
        L_0x01de:
            r17 = move-exception
            goto L_0x0168
        L_0x01e0:
            r17 = move-exception
            r13 = r14
            goto L_0x0168
        L_0x01e3:
            r5 = move-exception
            goto L_0x00ac
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.request.core.ApacheHttpClient.executeHttpGet(java.lang.String, java.lang.String, java.lang.String, boolean):java.lang.String");
    }

    private static HTTP_REQUEST_ERROR getRequestException(Exception e) {
        if (e instanceof UnsupportedEncodingException) {
            HTTP_REQUEST_ERROR error = HTTP_REQUEST_ERROR.HTTP_UNSUPPORTED_ENCODING;
            ZpLogger.e(TAG, "sendRequest error mHttpRequestError" + error + "e=" + e);
            return error;
        } else if (e instanceof SSLPeerUnverifiedException) {
            HTTP_REQUEST_ERROR error2 = HTTP_REQUEST_ERROR.HTTP_SSL_ERROR;
            ZpLogger.e(TAG, "sendRequest error mHttpRequestError" + error2 + "e=" + e);
            return error2;
        } else if (e instanceof ConnectException) {
            HTTP_REQUEST_ERROR error3 = HTTP_REQUEST_ERROR.HTTP_CONNECT_ERROR;
            ZpLogger.e(TAG, "sendRequest error mHttpRequestError" + error3 + "e=" + e);
            return error3;
        } else if (e instanceof SocketException) {
            HTTP_REQUEST_ERROR error4 = HTTP_REQUEST_ERROR.HTTP_SOCKET_ERROR;
            ZpLogger.e(TAG, "sendRequest error mHttpRequestError" + error4 + "e=" + e);
            return error4;
        } else if (e instanceof SocketTimeoutException) {
            HTTP_REQUEST_ERROR error5 = HTTP_REQUEST_ERROR.HTTP_SOCKET_TIMEOUT_ERROR;
            ZpLogger.e(TAG, "sendRequest error mHttpRequestError" + error5 + "e=" + e);
            return error5;
        } else if (e instanceof ConnectTimeoutException) {
            HTTP_REQUEST_ERROR error6 = HTTP_REQUEST_ERROR.HTTP_CONNECT_TIMEOUT_ERROR;
            ZpLogger.e(TAG, "sendRequest error mHttpRequestError" + error6 + "e=" + e);
            return error6;
        } else if (e instanceof UnknownHostException) {
            HTTP_REQUEST_ERROR error7 = HTTP_REQUEST_ERROR.HTTP_UNKNOWN_HOST_ERROR;
            ZpLogger.e(TAG, "sendRequest error mHttpRequestError" + error7 + "e=" + e);
            return error7;
        } else {
            HTTP_REQUEST_ERROR error8 = HTTP_REQUEST_ERROR.HTTP_OTHER_ERROR;
            ZpLogger.e(TAG, "sendRequest error mHttpRequestError" + error8 + "e=" + e);
            return error8;
        }
    }
}
