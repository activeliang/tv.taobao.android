package com.taobao.orange.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.taobao.orange.aidl.IOrangeApiService;
import com.taobao.orange.aidl.OrangeApiServiceStub;
import com.taobao.orange.util.OLog;

public class OrangeApiService extends Service {
    private static final String TAG = "OrangeApiService";
    private IOrangeApiService.Stub mBinder = null;

    public IBinder onBind(Intent intent) {
        OLog.i(TAG, "onBind", new Object[0]);
        if (this.mBinder == null) {
            this.mBinder = new OrangeApiServiceStub(this);
        }
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        OLog.i(TAG, "onUnbind", new Object[0]);
        return super.onUnbind(intent);
    }

    public void onCreate() {
        OLog.d(TAG, "onCreate", new Object[0]);
        super.onCreate();
    }

    public void onDestroy() {
        OLog.d(TAG, "onDestroy", new Object[0]);
        super.onDestroy();
    }

    public void onRebind(Intent intent) {
        OLog.i(TAG, "onRebind", "intent", intent);
        super.onRebind(intent);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 2;
    }
}
