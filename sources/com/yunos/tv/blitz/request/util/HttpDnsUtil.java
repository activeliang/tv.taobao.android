package com.yunos.tv.blitz.request.util;

import com.yunos.tv.blitz.request.common.AppDebug;
import java.net.URL;

public class HttpDnsUtil {
    public static String getDomainFromUrl(String s) {
        try {
            String domain = new URL(s).getHost();
            AppDebug.i("getDomainFromUrl", "getDomainFromUrl domain:" + domain + ";url=" + s);
            return domain;
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        r0 = getDomainFromUrl(r6);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getHttpDnsUrl(java.lang.String r6) {
        /*
            boolean r3 = android.text.TextUtils.isEmpty(r6)
            if (r3 != 0) goto L_0x000f
            java.lang.String r3 = "https"
            boolean r3 = r6.startsWith(r3)
            if (r3 == 0) goto L_0x0010
        L_0x000f:
            return r6
        L_0x0010:
            java.lang.String r0 = getDomainFromUrl(r6)
            com.spdu.httpdns.HttpDns r3 = com.spdu.httpdns.HttpDns.getInstance()
            java.lang.String r1 = r3.getIpByHttpDns(r0)
            if (r1 == 0) goto L_0x000f
            java.lang.String r2 = r6.replaceFirst(r0, r1)
            java.lang.String r3 = "httpdns"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "url:"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r4 = r4.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r3, r4)
            java.lang.String r3 = "httpdns"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "url2:"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r4 = r4.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r3, r4)
            java.lang.String r3 = "httpdns"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "host:"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r5 = ",ip:"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r1)
            java.lang.String r4 = r4.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r3, r4)
            r6 = r2
            goto L_0x000f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.blitz.request.util.HttpDnsUtil.getHttpDnsUrl(java.lang.String):java.lang.String");
    }
}
