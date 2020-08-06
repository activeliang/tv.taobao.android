package com.yunos.tv.blitz.listener;

import android.content.Context;

public interface BzMiscListener {
    String onGetMtopRequest(Context context, String str, int i);

    void onGetMtopResponse(String str, int i, String str2, String str3);

    void onStartActivity(Context context, String str, int i);

    void onStartActivityForResult(Context context, String str);
}
