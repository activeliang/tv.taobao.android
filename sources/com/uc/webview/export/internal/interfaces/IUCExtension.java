package com.uc.webview.export.internal.interfaces;

import com.uc.webview.export.annotations.Interface;
import com.uc.webview.export.extension.IBackForwardListListener;
import com.uc.webview.export.extension.UCClient;
import com.uc.webview.export.extension.UCExtension;
import com.uc.webview.export.extension.UCSettings;

@Interface
/* compiled from: ProGuard */
public interface IUCExtension extends InvokeObject {
    int getActiveLayoutStyle();

    String getBackUrl();

    String getFocusedNodeAnchorText();

    String getFocusedNodeImageUrl();

    String getFocusedNodeLinkUrl();

    String getForwardUrl();

    String getHttpsRemoteCertificate(String str);

    String getPageEncoding();

    int getPageSize();

    UCSettings getUCSettings();

    boolean ignoreTouchEvent();

    void moveCursorToTextInput(int i);

    void setBackForwardListListener(IBackForwardListListener iBackForwardListListener);

    void setClient(UCClient uCClient);

    void setInjectJSProvider(UCExtension.InjectJSProvider injectJSProvider, int i);

    void setSoftKeyboardListener(UCExtension.OnSoftKeyboardListener onSoftKeyboardListener);

    void setTextSelectionClient(UCExtension.TextSelectionClient textSelectionClient);
}
