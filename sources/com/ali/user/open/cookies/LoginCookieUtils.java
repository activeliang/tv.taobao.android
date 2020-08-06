package com.ali.user.open.cookies;

import android.text.TextUtils;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONObject;

public class LoginCookieUtils {
    private static final char COMMA = ',';
    private static final String DISCARD = "discard";
    private static final int DISCARD_LENGTH = DISCARD.length();
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
    private static final String VERSION = "version";
    private static final char WHITE_SPACE = ' ';

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0075, code lost:
        if (r8 == -1) goto L_0x000a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.ali.user.open.cookies.LoginCookie parseCookie(java.lang.String r22) {
        /*
            r8 = 0
            int r10 = r22.length()
        L_0x0005:
            r4 = 0
            if (r8 < 0) goto L_0x000a
            if (r8 < r10) goto L_0x000c
        L_0x000a:
            r15 = 0
        L_0x000b:
            return r15
        L_0x000c:
            r0 = r22
            char r15 = r0.charAt(r8)
            r16 = 32
            r0 = r16
            if (r15 != r0) goto L_0x001b
            int r8 = r8 + 1
            goto L_0x0005
        L_0x001b:
            r15 = 59
            r0 = r22
            int r12 = r0.indexOf(r15, r8)
            r15 = 61
            r0 = r22
            int r6 = r0.indexOf(r15, r8)
            com.ali.user.open.cookies.LoginCookie r4 = new com.ali.user.open.cookies.LoginCookie
            r4.<init>()
            r15 = -1
            if (r12 == r15) goto L_0x0035
            if (r12 < r6) goto L_0x0038
        L_0x0035:
            r15 = -1
            if (r6 != r15) goto L_0x004e
        L_0x0038:
            r15 = -1
            if (r12 != r15) goto L_0x003c
            r12 = r10
        L_0x003c:
            r0 = r22
            java.lang.String r15 = r0.substring(r8, r12)
            r4.name = r15
            r15 = 0
            r4.value = r15
        L_0x0047:
            r8 = r12
        L_0x0048:
            if (r8 < 0) goto L_0x004c
            if (r8 < r10) goto L_0x00b7
        L_0x004c:
            r15 = r4
            goto L_0x000b
        L_0x004e:
            r0 = r22
            java.lang.String r15 = r0.substring(r8, r6)
            r4.name = r15
            int r15 = r10 + -1
            if (r6 >= r15) goto L_0x0077
            int r15 = r6 + 1
            r0 = r22
            char r15 = r0.charAt(r15)
            r16 = 34
            r0 = r16
            if (r15 != r0) goto L_0x0077
            r15 = 34
            int r16 = r6 + 2
            r0 = r22
            r1 = r16
            int r8 = r0.indexOf(r15, r1)
            r15 = -1
            if (r8 == r15) goto L_0x000a
        L_0x0077:
            r15 = 59
            r0 = r22
            int r12 = r0.indexOf(r15, r8)
            r15 = -1
            if (r12 != r15) goto L_0x0083
            r12 = r10
        L_0x0083:
            int r15 = r12 - r6
            r16 = 4096(0x1000, float:5.74E-42)
            r0 = r16
            if (r15 <= r0) goto L_0x00a0
            int r15 = r6 + 1
            int r16 = r6 + 1
            r0 = r16
            int r0 = r0 + 4096
            r16 = r0
            r0 = r22
            r1 = r16
            java.lang.String r15 = r0.substring(r15, r1)
            r4.value = r15
            goto L_0x0047
        L_0x00a0:
            int r15 = r6 + 1
            if (r15 == r12) goto L_0x00a6
            if (r12 >= r6) goto L_0x00ac
        L_0x00a6:
            java.lang.String r15 = ""
            r4.value = r15
            goto L_0x0047
        L_0x00ac:
            int r15 = r6 + 1
            r0 = r22
            java.lang.String r15 = r0.substring(r15, r12)
            r4.value = r15
            goto L_0x0047
        L_0x00b7:
            r0 = r22
            char r15 = r0.charAt(r8)
            r16 = 32
            r0 = r16
            if (r15 == r0) goto L_0x00cf
            r0 = r22
            char r15 = r0.charAt(r8)
            r16 = 59
            r0 = r16
            if (r15 != r0) goto L_0x00d3
        L_0x00cf:
            int r8 = r8 + 1
            goto L_0x0048
        L_0x00d3:
            r0 = r22
            char r15 = r0.charAt(r8)
            r16 = 44
            r0 = r16
            if (r15 != r0) goto L_0x00e3
            int r8 = r8 + 1
            goto L_0x004c
        L_0x00e3:
            int r15 = r10 - r8
            int r16 = SECURE_LENGTH
            r0 = r16
            if (r15 < r0) goto L_0x0115
            int r15 = SECURE_LENGTH
            int r15 = r15 + r8
            r0 = r22
            java.lang.String r15 = r0.substring(r8, r15)
            java.lang.String r16 = "secure"
            boolean r15 = r15.equalsIgnoreCase(r16)
            if (r15 == 0) goto L_0x0115
            int r15 = SECURE_LENGTH
            int r8 = r8 + r15
            r15 = 1
            r4.secure = r15
            if (r8 == r10) goto L_0x004c
            r0 = r22
            char r15 = r0.charAt(r8)
            r16 = 61
            r0 = r16
            if (r15 != r0) goto L_0x0048
            int r8 = r8 + 1
            goto L_0x0048
        L_0x0115:
            int r15 = r10 - r8
            int r16 = HTTP_ONLY_LENGTH
            r0 = r16
            if (r15 < r0) goto L_0x0147
            int r15 = HTTP_ONLY_LENGTH
            int r15 = r15 + r8
            r0 = r22
            java.lang.String r15 = r0.substring(r8, r15)
            java.lang.String r16 = "httponly"
            boolean r15 = r15.equalsIgnoreCase(r16)
            if (r15 == 0) goto L_0x0147
            int r15 = HTTP_ONLY_LENGTH
            int r8 = r8 + r15
            r15 = 1
            r4.httpOnly = r15
            if (r8 == r10) goto L_0x004c
            r0 = r22
            char r15 = r0.charAt(r8)
            r16 = 61
            r0 = r16
            if (r15 != r0) goto L_0x0048
            int r8 = r8 + 1
            goto L_0x0048
        L_0x0147:
            int r15 = r10 - r8
            int r16 = DISCARD_LENGTH
            r0 = r16
            if (r15 < r0) goto L_0x0179
            int r15 = DISCARD_LENGTH
            int r15 = r15 + r8
            r0 = r22
            java.lang.String r15 = r0.substring(r8, r15)
            java.lang.String r16 = "discard"
            boolean r15 = r15.equalsIgnoreCase(r16)
            if (r15 == 0) goto L_0x0179
            int r15 = DISCARD_LENGTH
            int r8 = r8 + r15
            r15 = 1
            r4.discard = r15
            if (r8 == r10) goto L_0x004c
            r0 = r22
            char r15 = r0.charAt(r8)
            r16 = 61
            r0 = r16
            if (r15 != r0) goto L_0x0048
            int r8 = r8 + 1
            goto L_0x0048
        L_0x0179:
            r15 = 61
            r0 = r22
            int r6 = r0.indexOf(r15, r8)
            if (r6 <= 0) goto L_0x02e5
            r0 = r22
            java.lang.String r15 = r0.substring(r8, r6)
            java.lang.String r11 = r15.toLowerCase()
            java.lang.String r15 = "expires"
            boolean r15 = r11.equals(r15)
            if (r15 == 0) goto L_0x01ab
            r15 = 44
            r0 = r22
            int r2 = r0.indexOf(r15, r6)
            r15 = -1
            if (r2 == r15) goto L_0x01ab
            int r15 = r2 - r6
            r16 = 10
            r0 = r16
            if (r15 > r0) goto L_0x01ab
            int r8 = r2 + 1
        L_0x01ab:
            r15 = 59
            r0 = r22
            int r12 = r0.indexOf(r15, r8)
            r15 = 44
            r0 = r22
            int r3 = r0.indexOf(r15, r8)
            r15 = -1
            if (r12 != r15) goto L_0x0228
            r15 = -1
            if (r3 != r15) goto L_0x0228
            r8 = r10
        L_0x01c2:
            int r15 = r6 + 1
            r0 = r22
            java.lang.String r14 = r0.substring(r15, r8)     // Catch:{ Throwable -> 0x0222 }
            int r15 = r14.length()     // Catch:{ Throwable -> 0x0222 }
            r16 = 2
            r0 = r16
            if (r15 <= r0) goto L_0x01ee
            r15 = 0
            char r15 = r14.charAt(r15)     // Catch:{ Throwable -> 0x0222 }
            r16 = 34
            r0 = r16
            if (r15 != r0) goto L_0x01ee
            r15 = 34
            r16 = 1
            int r5 = r14.indexOf(r15, r16)     // Catch:{ Throwable -> 0x0222 }
            if (r5 <= 0) goto L_0x01ee
            r15 = 1
            java.lang.String r14 = r14.substring(r15, r5)     // Catch:{ Throwable -> 0x0222 }
        L_0x01ee:
            java.lang.String r15 = "expires"
            boolean r15 = r11.equals(r15)     // Catch:{ Throwable -> 0x0222 }
            if (r15 == 0) goto L_0x0237
            long r16 = com.ali.user.open.cookies.HttpDateTime.parse(r14)     // Catch:{ IllegalArgumentException -> 0x0201 }
            r0 = r16
            r4.expires = r0     // Catch:{ IllegalArgumentException -> 0x0201 }
            goto L_0x0048
        L_0x0201:
            r7 = move-exception
            java.lang.String r15 = "login.LoginCookieUtils"
            java.lang.StringBuilder r16 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0222 }
            r16.<init>()     // Catch:{ Throwable -> 0x0222 }
            java.lang.String r17 = "illegal format for expires: "
            java.lang.StringBuilder r16 = r16.append(r17)     // Catch:{ Throwable -> 0x0222 }
            r0 = r16
            java.lang.StringBuilder r16 = r0.append(r14)     // Catch:{ Throwable -> 0x0222 }
            java.lang.String r16 = r16.toString()     // Catch:{ Throwable -> 0x0222 }
            r0 = r16
            com.ali.user.open.core.trace.SDKLogger.e(r15, r0, r7)     // Catch:{ Throwable -> 0x0222 }
            goto L_0x0048
        L_0x0222:
            r13 = move-exception
            r13.printStackTrace()
            goto L_0x0048
        L_0x0228:
            r15 = -1
            if (r12 != r15) goto L_0x022d
            r8 = r3
            goto L_0x01c2
        L_0x022d:
            r15 = -1
            if (r3 != r15) goto L_0x0232
            r8 = r12
            goto L_0x01c2
        L_0x0232:
            int r8 = java.lang.Math.min(r12, r3)
            goto L_0x01c2
        L_0x0237:
            java.lang.String r15 = "max-age"
            boolean r15 = r11.equals(r15)     // Catch:{ Throwable -> 0x0222 }
            if (r15 == 0) goto L_0x0275
            long r16 = java.lang.System.currentTimeMillis()     // Catch:{ NumberFormatException -> 0x0254 }
            r18 = 1000(0x3e8, double:4.94E-321)
            long r20 = java.lang.Long.parseLong(r14)     // Catch:{ NumberFormatException -> 0x0254 }
            long r18 = r18 * r20
            long r16 = r16 + r18
            r0 = r16
            r4.expires = r0     // Catch:{ NumberFormatException -> 0x0254 }
            goto L_0x0048
        L_0x0254:
            r7 = move-exception
            java.lang.String r15 = "login.LoginCookieUtils"
            java.lang.StringBuilder r16 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0222 }
            r16.<init>()     // Catch:{ Throwable -> 0x0222 }
            java.lang.String r17 = "illegal format for max-age: "
            java.lang.StringBuilder r16 = r16.append(r17)     // Catch:{ Throwable -> 0x0222 }
            r0 = r16
            java.lang.StringBuilder r16 = r0.append(r14)     // Catch:{ Throwable -> 0x0222 }
            java.lang.String r16 = r16.toString()     // Catch:{ Throwable -> 0x0222 }
            r0 = r16
            com.ali.user.open.core.trace.SDKLogger.e(r15, r0, r7)     // Catch:{ Throwable -> 0x0222 }
            goto L_0x0048
        L_0x0275:
            java.lang.String r15 = "path"
            boolean r15 = r11.equals(r15)     // Catch:{ Throwable -> 0x0222 }
            if (r15 == 0) goto L_0x0288
            int r15 = r14.length()     // Catch:{ Throwable -> 0x0222 }
            if (r15 <= 0) goto L_0x0048
            r4.path = r14     // Catch:{ Throwable -> 0x0222 }
            goto L_0x0048
        L_0x0288:
            java.lang.String r15 = "domain"
            boolean r15 = r11.equals(r15)     // Catch:{ Throwable -> 0x0222 }
            if (r15 == 0) goto L_0x02d2
            r15 = 46
            int r9 = r14.lastIndexOf(r15)     // Catch:{ Throwable -> 0x0222 }
            if (r9 != 0) goto L_0x029e
            r15 = 0
            r4.domain = r15     // Catch:{ Throwable -> 0x0222 }
            goto L_0x0048
        L_0x029e:
            int r15 = r9 + 1
            java.lang.String r15 = r14.substring(r15)     // Catch:{ NumberFormatException -> 0x02a9 }
            java.lang.Integer.parseInt(r15)     // Catch:{ NumberFormatException -> 0x02a9 }
            goto L_0x0048
        L_0x02a9:
            r15 = move-exception
            java.lang.String r14 = r14.toLowerCase()     // Catch:{ Throwable -> 0x0222 }
            r15 = 0
            char r15 = r14.charAt(r15)     // Catch:{ Throwable -> 0x0222 }
            r16 = 46
            r0 = r16
            if (r15 == r0) goto L_0x02ce
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0222 }
            r15.<init>()     // Catch:{ Throwable -> 0x0222 }
            r16 = 46
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ Throwable -> 0x0222 }
            java.lang.StringBuilder r15 = r15.append(r14)     // Catch:{ Throwable -> 0x0222 }
            java.lang.String r14 = r15.toString()     // Catch:{ Throwable -> 0x0222 }
            int r9 = r9 + 1
        L_0x02ce:
            r4.domain = r14     // Catch:{ Throwable -> 0x0222 }
            goto L_0x0048
        L_0x02d2:
            java.lang.String r15 = "version"
            boolean r15 = r11.equals(r15)     // Catch:{ Throwable -> 0x0222 }
            if (r15 == 0) goto L_0x0048
            int r15 = r14.length()     // Catch:{ Throwable -> 0x0222 }
            if (r15 <= 0) goto L_0x0048
            r4.version = r14     // Catch:{ Throwable -> 0x0222 }
            goto L_0x0048
        L_0x02e5:
            r8 = r10
            goto L_0x0048
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.cookies.LoginCookieUtils.parseCookie(java.lang.String):com.ali.user.open.cookies.LoginCookie");
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
