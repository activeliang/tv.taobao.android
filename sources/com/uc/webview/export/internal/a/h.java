package com.uc.webview.export.internal.a;

import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.annotations.Interface;

@Interface
/* compiled from: ProGuard */
final class h extends SslErrorHandler {
    h(android.webkit.SslErrorHandler sslErrorHandler) {
        this.mHandler = sslErrorHandler;
    }
}
