package com.ali.user.open.cookies;

import com.ali.user.open.core.WebViewProxy;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.webview.DefaultWebViewProxy;

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
