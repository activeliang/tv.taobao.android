package com.taobao.mediaplay.common;

import android.content.Context;

public interface IYKVideoSourceAdapter {
    void getVideoUrlInfo(Context context, String str, IDWVideourlCallBack iDWVideourlCallBack);
}
