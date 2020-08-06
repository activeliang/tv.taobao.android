package com.uc.webview.export;

import android.webkit.WebHistoryItem;
import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: ProGuard */
public class WebBackForwardList {
    public android.webkit.WebBackForwardList mList = null;

    public WebHistoryItem createItem(WebHistoryItem webHistoryItem) {
        return null;
    }

    public synchronized WebHistoryItem getCurrentItem() {
        WebHistoryItem createItem;
        WebHistoryItem currentItem = this.mList.getCurrentItem();
        if (currentItem == null) {
            createItem = null;
        } else {
            createItem = createItem(currentItem);
        }
        return createItem;
    }

    public synchronized int getCurrentIndex() {
        return this.mList.getCurrentIndex();
    }

    public synchronized WebHistoryItem getItemAtIndex(int i) {
        WebHistoryItem createItem;
        WebHistoryItem itemAtIndex = this.mList.getItemAtIndex(i);
        if (itemAtIndex == null) {
            createItem = null;
        } else {
            createItem = createItem(itemAtIndex);
        }
        return createItem;
    }

    public synchronized int getSize() {
        return this.mList.getSize();
    }

    public synchronized WebBackForwardList clone() {
        return null;
    }
}
