package com.yunos.tv.blitz.listener;

import android.content.Context;

public interface BzPageStatusListener {
    void onPageLoadError(Context context, String str, String str2);

    void onPageLoadFinished(Context context, String str);

    void onPageLoadStart(Context context, String str);
}
