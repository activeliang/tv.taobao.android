package com.ali.auth.third.core.cookies;

import com.ali.auth.third.core.WebViewProxy;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.webview.DefaultWebViewProxy;

public class CookieManagerService {
    private CookieManagerService() {
    }

    public static WebViewProxy getWebViewProxy() {
        if (KernelContext.mWebViewProxy != null) {
            return KernelContext.mWebViewProxy;
        }
        return DefaultWebViewProxy.getInstance();
    }
}
