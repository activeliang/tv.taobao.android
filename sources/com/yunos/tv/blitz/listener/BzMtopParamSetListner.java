package com.yunos.tv.blitz.listener;

import android.content.Context;

public interface BzMtopParamSetListner {
    String getAppendPamram(Context context, String str);

    String getCookedDataForJs(Context context, String str);
}
