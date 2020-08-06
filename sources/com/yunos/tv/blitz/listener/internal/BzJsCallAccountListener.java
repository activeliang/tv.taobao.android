package com.yunos.tv.blitz.listener.internal;

import android.content.Context;

public interface BzJsCallAccountListener {
    String onAccountApplyMtopToken(Context context, String str, int i);

    String onAccountCommonApi(Context context, String str, int i);

    String onAccountGetToken(Context context, String str, int i);

    String onAccountGetUserInfo(Context context, String str, int i);

    String onAccountIsLogin(Context context, String str);

    String onAccountLogin(Context context, String str);

    String onApplyNewMtopToken(Context context, String str, int i);
}
