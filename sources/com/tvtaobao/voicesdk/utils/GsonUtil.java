package com.tvtaobao.voicesdk.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {
    private static final Gson G = new Gson();

    private GsonUtil() {
    }

    public static <T> T parseJson(String json, TypeToken<T> token) {
        if (json == null) {
            return null;
        }
        try {
            return G.fromJson(json, token.getType());
        } catch (Exception e) {
            LogPrint.e("GsonUtil", "parseJson error:" + e);
            return null;
        }
    }
}
