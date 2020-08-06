package com.ali.user.open.cookies;

import android.text.TextUtils;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.FileUtils;
import java.util.ArrayList;
import java.util.List;

public class CookieManagerWrapper {
    public static final CookieManagerWrapper INSTANCE = new CookieManagerWrapper();
    private static final String TAG = CookieManagerWrapper.class.getSimpleName();
    private String[] mCookies;

    private CookieManagerWrapper() {
    }

    public synchronized void refreshCookie(String domain) {
        if (this.mCookies == null) {
            try {
                String cookieData = FileUtils.readFileData(KernelContext.getApplicationContext(), "cookies");
                if (!TextUtils.isEmpty(cookieData)) {
                    SDKLogger.d(TAG, "get cookie from storage:" + cookieData);
                    this.mCookies = TextUtils.split(cookieData, "\u0005");
                }
            } catch (Throwable th) {
            }
        }
        if (this.mCookies != null) {
            injectCookie(domain, this.mCookies, (String[]) null);
        }
    }

    public synchronized void injectCookie(String domain, String[] cookies, String[] ssoDomainList) {
        this.mCookies = cookies;
        if (KernelContext.getApplicationContext() != null) {
            if (cookies != null) {
                SDKLogger.d(TAG, "injectCookie cookies != null");
                List<LoginCookie> taobaoCookies = new ArrayList<>();
                for (String cookie : cookies) {
                    if (!TextUtils.isEmpty(cookie)) {
                        try {
                            LoginCookie cook = LoginCookieUtils.parseCookie(cookie);
                            String url = LoginCookieUtils.getHttpDomin(cook);
                            String cookieStr = cook.toString();
                            SDKLogger.d(TAG, "add cookie: " + cookieStr);
                            CookieManagerService.getWebViewProxy().setCookie(url, cookieStr);
                            if (TextUtils.equals(cook.domain, domain)) {
                                taobaoCookies.add(cook);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (ssoDomainList != null) {
                    if (ssoDomainList.length > 0 && !taobaoCookies.isEmpty()) {
                        for (LoginCookie cookie2 : taobaoCookies) {
                            String originDomain = cookie2.domain;
                            for (String ssoDomain : ssoDomainList) {
                                cookie2.domain = ssoDomain;
                                String url2 = LoginCookieUtils.getHttpDomin(cookie2);
                                String cookieStr2 = cookie2.toString();
                                SDKLogger.d(TAG, "add cookies to domain:" + ssoDomain + ", cookie = " + cookieStr2);
                                CookieManagerService.getWebViewProxy().setCookie(url2, cookieStr2);
                            }
                            cookie2.domain = originDomain;
                        }
                    }
                }
                CookieManagerService.getWebViewProxy().flush();
                if (this.mCookies != null) {
                    FileUtils.writeFileData(KernelContext.getApplicationContext(), "cookies", TextUtils.join("\u0005", cookies));
                }
            } else {
                clearCookies();
            }
        }
    }

    /* JADX WARNING: type inference failed for: r14v32, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clearCookies() {
        /*
            r19 = this;
            java.lang.String r14 = "clearCookies"
            java.lang.String r15 = "into clearCookies "
            com.ali.user.open.core.trace.SDKLogger.e(r14, r15)
            android.content.Context r14 = com.ali.user.open.core.context.KernelContext.getApplicationContext()
            android.webkit.CookieSyncManager r5 = android.webkit.CookieSyncManager.createInstance(r14)
            java.lang.String r14 = "clearCookies"
            java.lang.String r15 = "into clearCookies removeSessionCookie finish"
            com.ali.user.open.core.trace.SDKLogger.e(r14, r15)
            r0 = r19
            java.lang.String[] r14 = r0.mCookies
            if (r14 != 0) goto L_0x0055
            android.content.Context r14 = com.ali.user.open.core.context.KernelContext.getApplicationContext()
            java.lang.String r15 = "cookies"
            java.lang.String r4 = com.ali.user.open.core.util.FileUtils.readFileData(r14, r15)
            boolean r14 = android.text.TextUtils.isEmpty(r4)
            if (r14 != 0) goto L_0x0055
            java.lang.String r14 = TAG
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "get cookie from storage:"
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.StringBuilder r15 = r15.append(r4)
            java.lang.String r15 = r15.toString()
            com.ali.user.open.core.trace.SDKLogger.d(r14, r15)
            java.lang.String r14 = "\u0005"
            java.lang.String[] r14 = android.text.TextUtils.split(r4, r14)
            r0 = r19
            r0.mCookies = r14
        L_0x0055:
            java.lang.String r14 = "clearCookies"
            java.lang.String r15 = "into clearCookies readFileData finish"
            com.ali.user.open.core.trace.SDKLogger.e(r14, r15)
            r0 = r19
            java.lang.String[] r14 = r0.mCookies
            if (r14 == 0) goto L_0x015e
            java.util.ArrayList r12 = new java.util.ArrayList
            r12.<init>()
            r0 = r19
            java.lang.String[] r15 = r0.mCookies
            int r0 = r15.length
            r16 = r0
            r14 = 0
        L_0x0071:
            r0 = r16
            if (r14 >= r0) goto L_0x00bd
            r3 = r15[r14]
            boolean r17 = android.text.TextUtils.isEmpty(r3)
            if (r17 != 0) goto L_0x008e
            com.ali.user.open.cookies.LoginCookie r2 = com.ali.user.open.cookies.LoginCookieUtils.parseCookie(r3)     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r17 = "munb"
            java.lang.String r0 = r2.name     // Catch:{ Throwable -> 0x00b8 }
            r18 = r0
            boolean r17 = r17.equals(r18)     // Catch:{ Throwable -> 0x00b8 }
            if (r17 == 0) goto L_0x0091
        L_0x008e:
            int r14 = r14 + 1
            goto L_0x0071
        L_0x0091:
            java.lang.String r13 = com.ali.user.open.cookies.LoginCookieUtils.getHttpDomin(r2)     // Catch:{ Throwable -> 0x00b8 }
            com.ali.user.open.cookies.LoginCookieUtils.expiresCookies(r2)     // Catch:{ Throwable -> 0x00b8 }
            com.ali.user.open.core.WebViewProxy r17 = com.ali.user.open.cookies.CookieManagerService.getWebViewProxy()     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r18 = r2.toString()     // Catch:{ Throwable -> 0x00b8 }
            r0 = r17
            r1 = r18
            r0.setCookie(r13, r1)     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r0 = r2.domain     // Catch:{ Throwable -> 0x00b8 }
            r17 = r0
            java.lang.String r18 = ".taobao.com"
            boolean r17 = android.text.TextUtils.equals(r17, r18)     // Catch:{ Throwable -> 0x00b8 }
            if (r17 == 0) goto L_0x008e
            r12.add(r2)     // Catch:{ Throwable -> 0x00b8 }
            goto L_0x008e
        L_0x00b8:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x008e
        L_0x00bd:
            r11 = 0
            com.ali.user.open.service.impl.SessionManager r14 = com.ali.user.open.service.impl.SessionManager.INSTANCE     // Catch:{ Exception -> 0x0123 }
            com.ali.user.open.session.InternalSession r14 = r14.getInternalSession()     // Catch:{ Exception -> 0x0123 }
            java.util.Map<java.lang.String, java.lang.Object> r14 = r14.otherInfo     // Catch:{ Exception -> 0x0123 }
            if (r14 == 0) goto L_0x00ec
            com.ali.user.open.service.impl.SessionManager r14 = com.ali.user.open.service.impl.SessionManager.INSTANCE     // Catch:{ Exception -> 0x0123 }
            com.ali.user.open.session.InternalSession r14 = r14.getInternalSession()     // Catch:{ Exception -> 0x0123 }
            java.util.Map<java.lang.String, java.lang.Object> r14 = r14.otherInfo     // Catch:{ Exception -> 0x0123 }
            java.lang.String r15 = "ssoDomainList"
            java.lang.Object r8 = r14.get(r15)     // Catch:{ Exception -> 0x0123 }
            if (r8 == 0) goto L_0x00ec
            boolean r14 = r8 instanceof java.util.ArrayList     // Catch:{ Exception -> 0x0123 }
            if (r14 == 0) goto L_0x00ec
            r0 = r8
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Exception -> 0x0123 }
            r7 = r0
            r14 = 0
            java.lang.String[] r14 = new java.lang.String[r14]     // Catch:{ Exception -> 0x0123 }
            java.lang.Object[] r14 = r7.toArray(r14)     // Catch:{ Exception -> 0x0123 }
            r0 = r14
            java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ Exception -> 0x0123 }
            r11 = r0
        L_0x00ec:
            if (r11 == 0) goto L_0x0144
            int r14 = r11.length
            if (r14 <= 0) goto L_0x0144
            boolean r14 = r12.isEmpty()
            if (r14 != 0) goto L_0x0144
            java.util.Iterator r15 = r12.iterator()
        L_0x00fb:
            boolean r14 = r15.hasNext()
            if (r14 == 0) goto L_0x0144
            java.lang.Object r3 = r15.next()
            com.ali.user.open.cookies.LoginCookie r3 = (com.ali.user.open.cookies.LoginCookie) r3
            java.lang.String r9 = r3.domain
            int r0 = r11.length
            r16 = r0
            r14 = 0
        L_0x010d:
            r0 = r16
            if (r14 >= r0) goto L_0x0141
            r10 = r11[r14]
            java.lang.String r17 = "munb"
            java.lang.String r0 = r3.name
            r18 = r0
            boolean r17 = r17.equals(r18)
            if (r17 == 0) goto L_0x0128
        L_0x0120:
            int r14 = r14 + 1
            goto L_0x010d
        L_0x0123:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x00ec
        L_0x0128:
            r3.domain = r10
            java.lang.String r13 = com.ali.user.open.cookies.LoginCookieUtils.getHttpDomin(r3)
            com.ali.user.open.cookies.LoginCookieUtils.expiresCookies(r3)
            com.ali.user.open.core.WebViewProxy r17 = com.ali.user.open.cookies.CookieManagerService.getWebViewProxy()
            java.lang.String r18 = r3.toString()
            r0 = r17
            r1 = r18
            r0.setCookie(r13, r1)
            goto L_0x0120
        L_0x0141:
            r3.domain = r9
            goto L_0x00fb
        L_0x0144:
            java.lang.String r14 = TAG
            java.lang.String r15 = "injectCookie cookies is null"
            com.ali.user.open.core.trace.SDKLogger.d(r14, r15)
            r14 = 0
            r0 = r19
            r0.mCookies = r14
            android.content.Context r14 = com.ali.user.open.core.context.KernelContext.getApplicationContext()
            java.lang.String r15 = "cookies"
            java.lang.String r16 = ""
            com.ali.user.open.core.util.FileUtils.writeFileData(r14, r15, r16)
        L_0x015e:
            com.ali.user.open.core.WebViewProxy r14 = com.ali.user.open.cookies.CookieManagerService.getWebViewProxy()
            java.lang.String r15 = "http://taobao.com"
            java.lang.String r16 = "cookie2=;Domain=.taobao.com;Path=\\/;Expires=Thu, 01-Oct-2000 00:00:00 GMT;HttpOnly"
            r14.setCookie(r15, r16)
            java.lang.String r14 = "clearCookies"
            java.lang.String r15 = "into clearCookies reset cookie finish"
            com.ali.user.open.core.trace.SDKLogger.e(r14, r15)
            com.ali.user.open.core.WebViewProxy r14 = com.ali.user.open.cookies.CookieManagerService.getWebViewProxy()
            r14.removeExpiredCookie()
            java.lang.String r14 = "clearCookies"
            java.lang.String r15 = "into clearCookies removeExpiredCookie finish"
            com.ali.user.open.core.trace.SDKLogger.e(r14, r15)
            com.ali.user.open.core.WebViewProxy r14 = com.ali.user.open.cookies.CookieManagerService.getWebViewProxy()
            r14.flush()
            java.lang.String r14 = "clearCookies"
            java.lang.String r15 = "into clearCookies  finish"
            com.ali.user.open.core.trace.SDKLogger.e(r14, r15)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.cookies.CookieManagerWrapper.clearCookies():void");
    }
}
