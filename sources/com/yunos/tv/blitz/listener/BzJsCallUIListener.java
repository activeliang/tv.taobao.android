package com.yunos.tv.blitz.listener;

import android.content.Context;

public interface BzJsCallUIListener {
    String onUIDialog(Context context, String str);

    String onUILoading(Context context, String str);

    String onUINetworkDialog(Context context, String str);

    String onUIStopLoading(Context context, String str);
}
