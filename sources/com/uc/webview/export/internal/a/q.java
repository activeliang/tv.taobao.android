package com.uc.webview.export.internal.a;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebViewClient;
import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.WebView;
import com.uc.webview.export.internal.c.a.a;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.Utils;
import java.util.Date;
import java.util.HashMap;

/* compiled from: ProGuard */
public final class q extends WebViewClient {
    private WebView a;
    private com.uc.webview.export.WebViewClient b;

    public q(WebView webView, com.uc.webview.export.WebViewClient webViewClient) {
        this.a = webView;
        this.b = webViewClient;
    }

    public final boolean shouldOverrideUrlLoading(android.webkit.WebView webView, String str) {
        return this.b.shouldOverrideUrlLoading(this.a, str);
    }

    public final void onPageStarted(android.webkit.WebView webView, String str, Bitmap bitmap) {
        this.b.onPageStarted(this.a, str, bitmap);
    }

    public final void onPageFinished(android.webkit.WebView webView, String str) {
        boolean z;
        a.C0009a aVar;
        if (a.a == null && d.e != null) {
            a.a(d.e);
        }
        a aVar2 = a.a;
        if (((Boolean) d.a(10006, "stat", true)).booleanValue() && !d.f) {
            if (str == null || str.trim().length() == 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                String lowerCase = str.toLowerCase();
                if (lowerCase.startsWith("http://") || lowerCase.startsWith("https://")) {
                    if (Utils.sWAPrintLog) {
                        Log.d("SDKWaStat", "statPV:" + lowerCase);
                    }
                    if (((Boolean) d.a(10006, "stat", true)).booleanValue() && !d.f) {
                        Date date = new Date(System.currentTimeMillis());
                        int intValue = ((Boolean) d.a(10010, new Object[0])).booleanValue() ? ((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue() : 0;
                        if (!(intValue == 2 || intValue == 0)) {
                            intValue = (intValue * 10) + d.l;
                        }
                        String str2 = aVar2.f.format(date) + "~" + intValue;
                        synchronized (aVar2.h) {
                            if (aVar2.d == null) {
                                aVar2.d = new HashMap();
                            }
                            a.C0009a aVar3 = aVar2.d.get(str2);
                            if (aVar3 == null) {
                                a.C0009a aVar4 = new a.C0009a(aVar2, (byte) 0);
                                aVar2.d.put(str2, aVar4);
                                aVar = aVar4;
                            } else {
                                aVar = aVar3;
                            }
                            aVar.b.put("tm", aVar2.g.format(date));
                            Integer num = aVar.a.get("sum_pv");
                            if (num == null) {
                                aVar.a.put("sum_pv", 1);
                            } else {
                                aVar.a.put("sum_pv", Integer.valueOf(num.intValue() + 1));
                            }
                        }
                    }
                }
            }
        }
        this.b.onPageFinished(this.a, str);
    }

    public final void onLoadResource(android.webkit.WebView webView, String str) {
        this.b.onLoadResource(this.a, str);
    }

    @TargetApi(11)
    public final WebResourceResponse shouldInterceptRequest(android.webkit.WebView webView, String str) {
        com.uc.webview.export.WebResourceResponse shouldInterceptRequest = this.b.shouldInterceptRequest(this.a, str);
        if (shouldInterceptRequest == null) {
            return null;
        }
        return new WebResourceResponse(shouldInterceptRequest.getMimeType(), shouldInterceptRequest.getEncoding(), shouldInterceptRequest.getData());
    }

    public final void onReceivedError(android.webkit.WebView webView, int i, String str, String str2) {
        this.b.onReceivedError(this.a, i, str, str2);
    }

    public final void onFormResubmission(android.webkit.WebView webView, Message message, Message message2) {
        this.b.onFormResubmission(this.a, message, message2);
    }

    public final void doUpdateVisitedHistory(android.webkit.WebView webView, String str, boolean z) {
        this.b.doUpdateVisitedHistory(this.a, str, z);
    }

    public final void onReceivedSslError(android.webkit.WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        this.b.onReceivedSslError(this.a, new h(sslErrorHandler), sslError);
    }

    public final void onReceivedHttpAuthRequest(android.webkit.WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
        if (this.b != null) {
            this.b.onReceivedHttpAuthRequest(this.a, new d(httpAuthHandler), str, str2);
        } else {
            httpAuthHandler.cancel();
        }
    }

    public final boolean shouldOverrideKeyEvent(android.webkit.WebView webView, KeyEvent keyEvent) {
        return this.b.shouldOverrideKeyEvent(this.a, keyEvent);
    }

    public final void onUnhandledKeyEvent(android.webkit.WebView webView, KeyEvent keyEvent) {
        this.b.onUnhandledKeyEvent(this.a, keyEvent);
    }

    public final void onScaleChanged(android.webkit.WebView webView, float f, float f2) {
        this.b.onScaleChanged(this.a, f, f2);
    }
}
