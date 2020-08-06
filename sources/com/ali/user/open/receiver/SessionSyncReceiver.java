package com.ali.user.open.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.user.open.service.impl.SessionManager;

public class SessionSyncReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            try {
                if (TextUtils.equals(intent.getAction(), "aliuser_sync_session") && TextUtils.equals("login_sdk", intent.getStringExtra("from"))) {
                    SessionManager.INSTANCE.reloadSession();
                }
            } catch (Throwable th) {
            }
        }
    }
}
