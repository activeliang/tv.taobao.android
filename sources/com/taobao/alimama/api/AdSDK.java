package com.taobao.alimama.api;

import android.content.Context;
import android.support.annotation.Keep;

@Keep
public class AdSDK {
    public static <T> T getService(Class<T> cls) {
        return a.a().a(cls);
    }

    public static void initSDK(Context context) {
        a.a().a(context);
    }
}
