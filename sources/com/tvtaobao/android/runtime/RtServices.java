package com.tvtaobao.android.runtime;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RtServices extends Service {
    private static final String TAG = RtServices.class.getSimpleName();

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return 2;
        }
        Log.i(TAG, intent.toString());
        RtCmdHandler.onCMD(this, intent);
        return 2;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
