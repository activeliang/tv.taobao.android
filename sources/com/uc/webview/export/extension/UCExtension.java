package com.uc.webview.export.extension;

import android.webkit.ValueCallback;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.interfaces.IUCExtension;
import com.uc.webview.export.internal.interfaces.IWebView;

@Api
/* compiled from: ProGuard */
public class UCExtension {
    public static final int LAYOUT_STYLE_ADAPT_SCREEN = 2;
    public static final int LAYOUT_STYLE_MOBILE_OPTIMUM = 4;
    public static final int LAYOUT_STYLE_ZOOM_OPTIMUM = 1;
    public static final String MOVE_CURSOR_KEY_NEXT_ENABLE = "next_enable";
    public static final String MOVE_CURSOR_KEY_PREVIOUS_ENABLE = "previous_enable";
    public static final String MOVE_CURSOR_KEY_SUCCEED = "succeed";
    public static final int MOVE_CURSOR_STEP_CURRENT = 0;
    public static final int MOVE_CURSOR_STEP_NEXT = 1;
    public static final int MOVE_CURSOR_STEP_PREV = -1;
    private IUCExtension a;

    @Api
    /* compiled from: ProGuard */
    public interface InjectJSProvider {
        public static final int TYPE_HEAD_START = 1;

        String getJS(int i);
    }

    @Api
    /* compiled from: ProGuard */
    public interface OnSoftKeyboardListener {
        boolean displaySoftKeyboard(String str, int i, ValueCallback<String> valueCallback);

        boolean hideSoftKeyboard();
    }

    @Api
    /* compiled from: ProGuard */
    public static class TextSelectionClient {
        public boolean shouldShowSearchItem() {
            return true;
        }

        public boolean shouldShowShareItem() {
            return true;
        }

        public boolean onSearchClicked(String str) {
            return false;
        }

        public boolean onShareClicked(String str) {
            return false;
        }
    }

    public UCExtension(IWebView iWebView) {
        this.a = iWebView.getUCExtension();
    }

    public void setClient(UCClient uCClient) {
        this.a.setClient(uCClient);
    }

    public String getBackUrl() {
        return this.a.getBackUrl();
    }

    public String getForwardUrl() {
        return this.a.getForwardUrl();
    }

    public int getPageSize() {
        return this.a.getPageSize();
    }

    public String getPageEncoding() {
        return this.a.getPageEncoding();
    }

    public String getHttpsRemoteCertificate(String str) {
        return this.a.getHttpsRemoteCertificate(str);
    }

    public void setBackForwardListListener(IBackForwardListListener iBackForwardListListener) {
        this.a.setBackForwardListListener(iBackForwardListListener);
    }

    public void moveCursorToTextInput(int i) {
        this.a.moveCursorToTextInput(i);
    }

    public String getFocusedNodeLinkUrl() {
        return this.a.getFocusedNodeLinkUrl();
    }

    public String getFocusedNodeAnchorText() {
        return this.a.getFocusedNodeAnchorText();
    }

    public String getFocusedNodeImageUrl() {
        return this.a.getFocusedNodeImageUrl();
    }

    public int getActiveLayoutStyle() {
        return this.a.getActiveLayoutStyle();
    }

    public UCSettings getUCSettings() {
        return this.a.getUCSettings();
    }

    public void setInjectJSProvider(InjectJSProvider injectJSProvider, int i) {
        this.a.setInjectJSProvider(injectJSProvider, i);
    }

    public void setSoftKeyboardListener(OnSoftKeyboardListener onSoftKeyboardListener) {
        this.a.setSoftKeyboardListener(onSoftKeyboardListener);
    }

    public void setTextSelectionClient(TextSelectionClient textSelectionClient) {
        this.a.setTextSelectionClient(textSelectionClient);
    }

    public boolean isLoadFromCachedPage() {
        Boolean bool = (Boolean) this.a.invoke(4, (Object[]) null);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public boolean ignoreTouchEvent() {
        return this.a.ignoreTouchEvent();
    }
}
