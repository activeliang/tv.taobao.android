package com.uc.webview.export.internal.interfaces;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import com.uc.webview.export.DownloadListener;
import com.uc.webview.export.WebBackForwardList;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import com.uc.webview.export.WebViewClient;
import com.uc.webview.export.annotations.Interface;
import java.util.Map;

@Interface
/* compiled from: ProGuard */
public interface IWebView extends ICommonExtension {

    @Interface
    /* compiled from: ProGuard */
    public interface IHitTestResult {
        String getExtra();

        int getType();
    }

    @Interface
    /* compiled from: ProGuard */
    public interface IWebViewTransport {
        IWebView getWebView();

        void setWebView(IWebView iWebView);
    }

    void addJavascriptInterface(Object obj, String str);

    boolean canGoBack();

    boolean canGoBackOrForward(int i);

    boolean canGoForward();

    @Deprecated
    boolean canZoomIn();

    @Deprecated
    boolean canZoomOut();

    @Deprecated
    Picture capturePicture();

    void clearCache(boolean z);

    void clearClientCertPreferences(Runnable runnable);

    void clearFormData();

    void clearHistory();

    void clearMatches();

    void clearSslPreferences();

    WebBackForwardList copyBackForwardListInner();

    void destroy();

    boolean dispatchKeyEvent(KeyEvent keyEvent);

    void documentHasImages(Message message);

    void evaluateJavascript(String str, ValueCallback<String> valueCallback);

    @Deprecated
    int findAll(String str);

    void findAllAsync(String str);

    void findNext(boolean z);

    void flingScroll(int i, int i2);

    SslCertificate getCertificate();

    ICommonExtension getCommonExtension();

    int getContentHeight();

    Bitmap getFavicon();

    IHitTestResult getHitTestResultInner();

    String[] getHttpAuthUsernamePassword(String str, String str2);

    String getOriginalUrl();

    IWebViewOverride getOverrideObject();

    int getProgress();

    float getScale();

    WebSettings getSettingsInner();

    String getTitle();

    IUCExtension getUCExtension();

    String getUrl();

    View getView();

    void goBack();

    void goBackOrForward(int i);

    void goForward();

    void invokeZoomPicker();

    boolean isHorizontalScrollBarEnabled();

    boolean isInOverScrollMoving();

    boolean isVerticalScrollBarEnabled();

    void loadData(String str, String str2, String str3);

    void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5);

    void loadUrl(String str);

    void loadUrl(String str, Map<String, String> map);

    void notifyForegroundChanged(boolean z);

    void onPause();

    void onResume();

    boolean overlayHorizontalScrollbar();

    boolean overlayVerticalScrollbar();

    boolean pageDown(boolean z);

    boolean pageUp(boolean z);

    void pauseTimers();

    void postUrl(String str, byte[] bArr);

    void reload();

    void removeJavascriptInterface(String str);

    void requestFocusNodeHref(Message message);

    void requestImageRef(Message message);

    WebBackForwardList restoreStateInner(Bundle bundle);

    void resumeTimers();

    WebBackForwardList saveStateInner(Bundle bundle);

    void setBackgroundColor(int i);

    void setDownloadListener(DownloadListener downloadListener);

    void setDropDownOverScrollEnabled(boolean z);

    void setFindListener(WebView.FindListener findListener);

    void setHorizontalScrollBarEnabled(boolean z);

    void setHttpAuthUsernamePassword(String str, String str2, String str3, String str4);

    void setInitialScale(int i);

    void setLayerType(int i, Paint paint);

    void setLayoutParams(ViewGroup.LayoutParams layoutParams);

    void setNetworkAvailable(boolean z);

    void setOnKeyListener(View.OnKeyListener onKeyListener);

    void setOnLongClickListener(View.OnLongClickListener onLongClickListener);

    void setOnTouchListener(View.OnTouchListener onTouchListener);

    void setOverScrollMode(int i);

    void setOverrideObject(IWebViewOverride iWebViewOverride);

    void setScrollBarStyle(int i);

    void setVerticalScrollBarEnabled(boolean z);

    void setVerticalScrollbarOverlay(boolean z);

    void setWebChromeClient(WebChromeClient webChromeClient);

    void setWebViewClient(WebViewClient webViewClient);

    void stopLoading();

    void superComputeScroll();

    void superDestroy();

    boolean superDispatchTouchEvent(MotionEvent motionEvent);

    void superDraw(Canvas canvas);

    void superOnConfigurationChanged(Configuration configuration);

    void superOnScrollChanged(int i, int i2, int i3, int i4);

    void superOnVisibilityChanged(View view, int i);

    boolean superOverScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z);

    void superRequestLayout();

    void superSetVisibility(int i);

    boolean zoomIn();

    boolean zoomOut();
}
