package com.yunos.tvtaobao.biz.updatesdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tvtaobao.biz.IAppUpdate;
import com.yunos.tvtaobao.biz.IAppUpdateCallback;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.zhiping.dev.android.logger.ZpLogger;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateClient implements IAppUpdateCallback {
    private static final String BUNDLE_KEY = "bundle";
    private static final String CODE_KEY = "code";
    private static final String INFO_KEY = "info";
    private static final String STATUS_KEY = "status";
    private static UpdateClient s_UpdateClient = null;
    private String TAG = "UpdateClient";
    /* access modifiers changed from: private */
    public IAppUpdate mAppUpdateService = null;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            IAppUpdate unused = UpdateClient.this.mAppUpdateService = IAppUpdate.Stub.asInterface(service);
            try {
                UpdateClient.this.mAppUpdateService.startUpdate(UpdateClient.this.mUpdateParam, UpdateClient.this);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            IAppUpdate unused = UpdateClient.this.mAppUpdateService = null;
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    private IUpdateCallback mUpdateCallback;
    /* access modifiers changed from: private */
    public String mUpdateParam;

    public interface IUpdateCallback {
        public static final int UPDATE_END = 200;
        public static final int UPDATE_ERROR = -1;
        public static final int UPDATE_PROCESSING = 101;
        public static final int UPDATE_START = 100;

        void onUpdateStatusChanged(int i, String str, Bundle bundle);
    }

    public static UpdateClient getInstance(Context context) {
        if (s_UpdateClient == null) {
            s_UpdateClient = new UpdateClient(context);
        }
        return s_UpdateClient;
    }

    public UpdateClient(Context context) {
        this.mContext = context;
        bindService();
    }

    private void bindService() {
        if (this.mAppUpdateService == null) {
            try {
                Intent intent = new Intent();
                intent.setPackage(this.mContext.getPackageName());
                intent.setAction("com.yunos.taobaotv.update.updateservice.IAppUpdate");
                this.mContext.bindService(intent, this.mConnection, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void unbindService() {
        if (this.mAppUpdateService != null) {
            this.mContext.unbindService(this.mConnection);
            this.mAppUpdateService = null;
        }
    }

    public void startDownload(String appCode, String versionName, int versionCode, String deviceId, String channelId, boolean isDirectShowUpdate, IUpdateCallback callback) {
        ZpLogger.i(this.TAG, this.TAG + ".startDownload.appCode = " + appCode + ".versionName = " + versionName + ", versionCode = " + versionCode + ".deviceId = " + deviceId + ".channelId = " + channelId + ", callback = " + callback);
        this.mUpdateCallback = callback;
        JSONObject paramJson = new JSONObject();
        if (appCode == null) {
            appCode = "";
        }
        try {
            paramJson.put("code", appCode);
            paramJson.put("versionCode", "" + versionCode);
            if (versionName == null) {
                versionName = "";
            }
            paramJson.put("versionName", versionName);
            if (deviceId == null) {
                deviceId = "";
            }
            paramJson.put("uuid", deviceId);
            if (channelId == null) {
                channelId = "";
            }
            paramJson.put("channelId", channelId);
            paramJson.put("isDirectShowUpdate", isDirectShowUpdate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mUpdateParam = paramJson.toString();
        if (this.mAppUpdateService == null) {
            bindService();
            return;
        }
        try {
            this.mAppUpdateService.startUpdate(this.mUpdateParam, this);
        } catch (RemoteException e2) {
            e2.printStackTrace();
        }
    }

    public void stopDownload() {
        if (this.mAppUpdateService != null) {
            try {
                this.mAppUpdateService.stopUpdate(this.mUpdateParam);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService();
        }
    }

    public IBinder asBinder() {
        return null;
    }

    public void onUpdateStatusChanged(Bundle result) throws RemoteException {
        ZpLogger.v(this.TAG, this.TAG + ".onUpdateStatusChanged.result = " + result + ", mUpdateCallback = " + this.mUpdateCallback);
        if (this.mUpdateCallback != null && result != null) {
            int status = result.getInt("status");
            String string = result.getString("code");
            this.mUpdateCallback.onUpdateStatusChanged(status, result.getString("info"), result.getBundle("bundle"));
        }
    }

    public void addCurrentVersionToSP() {
        new Thread(new Runnable() {
            public void run() {
                SharedPreferences.Editor editor = UpdateClient.this.mContext.getSharedPreferences("updateInfo", 0).edit();
                editor.putInt(UpdatePreference.UPDATE_CURRENT_VERSION_CODE, AppInfo.getAppVersionNum());
                editor.apply();
            }
        }).start();
    }
}
