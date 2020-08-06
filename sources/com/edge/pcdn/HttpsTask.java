package com.edge.pcdn;

import android.os.Handler;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class HttpsTask implements Runnable {
    private static final int BUFFER_SIZE = 1024;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 30000;
    private Handler handler;
    private String httpMethod;
    private String param;
    private String urlStr;

    public HttpsTask(String url, Handler handler2, String param2, String httpMethod2) {
        this.handler = handler2;
        this.urlStr = url;
        this.param = param2;
        this.httpMethod = httpMethod2;
    }

    /* JADX WARNING: type inference failed for: r16v17, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:108:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0190 A[Catch:{ all -> 0x024d }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x01b1 A[SYNTHETIC, Splitter:B:32:0x01b1] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x01b6 A[SYNTHETIC, Splitter:B:35:0x01b6] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x01bb A[SYNTHETIC, Splitter:B:38:0x01bb] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x01c0 A[SYNTHETIC, Splitter:B:41:0x01c0] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0250 A[SYNTHETIC, Splitter:B:80:0x0250] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0255 A[SYNTHETIC, Splitter:B:83:0x0255] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x025a A[SYNTHETIC, Splitter:B:86:0x025a] */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x025f A[SYNTHETIC, Splitter:B:89:0x025f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r21 = this;
            android.os.Message r10 = new android.os.Message
            r10.<init>()
            java.lang.String r16 = "GET"
            r0 = r21
            java.lang.String r0 = r0.httpMethod
            r17 = r0
            boolean r16 = r16.equals(r17)
            if (r16 == 0) goto L_0x0037
            java.lang.StringBuilder r16 = new java.lang.StringBuilder
            r16.<init>()
            r0 = r21
            java.lang.String r0 = r0.urlStr
            r17 = r0
            java.lang.StringBuilder r16 = r16.append(r17)
            r0 = r21
            java.lang.String r0 = r0.param
            r17 = r0
            java.lang.StringBuilder r16 = r16.append(r17)
            java.lang.String r16 = r16.toString()
            r0 = r16
            r1 = r21
            r1.urlStr = r0
        L_0x0037:
            r0 = r21
            java.lang.String r0 = r0.urlStr
            r16 = r0
            com.edge.pcdn.PcdnLog.d(r16)
            r8 = 0
            r11 = 0
            r9 = 0
            r3 = 0
            java.net.URL r15 = new java.net.URL     // Catch:{ Exception -> 0x028d }
            r0 = r21
            java.lang.String r0 = r0.urlStr     // Catch:{ Exception -> 0x028d }
            r16 = r0
            r15.<init>(r16)     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = "TLS"
            javax.net.ssl.SSLContext r14 = javax.net.ssl.SSLContext.getInstance(r16)     // Catch:{ Exception -> 0x028d }
            r16 = 0
            r17 = 1
            r0 = r17
            javax.net.ssl.TrustManager[] r0 = new javax.net.ssl.TrustManager[r0]     // Catch:{ Exception -> 0x028d }
            r17 = r0
            r18 = 0
            com.edge.pcdn.HttpsTask$MyTrustManager r19 = new com.edge.pcdn.HttpsTask$MyTrustManager     // Catch:{ Exception -> 0x028d }
            r20 = 0
            r0 = r19
            r1 = r21
            r2 = r20
            r0.<init>()     // Catch:{ Exception -> 0x028d }
            r17[r18] = r19     // Catch:{ Exception -> 0x028d }
            java.security.SecureRandom r18 = new java.security.SecureRandom     // Catch:{ Exception -> 0x028d }
            r18.<init>()     // Catch:{ Exception -> 0x028d }
            r0 = r16
            r1 = r17
            r2 = r18
            r14.init(r0, r1, r2)     // Catch:{ Exception -> 0x028d }
            java.net.URLConnection r16 = r15.openConnection()     // Catch:{ Exception -> 0x028d }
            r0 = r16
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ Exception -> 0x028d }
            r8 = r0
            com.edge.pcdn.HttpsTask$MyHostnameVerifier r16 = new com.edge.pcdn.HttpsTask$MyHostnameVerifier     // Catch:{ Exception -> 0x028d }
            r17 = 0
            r0 = r16
            r1 = r21
            r2 = r17
            r0.<init>()     // Catch:{ Exception -> 0x028d }
            r0 = r16
            r8.setHostnameVerifier(r0)     // Catch:{ Exception -> 0x028d }
            javax.net.ssl.SSLSocketFactory r16 = r14.getSocketFactory()     // Catch:{ Exception -> 0x028d }
            r0 = r16
            r8.setSSLSocketFactory(r0)     // Catch:{ Exception -> 0x028d }
            r16 = 1
            r0 = r16
            r8.setDoInput(r0)     // Catch:{ Exception -> 0x028d }
            r16 = 1
            r0 = r16
            r8.setDoOutput(r0)     // Catch:{ Exception -> 0x028d }
            r16 = 0
            r0 = r16
            r8.setUseCaches(r0)     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = "Connection"
            java.lang.String r17 = "Keep-Alive"
            r0 = r16
            r1 = r17
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = "Charset"
            java.lang.String r17 = "UTF-8"
            r0 = r16
            r1 = r17
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = "Content-Type"
            java.lang.String r17 = "application/x-www-form-urlencoded"
            r0 = r16
            r1 = r17
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = "User-Agent"
            java.lang.String r17 = "PCDN"
            r0 = r16
            r1 = r17
            r8.addRequestProperty(r0, r1)     // Catch:{ Exception -> 0x028d }
            r16 = 10000(0x2710, float:1.4013E-41)
            r0 = r16
            r8.setConnectTimeout(r0)     // Catch:{ Exception -> 0x028d }
            r16 = 30000(0x7530, float:4.2039E-41)
            r0 = r16
            r8.setReadTimeout(r0)     // Catch:{ Exception -> 0x028d }
            r0 = r21
            java.lang.String r0 = r0.httpMethod     // Catch:{ Exception -> 0x028d }
            r16 = r0
            r0 = r16
            r8.setRequestMethod(r0)     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = "POST"
            r0 = r21
            java.lang.String r0 = r0.httpMethod     // Catch:{ Exception -> 0x028d }
            r17 = r0
            boolean r16 = r16.equals(r17)     // Catch:{ Exception -> 0x028d }
            if (r16 == 0) goto L_0x0154
            java.lang.String r16 = "POST"
            r0 = r16
            r8.setRequestMethod(r0)     // Catch:{ Exception -> 0x028d }
            r0 = r21
            java.lang.String r0 = r0.param     // Catch:{ Exception -> 0x028d }
            r16 = r0
            if (r16 == 0) goto L_0x0154
            r0 = r21
            java.lang.String r0 = r0.param     // Catch:{ Exception -> 0x028d }
            r16 = r0
            byte[] r6 = r16.getBytes()     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = "Content-Length"
            int r0 = r6.length     // Catch:{ Exception -> 0x028d }
            r17 = r0
            java.lang.String r17 = java.lang.String.valueOf(r17)     // Catch:{ Exception -> 0x028d }
            r0 = r16
            r1 = r17
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x028d }
            java.io.DataOutputStream r12 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x028d }
            java.io.OutputStream r16 = r8.getOutputStream()     // Catch:{ Exception -> 0x028d }
            r0 = r16
            r12.<init>(r0)     // Catch:{ Exception -> 0x028d }
            r12.write(r6)     // Catch:{ Exception -> 0x0290, all -> 0x0287 }
            r12.flush()     // Catch:{ Exception -> 0x0290, all -> 0x0287 }
            r12.close()     // Catch:{ Exception -> 0x0290, all -> 0x0287 }
            r11 = r12
        L_0x0154:
            int r13 = r8.getResponseCode()     // Catch:{ Exception -> 0x028d }
            r10.what = r13     // Catch:{ Exception -> 0x028d }
            java.io.InputStream r9 = r8.getInputStream()     // Catch:{ Exception -> 0x028d }
            if (r9 == 0) goto L_0x01d8
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x028d }
            r4.<init>()     // Catch:{ Exception -> 0x028d }
            r16 = 1024(0x400, float:1.435E-42)
            r0 = r16
            byte[] r6 = new byte[r0]     // Catch:{ Exception -> 0x0186, all -> 0x028a }
            r5 = -1
        L_0x016c:
            r16 = 0
            r17 = 1024(0x400, float:1.435E-42)
            r0 = r16
            r1 = r17
            int r5 = r9.read(r6, r0, r1)     // Catch:{ Exception -> 0x0186, all -> 0x028a }
            r16 = -1
            r0 = r16
            if (r5 == r0) goto L_0x01c4
            r16 = 0
            r0 = r16
            r4.write(r6, r0, r5)     // Catch:{ Exception -> 0x0186, all -> 0x028a }
            goto L_0x016c
        L_0x0186:
            r7 = move-exception
            r3 = r4
        L_0x0188:
            r0 = r21
            android.os.Handler r0 = r0.handler     // Catch:{ all -> 0x024d }
            r16 = r0
            if (r16 == 0) goto L_0x01a8
            r16 = -2
            r0 = r16
            r10.what = r0     // Catch:{ all -> 0x024d }
            java.lang.String r16 = "网络请求过程中产生异常"
            r0 = r16
            r10.obj = r0     // Catch:{ all -> 0x024d }
            r0 = r21
            android.os.Handler r0 = r0.handler     // Catch:{ all -> 0x024d }
            r16 = r0
            r0 = r16
            r0.sendMessage(r10)     // Catch:{ all -> 0x024d }
        L_0x01a8:
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)     // Catch:{ all -> 0x024d }
            com.edge.pcdn.PcdnLog.d(r16)     // Catch:{ all -> 0x024d }
            if (r11 == 0) goto L_0x01b4
            r11.close()     // Catch:{ Exception -> 0x0227 }
        L_0x01b4:
            if (r9 == 0) goto L_0x01b9
            r9.close()     // Catch:{ IOException -> 0x0230 }
        L_0x01b9:
            if (r3 == 0) goto L_0x01be
            r3.close()     // Catch:{ IOException -> 0x0239 }
        L_0x01be:
            if (r8 == 0) goto L_0x01c3
            r8.disconnect()     // Catch:{ Exception -> 0x0243 }
        L_0x01c3:
            return
        L_0x01c4:
            r9.close()     // Catch:{ Exception -> 0x0186, all -> 0x028a }
            r4.close()     // Catch:{ Exception -> 0x0186, all -> 0x028a }
            java.lang.String r16 = "UTF-8"
            r0 = r16
            java.lang.String r16 = r4.toString(r0)     // Catch:{ Exception -> 0x0186, all -> 0x028a }
            r0 = r16
            r10.obj = r0     // Catch:{ Exception -> 0x0186, all -> 0x028a }
            r3 = r4
        L_0x01d8:
            r8.disconnect()     // Catch:{ Exception -> 0x028d }
            r0 = r21
            android.os.Handler r0 = r0.handler     // Catch:{ Exception -> 0x028d }
            r16 = r0
            if (r16 == 0) goto L_0x01ee
            r0 = r21
            android.os.Handler r0 = r0.handler     // Catch:{ Exception -> 0x028d }
            r16 = r0
            r0 = r16
            r0.sendMessage(r10)     // Catch:{ Exception -> 0x028d }
        L_0x01ee:
            if (r11 == 0) goto L_0x01f3
            r11.close()     // Catch:{ Exception -> 0x020c }
        L_0x01f3:
            if (r9 == 0) goto L_0x01f8
            r9.close()     // Catch:{ IOException -> 0x0215 }
        L_0x01f8:
            if (r3 == 0) goto L_0x01fd
            r3.close()     // Catch:{ IOException -> 0x021e }
        L_0x01fd:
            if (r8 == 0) goto L_0x01c3
            r8.disconnect()     // Catch:{ Exception -> 0x0203 }
            goto L_0x01c3
        L_0x0203:
            r7 = move-exception
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r16)
            goto L_0x01c3
        L_0x020c:
            r7 = move-exception
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r16)
            goto L_0x01f3
        L_0x0215:
            r7 = move-exception
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r16)
            goto L_0x01f8
        L_0x021e:
            r7 = move-exception
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r16)
            goto L_0x01fd
        L_0x0227:
            r7 = move-exception
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r16)
            goto L_0x01b4
        L_0x0230:
            r7 = move-exception
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r16)
            goto L_0x01b9
        L_0x0239:
            r7 = move-exception
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r16)
            goto L_0x01be
        L_0x0243:
            r7 = move-exception
            java.lang.String r16 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r16)
            goto L_0x01c3
        L_0x024d:
            r16 = move-exception
        L_0x024e:
            if (r11 == 0) goto L_0x0253
            r11.close()     // Catch:{ Exception -> 0x0263 }
        L_0x0253:
            if (r9 == 0) goto L_0x0258
            r9.close()     // Catch:{ IOException -> 0x026c }
        L_0x0258:
            if (r3 == 0) goto L_0x025d
            r3.close()     // Catch:{ IOException -> 0x0275 }
        L_0x025d:
            if (r8 == 0) goto L_0x0262
            r8.disconnect()     // Catch:{ Exception -> 0x027e }
        L_0x0262:
            throw r16
        L_0x0263:
            r7 = move-exception
            java.lang.String r17 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r17)
            goto L_0x0253
        L_0x026c:
            r7 = move-exception
            java.lang.String r17 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r17)
            goto L_0x0258
        L_0x0275:
            r7 = move-exception
            java.lang.String r17 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r17)
            goto L_0x025d
        L_0x027e:
            r7 = move-exception
            java.lang.String r17 = com.edge.pcdn.PcdnLog.toString(r7)
            com.edge.pcdn.PcdnLog.e(r17)
            goto L_0x0262
        L_0x0287:
            r16 = move-exception
            r11 = r12
            goto L_0x024e
        L_0x028a:
            r16 = move-exception
            r3 = r4
            goto L_0x024e
        L_0x028d:
            r7 = move-exception
            goto L_0x0188
        L_0x0290:
            r7 = move-exception
            r11 = r12
            goto L_0x0188
        */
        throw new UnsupportedOperationException("Method not decompiled: com.edge.pcdn.HttpsTask.run():void");
    }

    private class MyHostnameVerifier implements HostnameVerifier {
        private MyHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            if (hostname == null || !hostname.endsWith("alicdn.com")) {
                return false;
            }
            return true;
        }
    }

    private class MyTrustManager implements X509TrustManager {
        private MyTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
