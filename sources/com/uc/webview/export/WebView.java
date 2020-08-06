package com.uc.webview.export;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.net.http.SslCertificate;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.extension.CommonExtension;
import com.uc.webview.export.extension.UCExtension;
import com.uc.webview.export.internal.c;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWebResourceInternal;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.interfaces.IWebViewOverride;
import com.uc.webview.export.internal.interfaces.InvokeObject;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Api
/* compiled from: ProGuard */
public class WebView extends FrameLayout implements IWebViewOverride {
    public static final int CORE_TYPE_ANDROID = 2;
    public static final int CORE_TYPE_U3 = 1;
    public static final int CORE_TYPE_U4 = 3;
    public static final int DEFAULT_CORE_TYPE = 1;
    private WebSettings a;
    private c b;
    private CommonExtension c;
    private UCExtension d;
    private boolean e;
    private int f;
    public IWebView mWebView;

    @Api
    /* compiled from: ProGuard */
    public interface FindListener {
        void onFindResultReceived(int i, int i2, boolean z);
    }

    public CommonExtension getCommonExtension() {
        return this.c;
    }

    public UCExtension getUCExtension() {
        return this.d;
    }

    @Api
    /* compiled from: ProGuard */
    public class WebViewTransport {
        private WebView b;

        public WebViewTransport() {
        }

        public synchronized void setWebView(WebView webView) {
            this.b = webView;
        }

        public synchronized WebView getWebView() {
            return this.b;
        }
    }

    @Api
    /* compiled from: ProGuard */
    public class HitTestResult {
        @Deprecated
        public static final int ANCHOR_TYPE = 1;
        public static final int EDIT_TEXT_TYPE = 9;
        public static final int EMAIL_TYPE = 4;
        public static final int GEO_TYPE = 3;
        @Deprecated
        public static final int IMAGE_ANCHOR_TYPE = 6;
        public static final int IMAGE_TYPE = 5;
        public static final int PHONE_TYPE = 2;
        public static final int SRC_ANCHOR_TYPE = 7;
        public static final int SRC_IMAGE_ANCHOR_TYPE = 8;
        public static final int UNKNOWN_TYPE = 0;
        private IWebView.IHitTestResult a;

        /* synthetic */ HitTestResult(WebView webView, IWebView.IHitTestResult iHitTestResult, byte b2) {
            this(iHitTestResult);
        }

        public HitTestResult() {
        }

        private HitTestResult(IWebView.IHitTestResult iHitTestResult) {
            this.a = iHitTestResult;
        }

        public int getType() {
            return this.a.getType();
        }

        public String getExtra() {
            return this.a.getExtra();
        }
    }

    public WebView(Context context) {
        this(context, (AttributeSet) null, 16842885, false, (byte) 0);
    }

    public WebView(Context context, boolean z) {
        this(context, (AttributeSet) null, 16842885, z, (byte) 0);
    }

    public WebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842885, false, (byte) 0);
    }

    public WebView(Context context, AttributeSet attributeSet, boolean z) {
        this(context, attributeSet, 16842885, z, (byte) 0);
    }

    public WebView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, false, (byte) 0);
    }

    public WebView(Context context, AttributeSet attributeSet, boolean z, int i) {
        this(context, attributeSet, i, z, (byte) 0);
    }

    @Deprecated
    public WebView(Context context, AttributeSet attributeSet, int i, boolean z) {
        this(context, attributeSet, i, false, (byte) 0);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private WebView(android.content.Context r10, android.util.AttributeSet r11, int r12, boolean r13, byte r14) {
        /*
            r9 = this;
            r8 = 3
            r7 = -1
            r6 = 2
            r5 = 1
            r4 = 0
            r9.<init>(r10, r11, r12)
            r0 = 0
            r9.a = r0
            r0 = 0
            r9.b = r0
            r9.e = r4
            if (r10 != 0) goto L_0x001b
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Invalid context argument"
            r0.<init>(r1)
            throw r0
        L_0x001b:
            int[] r1 = new int[r5]
            r0 = 10012(0x271c, float:1.403E-41)
            r2 = 5
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r4] = r10
            r2[r5] = r11
            r2[r6] = r9
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r13)
            r2[r8] = r3
            r3 = 4
            r2[r3] = r1
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r2)
            com.uc.webview.export.internal.interfaces.IWebView r0 = (com.uc.webview.export.internal.interfaces.IWebView) r0
            r9.mWebView = r0
            r0 = r1[r4]
            r9.f = r0
            r0 = 10014(0x271e, float:1.4033E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            int r2 = r9.f
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r1[r4] = r2
            android.content.Context r2 = r10.getApplicationContext()
            r1[r5] = r2
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r1)
            com.uc.webview.export.internal.c r0 = (com.uc.webview.export.internal.c) r0
            r9.b = r0
            com.uc.webview.export.internal.interfaces.IWebView r0 = r9.mWebView
            r0.setOverrideObject(r9)
            com.uc.webview.export.internal.interfaces.IWebView r0 = r9.mWebView
            com.uc.webview.export.WebSettings r0 = r0.getSettingsInner()
            r9.a = r0
            if (r11 != 0) goto L_0x00a6
            android.widget.FrameLayout$LayoutParams r0 = new android.widget.FrameLayout$LayoutParams
            r0.<init>(r7, r7)
            com.uc.webview.export.internal.interfaces.IWebView r1 = r9.mWebView
            android.view.View r1 = r1.getView()
            r9.addView(r1, r0)
        L_0x0074:
            com.uc.webview.export.extension.CommonExtension r0 = new com.uc.webview.export.extension.CommonExtension
            com.uc.webview.export.internal.interfaces.IWebView r1 = r9.mWebView
            r0.<init>(r1)
            r9.c = r0
            r0 = 10013(0x271d, float:1.4031E-41)
            java.lang.Object[] r1 = new java.lang.Object[r8]
            r1[r4] = r10
            com.uc.webview.export.internal.interfaces.IWebView r2 = r9.mWebView
            r1[r5] = r2
            int r2 = r9.f
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r1[r6] = r2
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r1)
            com.uc.webview.export.extension.UCExtension r0 = (com.uc.webview.export.extension.UCExtension) r0
            r9.d = r0
            boolean r0 = com.uc.webview.export.internal.d.f
            if (r0 != 0) goto L_0x009e
            r9.setWillNotDraw(r4)
        L_0x009e:
            com.uc.webview.export.internal.interfaces.IWebView r0 = r9.mWebView
            boolean r1 = r9.e
            r0.setDropDownOverScrollEnabled(r1)
            return
        L_0x00a6:
            r0 = 10040(0x2738, float:1.4069E-41)
            java.lang.Object[] r1 = new java.lang.Object[r4]
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r1)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x00c0
            com.uc.webview.export.internal.interfaces.IWebView r0 = r9.mWebView
            android.view.View r0 = r0.getView()
            r9.addView(r0)
            goto L_0x0074
        L_0x00c0:
            android.widget.FrameLayout$LayoutParams r0 = r9.generateLayoutParams(r11)
            com.uc.webview.export.internal.interfaces.IWebView r1 = r9.mWebView
            android.view.View r1 = r1.getView()
            r9.addView(r1, r0)
            goto L_0x0074
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.WebView.<init>(android.content.Context, android.util.AttributeSet, int, boolean, byte):void");
    }

    public void addJavascriptInterface(Object obj, String str) {
        this.mWebView.addJavascriptInterface(obj, str);
    }

    public boolean canGoBack() {
        return this.mWebView.canGoBack();
    }

    public boolean canGoBackOrForward(int i) {
        return this.mWebView.canGoBackOrForward(i);
    }

    public boolean canGoForward() {
        return this.mWebView.canGoForward();
    }

    public void clearCache(boolean z) {
        this.mWebView.clearCache(z);
    }

    public void clearFormData() {
        this.mWebView.clearFormData();
    }

    public void clearHistory() {
        this.mWebView.clearHistory();
    }

    public void clearMatches() {
        this.mWebView.clearMatches();
    }

    public void clearSslPreferences() {
        this.mWebView.clearSslPreferences();
    }

    public WebBackForwardList copyBackForwardList() {
        return this.mWebView.copyBackForwardListInner();
    }

    public final void destroy() {
        this.mWebView.destroy();
    }

    public void documentHasImages(Message message) {
        this.mWebView.documentHasImages(message);
    }

    public void findAllAsync(String str) {
        this.mWebView.findAllAsync(str);
    }

    public void findNext(boolean z) {
        this.mWebView.findNext(z);
    }

    public void flingScroll(int i, int i2) {
        this.mWebView.flingScroll(i, i2);
    }

    public SslCertificate getCertificate() {
        return this.mWebView.getCertificate();
    }

    public int getContentHeight() {
        return this.mWebView.getContentHeight();
    }

    public Bitmap getFavicon() {
        return this.mWebView.getFavicon();
    }

    public HitTestResult getHitTestResult() {
        if (this.mWebView.getHitTestResultInner() != null) {
            return new HitTestResult(this, this.mWebView.getHitTestResultInner(), (byte) 0);
        }
        return null;
    }

    public String[] getHttpAuthUsernamePassword(String str, String str2) {
        return this.mWebView.getHttpAuthUsernamePassword(str, str2);
    }

    public String getOriginalUrl() {
        return this.mWebView.getOriginalUrl();
    }

    public int getProgress() {
        return this.mWebView.getProgress();
    }

    @Deprecated
    public float getScale() {
        return this.mWebView.getScale();
    }

    public String getTitle() {
        return this.mWebView.getTitle();
    }

    public String getUrl() {
        return this.mWebView.getUrl();
    }

    public void goBack() {
        this.mWebView.goBack();
    }

    public void goBackOrForward(int i) {
        this.mWebView.goBackOrForward(i);
    }

    public void goForward() {
        this.mWebView.goForward();
    }

    public void invokeZoomPicker() {
        this.mWebView.invokeZoomPicker();
    }

    public void loadData(String str, String str2, String str3) {
        this.mWebView.loadData(str, str2, str3);
    }

    public void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5) {
        this.mWebView.loadDataWithBaseURL(str, str2, str3, str4, str5);
    }

    public void loadUrl(String str) {
        this.mWebView.loadUrl(str);
    }

    public void loadUrl(String str, Map<String, String> map) {
        this.mWebView.loadUrl(str, map);
    }

    public void onPause() {
        this.mWebView.onPause();
    }

    public void onResume() {
        this.mWebView.onResume();
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mWebView.setOnLongClickListener(onLongClickListener);
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mWebView.setOnTouchListener(onTouchListener);
    }

    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setOnKeyListener(View.OnKeyListener onKeyListener) {
        this.mWebView.setOnKeyListener(onKeyListener);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return this.mWebView.dispatchKeyEvent(keyEvent);
    }

    public boolean isVerticalScrollBarEnabled() {
        return this.mWebView.isVerticalScrollBarEnabled();
    }

    public void setVerticalScrollBarEnabled(boolean z) {
        this.mWebView.setVerticalScrollBarEnabled(z);
    }

    public boolean isHorizontalScrollBarEnabled() {
        return this.mWebView.isHorizontalScrollBarEnabled();
    }

    public void setHorizontalScrollBarEnabled(boolean z) {
        this.mWebView.setHorizontalScrollBarEnabled(z);
    }

    public boolean overlayHorizontalScrollbar() {
        return this.mWebView.overlayHorizontalScrollbar();
    }

    public boolean overlayVerticalScrollbar() {
        return this.mWebView.overlayVerticalScrollbar();
    }

    public boolean pageDown(boolean z) {
        return this.mWebView.pageDown(z);
    }

    public boolean pageUp(boolean z) {
        return this.mWebView.pageUp(z);
    }

    public void pauseTimers() {
        this.mWebView.pauseTimers();
    }

    public void postUrl(String str, byte[] bArr) {
        this.mWebView.postUrl(str, bArr);
    }

    public void reload() {
        this.mWebView.reload();
    }

    public void removeJavascriptInterface(String str) {
        try {
            this.mWebView.removeJavascriptInterface(str);
        } catch (NoSuchMethodError e2) {
        }
    }

    public WebSettings getSettings() {
        return this.a;
    }

    public void requestFocusNodeHref(Message message) {
        this.mWebView.requestFocusNodeHref(message);
    }

    public void requestImageRef(Message message) {
        this.mWebView.requestImageRef(message);
    }

    public WebBackForwardList restoreState(Bundle bundle) {
        return this.mWebView.restoreStateInner(bundle);
    }

    public void resumeTimers() {
        this.mWebView.resumeTimers();
    }

    public WebBackForwardList saveState(Bundle bundle) {
        return this.mWebView.saveStateInner(bundle);
    }

    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        if (this.mWebView != null) {
            this.mWebView.setBackgroundColor(i);
        }
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.mWebView.setDownloadListener(downloadListener);
    }

    public void setHttpAuthUsernamePassword(String str, String str2, String str3, String str4) {
        this.mWebView.setHttpAuthUsernamePassword(str, str2, str3, str4);
    }

    public void setInitialScale(int i) {
        this.mWebView.setInitialScale(i);
    }

    public void setNetworkAvailable(boolean z) {
        this.mWebView.setNetworkAvailable(z);
    }

    public final void setOverScrollMode(int i) {
    }

    public void setVerticalScrollbarOverlay(boolean z) {
        this.mWebView.setVerticalScrollbarOverlay(z);
    }

    public void stopLoading() {
        this.mWebView.stopLoading();
    }

    public boolean zoomIn() {
        return this.mWebView.zoomIn();
    }

    public boolean zoomOut() {
        return this.mWebView.zoomOut();
    }

    public void zoomBy(float f2) {
        if (((double) f2) < 0.01d) {
            throw new IllegalArgumentException("zoomFactor must be greater than 0.01.");
        } else if (((double) f2) > 100.0d) {
            throw new IllegalArgumentException("zoomFactor must be less than 100.");
        } else if (getCoreType() != 2) {
            ((InvokeObject) this.mWebView).invoke(7, new Object[]{Float.valueOf(f2)});
        } else if (Build.VERSION.SDK_INT >= 21) {
            try {
                ReflectionUtil.invoke((Object) getCoreView(), "zoomBy", new Class[]{Float.TYPE}, new Object[]{Float.valueOf(f2)});
            } catch (Exception e2) {
            }
        }
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        this.mWebView.setWebViewClient(webViewClient);
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        this.mWebView.setWebChromeClient(webChromeClient);
    }

    @Deprecated
    public int findAll(String str) {
        return this.mWebView.findAll(str);
    }

    public void setScrollBarStyle(int i) {
        this.mWebView.setScrollBarStyle(i);
        super.setScrollBarStyle(i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mWebView != null && this.b != null) {
            c.a(this.mWebView);
        }
    }

    public void evaluateJavascript(String str, ValueCallback<String> valueCallback) {
        this.mWebView.evaluateJavascript(str, valueCallback);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mWebView != null && this.b != null) {
            this.b.b(this.mWebView);
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (this.mWebView != null && this.b != null) {
            this.b.a(this.mWebView, i);
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mWebView != null && this.b != null) {
            this.b.a(i, i2);
        }
    }

    @Deprecated
    public boolean canZoomIn() {
        try {
            return this.mWebView.canZoomIn();
        } catch (NoSuchMethodError e2) {
            return false;
        }
    }

    @Deprecated
    public boolean canZoomOut() {
        try {
            return this.mWebView.canZoomOut();
        } catch (NoSuchMethodError e2) {
            return false;
        }
    }

    @Deprecated
    public Picture capturePicture() {
        return this.mWebView.capturePicture();
    }

    public static int getCoreType() {
        return ((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue();
    }

    public int getCurrentViewCoreType() {
        return this.f;
    }

    public View getCoreView() {
        return this.mWebView.getView();
    }

    public final void computeScroll() {
        super.computeScroll();
    }

    public void coreComputeScroll() {
        this.mWebView.superComputeScroll();
    }

    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void coreOnConfigurationChanged(Configuration configuration) {
        this.mWebView.superOnConfigurationChanged(configuration);
    }

    public final void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
    }

    public void coreOnVisibilityChanged(View view, int i) {
        this.mWebView.superOnVisibilityChanged(view, i);
    }

    public final void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
    }

    public void coreOnScrollChanged(int i, int i2, int i3, int i4) {
        this.mWebView.superOnScrollChanged(i, i2, i3, i4);
    }

    public boolean coreDispatchTouchEvent(MotionEvent motionEvent) {
        return this.mWebView.superDispatchTouchEvent(motionEvent);
    }

    public final void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void coreDraw(Canvas canvas) {
        this.mWebView.superDraw(canvas);
    }

    public void coreDestroy() {
        this.mWebView.superDestroy();
    }

    public final void setVisibility(int i) {
        super.setVisibility(i);
    }

    public void coreSetVisibility(int i) {
        this.mWebView.superSetVisibility(i);
    }

    public final void requestLayout() {
        super.requestLayout();
    }

    public void coreRequestLayout() {
        this.mWebView.superRequestLayout();
    }

    public boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        return super.overScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
    }

    public boolean coreOverScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        return this.mWebView.superOverScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
    }

    public void coreOnInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        accessibilityNodeInfo.setClassName(WebView.class.getName());
        ((InvokeObject) this.mWebView).invoke(1, new Object[]{accessibilityNodeInfo});
    }

    public void coreOnInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        accessibilityEvent.setClassName(WebView.class.getName());
        ((InvokeObject) this.mWebView).invoke(2, new Object[]{accessibilityEvent});
    }

    public boolean corePerformAccessibilityAction(int i, Bundle bundle) {
        return Boolean.parseBoolean(((InvokeObject) this.mWebView).invoke(3, new Object[]{Integer.valueOf(i), bundle}).toString());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getCoreType() == 1 && this.e && this.mWebView.isInOverScrollMoving()) {
            float f2 = getContext().getResources().getDisplayMetrics().density;
            float f3 = 13.0f * f2;
            float f4 = 15.0f * f2;
            float f5 = 25.0f * f2;
            float f6 = 24.0f * f2;
            if (((UCMobileWebKit) d.a((int) UCAsyncTask.getRootTask, new Object[0])) != null) {
                IWebResourceInternal webResources = ((UCMobileWebKit) d.a((int) UCAsyncTask.getRootTask, new Object[0])).getWebResources();
                canvas.drawColor(webResources.getColor(IWebResourceInternal.COLOR_DROP_DOWN_BG_COLOR));
                Drawable drawable = webResources.getDrawable(IWebResourceInternal.DRAWABLE_WEBVIEW_DROP_DOWN_SHADOW);
                drawable.setBounds(0, 0, getRight(), drawable.getIntrinsicHeight());
                drawable.draw(canvas);
                Paint paint = new Paint(1);
                paint.setColor(webResources.getColor(IWebResourceInternal.COLOR_DROP_DOWN_TEXT_COLOR));
                paint.setFilterBitmap(true);
                paint.setTextSize(f3);
                String[] split = webResources.getText(IWebResourceInternal.TEXT_DOWP_DOWN_BRAND_EXPOSURE).split("%END");
                if (split.length >= 2) {
                    String replace = split[0].replace("%URL", a(getUrl()));
                    float f7 = f3 + f5;
                    canvas.drawText(replace, (((float) getRight()) - paint.measureText(replace)) / 2.0f, f7, paint);
                    String str = split[1];
                    float right = ((((float) getRight()) - paint.measureText(str)) - f4) / 2.0f;
                    float f8 = f7 + f6;
                    Drawable drawable2 = webResources.getDrawable(IWebResourceInternal.DRAWABLE_UC_LOGO);
                    drawable2.setBounds((int) right, (int) ((f8 - f4) + 1.0f), (int) (right + f4), ((int) f8) + 1);
                    drawable2.draw(canvas);
                    canvas.drawText(str, right + f4, f8, paint);
                }
            }
        }
    }

    private static String a(String str) {
        String str2;
        if (str == null) {
            return "";
        }
        try {
            str2 = new URL(str).getHost();
        } catch (MalformedURLException e2) {
            str2 = str;
        }
        if (str2 != null) {
            str = str2;
        }
        if (str.length() > 30) {
            return str.substring(0, 27) + "...";
        }
        return str;
    }

    /* compiled from: ProGuard */
    private static class b extends WebViewClient {
        private Object a;

        b(Object obj) {
            this.a = obj;
        }

        public final void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            ReflectionUtil.invokeNoThrow(this.a, "onPageStarted", new Class[]{Object.class, String.class, Bitmap.class}, new Object[]{webView, str, bitmap});
        }

        public final void onPageFinished(WebView webView, String str) {
            ReflectionUtil.invokeNoThrow(this.a, "onPageFinished", new Class[]{Object.class, String.class}, new Object[]{webView, str});
        }
    }

    /* compiled from: ProGuard */
    private static class a extends WebChromeClient {
        private Object a;

        a(Object obj) {
            this.a = obj;
        }

        public final boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
            return ((Boolean) ReflectionUtil.invokeNoThrow(this.a, "onJsAlert", new Class[]{Object.class, String.class, String.class, Object.class}, new Object[]{webView, str, str2, jsResult})).booleanValue();
        }

        public final boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
            return ((Boolean) ReflectionUtil.invokeNoThrow(this.a, "onJsConfirm", new Class[]{Object.class, String.class, String.class, Object.class}, new Object[]{webView, str, str2, jsResult})).booleanValue();
        }

        public final boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            return ((Boolean) ReflectionUtil.invokeNoThrow(this.a, "onJsPrompt", new Class[]{Object.class, String.class, String.class, Object.class}, new Object[]{webView, str, str2, jsPromptResult})).booleanValue();
        }
    }

    public void setTag(Object obj) {
        if (obj.getClass().getName().equals("io.selendroid.server.uc.impl.WebViewImpl$WebViewClientImpl")) {
            setWebViewClient(new b(obj));
        } else if (obj.getClass().getName().equals("io.selendroid.server.uc.impl.WebViewImpl$WebChromeClientImpl")) {
            setWebChromeClient(new a(obj));
        } else {
            super.setTag(obj);
        }
    }
}
