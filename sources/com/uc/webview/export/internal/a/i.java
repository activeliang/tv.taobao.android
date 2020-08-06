package com.uc.webview.export.internal.a;

import com.uc.webview.export.WebBackForwardList;
import com.uc.webview.export.WebHistoryItem;

/* compiled from: ProGuard */
final class i extends WebBackForwardList {

    /* compiled from: ProGuard */
    private class a extends WebHistoryItem {
        a(android.webkit.WebHistoryItem webHistoryItem) {
            this.mItem = webHistoryItem;
        }
    }

    i(android.webkit.WebBackForwardList webBackForwardList) {
        this.mList = webBackForwardList;
    }

    /* access modifiers changed from: protected */
    public final WebHistoryItem createItem(android.webkit.WebHistoryItem webHistoryItem) {
        return new a(webHistoryItem);
    }
}
