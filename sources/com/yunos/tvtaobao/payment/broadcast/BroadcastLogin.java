package com.yunos.tvtaobao.payment.broadcast;

import android.content.Context;
import android.content.Intent;

public class BroadcastLogin {
    private static final String MEMBER_LOGIN_BROADCAST_ACTION = "com.membersSDK.login";
    private static final String MEMBER_LOGOUT_BROADCAST_ACTION = "com.membersSDK.loginOut";

    public static void sendBroadcastLogin(Context context, boolean islogin) {
        if (islogin) {
            context.sendBroadcast(new Intent("com.membersSDK.login"));
        } else {
            context.sendBroadcast(new Intent("com.membersSDK.loginOut"));
        }
    }
}
