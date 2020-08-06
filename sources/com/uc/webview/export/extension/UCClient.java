package com.uc.webview.export.extension;

import android.webkit.ValueCallback;
import com.uc.webview.export.WebResourceRequest;
import com.uc.webview.export.WebView;
import com.uc.webview.export.annotations.Api;
import java.util.HashMap;

@Api
/* compiled from: ProGuard */
public class UCClient {
    public static final int FORM_PROMPT_TYPE_COVER = 1;
    public static final int FORM_PROMPT_TYPE_SAVE = 0;
    public static final int WEBVIEW_EVENT_TYPE_EMPTY_SCREEN = 9;
    public static final int WIFI_POLICY_CONTINUE = 0;
    public static final int WIFI_POLICY_INTERRUP = 1;
    public static final int WIFI_POLICY_USE_FOXY_SERVER = 2;

    @Api
    /* compiled from: ProGuard */
    public static class MoveCursorToTextInputResult {
        public boolean mCanMoveToNext;
        public boolean mCanMoveToPrevious;
        public boolean mSuccess;
    }

    public void onFirstLayoutFinished(boolean z, String str) {
    }

    public void onWifiSafePolicy(WebView webView, IGenenalSyncResult iGenenalSyncResult) {
        iGenenalSyncResult.setResult(0);
        iGenenalSyncResult.wakeUp();
    }

    public void onFirstVisuallyNonEmptyDraw() {
    }

    public void onMoveCursorToTextInput(MoveCursorToTextInputResult moveCursorToTextInputResult) {
    }

    public void onReceivedDispatchResponse(HashMap<String, String> hashMap) {
    }

    public boolean onWillInterceptResponse(HashMap<String, String> hashMap) {
        return false;
    }

    public WebResourceRequest onWillSendRequest(WebResourceRequest webResourceRequest) {
        return null;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str, int i) {
        return false;
    }

    public void onResourceDidFinishLoading(String str, long j) {
    }

    public void onWebViewEvent(WebView webView, int i, Object obj) {
    }

    public void onPrereadFinished(WebView webView, String str, boolean z) {
    }

    public void onPrereadPageOpened(WebView webView, String str) {
    }

    public void onSaveFormDataPrompt(int i, ValueCallback<Boolean> valueCallback) {
    }
}
