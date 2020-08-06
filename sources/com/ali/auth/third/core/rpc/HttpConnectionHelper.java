package com.ali.auth.third.core.rpc;

import android.text.TextUtils;
import com.ali.auth.third.core.rpc.protocol.RpcException;
import com.ali.auth.third.core.rpc.protocol.SecurityKey;
import com.ali.auth.third.core.rpc.protocol.SecurityThreadLocal;
import com.ali.auth.third.core.rpc.safe.AESCrypto;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.RSAKey;
import com.ali.auth.third.core.util.Rsa;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpConnectionHelper extends AbsHttpConnectionHelper {
    private static final String TAG = HttpConnectionHelper.class.getSimpleName();
    private static final String TBUSERGW_DAILY = "http://hz.tbusergw.taobao.net/gw.do";
    private static final String TBUSERGW_ONLINE = "https://mgw.m.taobao.com/gw.do";
    private static final String TBUSERGW_PRE = "http://hz.pre.tbusergw.taobao.net/gw.do";

    /* JADX WARNING: type inference failed for: r6v7, types: [java.net.URLConnection] */
    /* JADX WARNING: type inference failed for: r6v9, types: [java.net.URLConnection] */
    /* JADX WARNING: type inference failed for: r6v30, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ed A[SYNTHETIC, Splitter:B:30:0x00ed] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String post(java.lang.String r9, java.lang.String r10, java.lang.String r11) {
        /*
            java.lang.String r6 = TAG
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "post target = "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r9)
            java.lang.String r8 = " params"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r11)
            java.lang.String r7 = r7.toString()
            com.ali.auth.third.core.trace.SDKLogger.d(r6, r7)
            r4 = 0
            r2 = 0
            com.ali.auth.third.core.config.Environment r6 = com.ali.auth.third.core.context.KernelContext.getEnvironment()     // Catch:{ Throwable -> 0x00e0 }
            com.ali.auth.third.core.config.Environment r7 = com.ali.auth.third.core.config.Environment.PRE     // Catch:{ Throwable -> 0x00e0 }
            if (r6 != r7) goto L_0x00b4
            java.net.URL r6 = new java.net.URL     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r7 = "http://hz.pre.tbusergw.taobao.net/gw.do"
            r6.<init>(r7)     // Catch:{ Throwable -> 0x00e0 }
            java.net.URLConnection r6 = r6.openConnection()     // Catch:{ Throwable -> 0x00e0 }
            r0 = r6
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x00e0 }
            r2 = r0
        L_0x003e:
            r6 = 1
            r2.setDoInput(r6)     // Catch:{ Throwable -> 0x00e0 }
            r6 = 1
            r2.setDoOutput(r6)     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r6 = "POST"
            r2.setRequestMethod(r6)     // Catch:{ Throwable -> 0x00e0 }
            r6 = 0
            r2.setUseCaches(r6)     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r6 = "Content-Type"
            java.lang.String r7 = "application/x-www-form-urlencoded"
            r2.setRequestProperty(r6, r7)     // Catch:{ Throwable -> 0x00e0 }
            r6 = 15000(0x3a98, float:2.102E-41)
            r2.setConnectTimeout(r6)     // Catch:{ Throwable -> 0x00e0 }
            r6 = 15000(0x3a98, float:2.102E-41)
            r2.setReadTimeout(r6)     // Catch:{ Throwable -> 0x00e0 }
            com.ali.auth.third.core.service.impl.CredentialManager r6 = com.ali.auth.third.core.service.impl.CredentialManager.INSTANCE     // Catch:{ Throwable -> 0x00e0 }
            com.ali.auth.third.core.model.InternalSession r3 = r6.getInternalSession()     // Catch:{ Throwable -> 0x00e0 }
            com.ali.auth.third.core.model.User r6 = r3.user     // Catch:{ Throwable -> 0x00e0 }
            if (r6 == 0) goto L_0x0081
            com.ali.auth.third.core.model.User r6 = r3.user     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r6 = r6.userId     // Catch:{ Throwable -> 0x00e0 }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x00e0 }
            if (r6 != 0) goto L_0x0081
            java.lang.String r6 = "userid"
            com.ali.auth.third.core.model.User r7 = r3.user     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r7 = r7.userId     // Catch:{ Throwable -> 0x00e0 }
            r2.setRequestProperty(r6, r7)     // Catch:{ Throwable -> 0x00e0 }
        L_0x0081:
            java.io.OutputStreamWriter r5 = new java.io.OutputStreamWriter     // Catch:{ Throwable -> 0x00e0 }
            java.io.OutputStream r6 = r2.getOutputStream()     // Catch:{ Throwable -> 0x00e0 }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r6 = encodeRequest(r9, r10, r11)     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            r5.write(r6)     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            r5.flush()     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            int r6 = r2.getResponseCode()     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            filterResponseCode(r6)     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            java.io.InputStream r6 = r2.getInputStream()     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            java.lang.String r7 = r2.getContentType()     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            java.lang.String r7 = getCharset(r7)     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            java.lang.String r6 = decodeResponse(r6, r7)     // Catch:{ Throwable -> 0x00f8, all -> 0x00f5 }
            com.ali.auth.third.core.util.IOUtils.closeQuietly(r5)
            if (r2 == 0) goto L_0x00b3
            r2.disconnect()     // Catch:{ Exception -> 0x00f1 }
        L_0x00b3:
            return r6
        L_0x00b4:
            com.ali.auth.third.core.config.Environment r6 = com.ali.auth.third.core.context.KernelContext.getEnvironment()     // Catch:{ Throwable -> 0x00e0 }
            com.ali.auth.third.core.config.Environment r7 = com.ali.auth.third.core.config.Environment.TEST     // Catch:{ Throwable -> 0x00e0 }
            if (r6 != r7) goto L_0x00ce
            java.net.URL r6 = new java.net.URL     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r7 = "http://hz.tbusergw.taobao.net/gw.do"
            r6.<init>(r7)     // Catch:{ Throwable -> 0x00e0 }
            java.net.URLConnection r6 = r6.openConnection()     // Catch:{ Throwable -> 0x00e0 }
            r0 = r6
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x00e0 }
            r2 = r0
            goto L_0x003e
        L_0x00ce:
            java.net.URL r6 = new java.net.URL     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r7 = "https://mgw.m.taobao.com/gw.do"
            r6.<init>(r7)     // Catch:{ Throwable -> 0x00e0 }
            java.net.URLConnection r6 = r6.openConnection()     // Catch:{ Throwable -> 0x00e0 }
            r0 = r6
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x00e0 }
            r2 = r0
            goto L_0x003e
        L_0x00e0:
            r1 = move-exception
        L_0x00e1:
            java.lang.RuntimeException r6 = new java.lang.RuntimeException     // Catch:{ all -> 0x00e7 }
            r6.<init>(r1)     // Catch:{ all -> 0x00e7 }
            throw r6     // Catch:{ all -> 0x00e7 }
        L_0x00e7:
            r6 = move-exception
        L_0x00e8:
            com.ali.auth.third.core.util.IOUtils.closeQuietly(r4)
            if (r2 == 0) goto L_0x00f0
            r2.disconnect()     // Catch:{ Exception -> 0x00f3 }
        L_0x00f0:
            throw r6
        L_0x00f1:
            r7 = move-exception
            goto L_0x00b3
        L_0x00f3:
            r7 = move-exception
            goto L_0x00f0
        L_0x00f5:
            r6 = move-exception
            r4 = r5
            goto L_0x00e8
        L_0x00f8:
            r1 = move-exception
            r4 = r5
            goto L_0x00e1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.core.rpc.HttpConnectionHelper.post(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    public static String encodeRequest(String target, String version, String params) {
        String triDesKey = SecurityKey.getRandomString(16);
        SecurityThreadLocal.set(triDesKey);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("id=1&");
            sb.append("apiName=" + target + "&");
            sb.append("apiVersion=" + version + "&");
            sb.append("requestData=" + URLEncoder.encode(getEncryptedData(triDesKey, params), "UTF-8"));
            sb.append("&symType=AES");
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            SDKLogger.e(TAG, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String getEncryptedData(String triDesKey, String request) {
        String rsaKey = RSAKey.getRsaPubkey();
        if (TextUtils.isEmpty(rsaKey)) {
            throw new IllegalArgumentException("get rsa from server failed!!!");
        }
        String encryptedKey = Rsa.encrypt(triDesKey, rsaKey);
        String encryptedContent = null;
        try {
            encryptedContent = AESCrypto.instance().encrypt(request, triDesKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format(Locale.getDefault(), "%08X%s%08X%s", new Object[]{Integer.valueOf(encryptedKey.length()), encryptedKey, Integer.valueOf(encryptedContent.length()), encryptedContent});
    }

    private static String decodeResponse(InputStream inputStream, String charset) {
        String message;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        String response = null;
        while (true) {
            try {
                int len = inputStream.read(data);
                if (len == -1) {
                    break;
                }
                byteArrayOutputStream.write(data, 0, len);
            } catch (JSONException e) {
                e = e;
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                throw new RpcException("net work error");
            }
        }
        String response2 = new String(byteArrayOutputStream.toByteArray(), charset);
        try {
            String triDesKey = SecurityThreadLocal.get();
            SDKLogger.e(TAG, "raw response = " + response2);
            String response3 = AESCrypto.instance().decryptWrapper(response2, triDesKey);
            SDKLogger.e(TAG, "decrypt gateway response=" + response3);
            JSONObject jsonObject = new JSONObject(response3);
            int resultStatus = jsonObject.getInt("resultStatus");
            if (resultStatus == 1000) {
                return jsonObject.getString("result");
            }
            throw new RpcException(Integer.valueOf(resultStatus), "memo");
        } catch (JSONException e3) {
            e = e3;
            response = response2;
        } catch (Exception e4) {
            e = e4;
            String str = response2;
            e.printStackTrace();
            throw new RpcException("net work error");
        }
        if (("response  =" + response + SymbolExpUtil.SYMBOL_COLON + e) == null) {
            message = "";
        } else {
            message = e.getMessage();
        }
        throw new RpcException((Integer) 10, message);
    }
}
