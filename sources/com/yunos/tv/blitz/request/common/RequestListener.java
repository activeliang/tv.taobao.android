package com.yunos.tv.blitz.request.common;

import org.json.JSONArray;

public interface RequestListener<T> {
    void onRequestDone(T t, int i, String str, JSONArray jSONArray, String str2);
}
