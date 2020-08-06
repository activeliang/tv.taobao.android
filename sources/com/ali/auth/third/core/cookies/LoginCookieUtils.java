package com.ali.auth.third.core.cookies;

import android.text.TextUtils;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONObject;

public class LoginCookieUtils {
    private static final char COMMA = ',';
    private static final String DOMAIN = "domain";
    private static final char EQUAL = '=';
    private static final String EXPIRES = "expires";
    private static final String HTTPS = "https";
    private static final String HTTP_ONLY = "httponly";
    private static final int HTTP_ONLY_LENGTH = HTTP_ONLY.length();
    private static final String MAX_AGE = "max-age";
    private static final int MAX_COOKIE_LENGTH = 4096;
    private static final String PATH = "path";
    private static final char PATH_DELIM = '/';
    private static final char PERIOD = '.';
    private static final char QUESTION_MARK = '?';
    private static final char QUOTATION = '\"';
    private static final String SECURE = "secure";
    private static final int SECURE_LENGTH = SECURE.length();
    private static final char SEMICOLON = ';';
    private static final String TAG = "login.LoginCookieUtils";
    private static final char WHITE_SPACE = ' ';

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006f, code lost:
        if (r8 == -1) goto L_0x000a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.ali.auth.third.core.cookies.LoginCookie parseCookie(java.lang.String r20) {
        /*
            r8 = 0
            int r10 = r20.length()
        L_0x0005:
            r4 = 0
            if (r8 < 0) goto L_0x000a
            if (r8 < r10) goto L_0x000c
        L_0x000a:
            r14 = 0
        L_0x000b:
            return r14
        L_0x000c:
            r0 = r20
            char r14 = r0.charAt(r8)
            r15 = 32
            if (r14 != r15) goto L_0x0019
            int r8 = r8 + 1
            goto L_0x0005
        L_0x0019:
            r14 = 59
            r0 = r20
            int r12 = r0.indexOf(r14, r8)
            r14 = 61
            r0 = r20
            int r6 = r0.indexOf(r14, r8)
            com.ali.auth.third.core.cookies.LoginCookie r4 = new com.ali.auth.third.core.cookies.LoginCookie
            r4.<init>()
            r14 = -1
            if (r12 == r14) goto L_0x0033
            if (r12 < r6) goto L_0x0036
        L_0x0033:
            r14 = -1
            if (r6 != r14) goto L_0x004c
        L_0x0036:
            r14 = -1
            if (r12 != r14) goto L_0x003a
            r12 = r10
        L_0x003a:
            r0 = r20
            java.lang.String r14 = r0.substring(r8, r12)
            r4.name = r14
            r14 = 0
            r4.value = r14
        L_0x0045:
            r8 = r12
        L_0x0046:
            if (r8 < 0) goto L_0x004a
            if (r8 < r10) goto L_0x00a9
        L_0x004a:
            r14 = r4
            goto L_0x000b
        L_0x004c:
            r0 = r20
            java.lang.String r14 = r0.substring(r8, r6)
            r4.name = r14
            int r14 = r10 + -1
            if (r6 >= r14) goto L_0x0071
            int r14 = r6 + 1
            r0 = r20
            char r14 = r0.charAt(r14)
            r15 = 34
            if (r14 != r15) goto L_0x0071
            r14 = 34
            int r15 = r6 + 2
            r0 = r20
            int r8 = r0.indexOf(r14, r15)
            r14 = -1
            if (r8 == r14) goto L_0x000a
        L_0x0071:
            r14 = 59
            r0 = r20
            int r12 = r0.indexOf(r14, r8)
            r14 = -1
            if (r12 != r14) goto L_0x007d
            r12 = r10
        L_0x007d:
            int r14 = r12 - r6
            r15 = 4096(0x1000, float:5.74E-42)
            if (r14 <= r15) goto L_0x0092
            int r14 = r6 + 1
            int r15 = r6 + 1
            int r15 = r15 + 4096
            r0 = r20
            java.lang.String r14 = r0.substring(r14, r15)
            r4.value = r14
            goto L_0x0045
        L_0x0092:
            int r14 = r6 + 1
            if (r14 == r12) goto L_0x0098
            if (r12 >= r6) goto L_0x009e
        L_0x0098:
            java.lang.String r14 = ""
            r4.value = r14
            goto L_0x0045
        L_0x009e:
            int r14 = r6 + 1
            r0 = r20
            java.lang.String r14 = r0.substring(r14, r12)
            r4.value = r14
            goto L_0x0045
        L_0x00a9:
            r0 = r20
            char r14 = r0.charAt(r8)
            r15 = 32
            if (r14 == r15) goto L_0x00bd
            r0 = r20
            char r14 = r0.charAt(r8)
            r15 = 59
            if (r14 != r15) goto L_0x00c0
        L_0x00bd:
            int r8 = r8 + 1
            goto L_0x0046
        L_0x00c0:
            r0 = r20
            char r14 = r0.charAt(r8)
            r15 = 44
            if (r14 != r15) goto L_0x00ce
            int r8 = r8 + 1
            goto L_0x004a
        L_0x00ce:
            int r14 = r10 - r8
            int r15 = SECURE_LENGTH
            if (r14 < r15) goto L_0x00fc
            int r14 = SECURE_LENGTH
            int r14 = r14 + r8
            r0 = r20
            java.lang.String r14 = r0.substring(r8, r14)
            java.lang.String r15 = "secure"
            boolean r14 = r14.equalsIgnoreCase(r15)
            if (r14 == 0) goto L_0x00fc
            int r14 = SECURE_LENGTH
            int r8 = r8 + r14
            r14 = 1
            r4.secure = r14
            if (r8 == r10) goto L_0x004a
            r0 = r20
            char r14 = r0.charAt(r8)
            r15 = 61
            if (r14 != r15) goto L_0x0046
            int r8 = r8 + 1
            goto L_0x0046
        L_0x00fc:
            int r14 = r10 - r8
            int r15 = HTTP_ONLY_LENGTH
            if (r14 < r15) goto L_0x012a
            int r14 = HTTP_ONLY_LENGTH
            int r14 = r14 + r8
            r0 = r20
            java.lang.String r14 = r0.substring(r8, r14)
            java.lang.String r15 = "httponly"
            boolean r14 = r14.equalsIgnoreCase(r15)
            if (r14 == 0) goto L_0x012a
            int r14 = HTTP_ONLY_LENGTH
            int r8 = r8 + r14
            r14 = 1
            r4.httpOnly = r14
            if (r8 == r10) goto L_0x004a
            r0 = r20
            char r14 = r0.charAt(r8)
            r15 = 61
            if (r14 != r15) goto L_0x0046
            int r8 = r8 + 1
            goto L_0x0046
        L_0x012a:
            r14 = 61
            r0 = r20
            int r6 = r0.indexOf(r14, r8)
            if (r6 <= 0) goto L_0x0267
            r0 = r20
            java.lang.String r14 = r0.substring(r8, r6)
            java.lang.String r11 = r14.toLowerCase()
            java.lang.String r14 = "expires"
            boolean r14 = r11.equals(r14)
            if (r14 == 0) goto L_0x015a
            r14 = 44
            r0 = r20
            int r2 = r0.indexOf(r14, r6)
            r14 = -1
            if (r2 == r14) goto L_0x015a
            int r14 = r2 - r6
            r15 = 10
            if (r14 > r15) goto L_0x015a
            int r8 = r2 + 1
        L_0x015a:
            r14 = 59
            r0 = r20
            int r12 = r0.indexOf(r14, r8)
            r14 = 44
            r0 = r20
            int r3 = r0.indexOf(r14, r8)
            r14 = -1
            if (r12 != r14) goto L_0x01c5
            r14 = -1
            if (r3 != r14) goto L_0x01c5
            r8 = r10
        L_0x0171:
            int r14 = r6 + 1
            r0 = r20
            java.lang.String r13 = r0.substring(r14, r8)
            int r14 = r13.length()
            r15 = 2
            if (r14 <= r15) goto L_0x0197
            r14 = 0
            char r14 = r13.charAt(r14)
            r15 = 34
            if (r14 != r15) goto L_0x0197
            r14 = 34
            r15 = 1
            int r5 = r13.indexOf(r14, r15)
            if (r5 <= 0) goto L_0x0197
            r14 = 1
            java.lang.String r13 = r13.substring(r14, r5)
        L_0x0197:
            java.lang.String r14 = "expires"
            boolean r14 = r11.equals(r14)
            if (r14 == 0) goto L_0x01d4
            long r14 = com.ali.auth.third.core.cookies.HttpDateTime.parse(r13)     // Catch:{ IllegalArgumentException -> 0x01a8 }
            r4.expires = r14     // Catch:{ IllegalArgumentException -> 0x01a8 }
            goto L_0x0046
        L_0x01a8:
            r7 = move-exception
            java.lang.String r14 = "login.LoginCookieUtils"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "illegal format for expires: "
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.StringBuilder r15 = r15.append(r13)
            java.lang.String r15 = r15.toString()
            com.ali.auth.third.core.trace.SDKLogger.e(r14, r15, r7)
            goto L_0x0046
        L_0x01c5:
            r14 = -1
            if (r12 != r14) goto L_0x01ca
            r8 = r3
            goto L_0x0171
        L_0x01ca:
            r14 = -1
            if (r3 != r14) goto L_0x01cf
            r8 = r12
            goto L_0x0171
        L_0x01cf:
            int r8 = java.lang.Math.min(r12, r3)
            goto L_0x0171
        L_0x01d4:
            java.lang.String r14 = "max-age"
            boolean r14 = r11.equals(r14)
            if (r14 == 0) goto L_0x020c
            long r14 = java.lang.System.currentTimeMillis()     // Catch:{ NumberFormatException -> 0x01ef }
            r16 = 1000(0x3e8, double:4.94E-321)
            long r18 = java.lang.Long.parseLong(r13)     // Catch:{ NumberFormatException -> 0x01ef }
            long r16 = r16 * r18
            long r14 = r14 + r16
            r4.expires = r14     // Catch:{ NumberFormatException -> 0x01ef }
            goto L_0x0046
        L_0x01ef:
            r7 = move-exception
            java.lang.String r14 = "login.LoginCookieUtils"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "illegal format for max-age: "
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.StringBuilder r15 = r15.append(r13)
            java.lang.String r15 = r15.toString()
            com.ali.auth.third.core.trace.SDKLogger.e(r14, r15, r7)
            goto L_0x0046
        L_0x020c:
            java.lang.String r14 = "path"
            boolean r14 = r11.equals(r14)
            if (r14 == 0) goto L_0x021f
            int r14 = r13.length()
            if (r14 <= 0) goto L_0x0046
            r4.path = r13
            goto L_0x0046
        L_0x021f:
            java.lang.String r14 = "domain"
            boolean r14 = r11.equals(r14)
            if (r14 == 0) goto L_0x0046
            r14 = 46
            int r9 = r13.lastIndexOf(r14)
            if (r9 != 0) goto L_0x0235
            r14 = 0
            r4.domain = r14
            goto L_0x0046
        L_0x0235:
            int r14 = r9 + 1
            java.lang.String r14 = r13.substring(r14)     // Catch:{ NumberFormatException -> 0x0240 }
            java.lang.Integer.parseInt(r14)     // Catch:{ NumberFormatException -> 0x0240 }
            goto L_0x0046
        L_0x0240:
            r14 = move-exception
            java.lang.String r13 = r13.toLowerCase()
            r14 = 0
            char r14 = r13.charAt(r14)
            r15 = 46
            if (r14 == r15) goto L_0x0263
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r15 = 46
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.StringBuilder r14 = r14.append(r13)
            java.lang.String r13 = r14.toString()
            int r9 = r9 + 1
        L_0x0263:
            r4.domain = r13
            goto L_0x0046
        L_0x0267:
            r8 = r10
            goto L_0x0046
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.core.cookies.LoginCookieUtils.parseCookie(java.lang.String):com.ali.auth.third.core.cookies.LoginCookie");
    }

    public static void expiresCookies(LoginCookie cookie) {
        Long l = 1000L;
        cookie.expires = l.longValue();
    }

    public static String getHttpDomin(LoginCookie cookie) {
        String host = cookie.domain;
        if (!TextUtils.isEmpty(host) && host.startsWith(".")) {
            host = host.substring(1);
        }
        return "http://" + host;
    }

    public static String getValue(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        try {
            String cookies = CookieManagerService.getWebViewProxy().getCookie(".taobao.com");
            if (TextUtils.isEmpty(cookies)) {
                return null;
            }
            for (String oneCookie : cookies.split(SymbolExpUtil.SYMBOL_SEMICOLON)) {
                String[] keyValue = oneCookie.split("=");
                if (keyValue.length >= 2 && key.equals(keyValue[0].trim())) {
                    int index = oneCookie.indexOf("=");
                    if (keyValue.length == 2) {
                        return keyValue[1].trim();
                    }
                    return oneCookie.substring(index + 1);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getKeyValues(String key) {
        JSONObject ret = new JSONObject();
        if (!TextUtils.isEmpty(key)) {
            try {
                String cookies = CookieManagerService.getWebViewProxy().getCookie(".taobao.com");
                if (!TextUtils.isEmpty(cookies)) {
                    for (String oneCookie : cookies.split(SymbolExpUtil.SYMBOL_SEMICOLON)) {
                        String[] keyValue = oneCookie.split("=");
                        if (keyValue.length >= 2 && keyValue[0].contains(key)) {
                            if (keyValue.length == 2) {
                                ret.put(keyValue[0].trim(), keyValue[1].trim());
                            } else {
                                ret.put(keyValue[0].trim(), oneCookie.substring(oneCookie.indexOf("=") + 1));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
