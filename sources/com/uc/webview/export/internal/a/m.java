package com.uc.webview.export.internal.a;

import android.content.Intent;
import com.uc.webview.export.WebChromeClient;

/* compiled from: ProGuard */
final class m extends WebChromeClient.FileChooserParams {
    final /* synthetic */ j a;

    m(j jVar) {
        this.a = jVar;
    }

    public final int getMode() {
        return 0;
    }

    public final String[] getAcceptTypes() {
        return new String[]{"*/*"};
    }

    public final boolean isCaptureEnabled() {
        return false;
    }

    public final CharSequence getTitle() {
        return "";
    }

    public final String getFilenameHint() {
        return "";
    }

    public final Intent createIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("*/*");
        return intent;
    }
}
