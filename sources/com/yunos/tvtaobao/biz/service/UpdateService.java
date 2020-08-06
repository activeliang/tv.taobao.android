package com.yunos.tvtaobao.biz.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.UpdateStatus;
import com.yunos.tvtaobao.biz.IAppUpdate;
import com.yunos.tvtaobao.biz.IAppUpdateCallback;
import com.yunos.tvtaobao.biz.controller.LogUtils;
import com.yunos.tvtaobao.biz.controller.Update;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.biz.util.UserTrackUtil;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public class UpdateService extends Service {
    private static final String TAG = "UpdateService";
    /* access modifiers changed from: private */
    public IAppUpdateCallback mAppUpdateCallback;
    public final IAppUpdate.Stub mBinder = new IAppUpdate.Stub() {
        public void startUpdate(String jsonParam, IAppUpdateCallback callback) throws RemoteException {
            ZpLogger.d(UpdateService.TAG, "UpdateService.startUpdate, jsonParam: " + jsonParam + ", callback = " + callback);
            UpdateStatus.setUpdateStatus(UpdateStatus.UNKNOWN, (Bundle) null);
            if (callback != null) {
                IAppUpdateCallback unused = UpdateService.this.mAppUpdateCallback = callback;
            }
            if (jsonParam == null) {
                ZpLogger.d(UpdateService.TAG, "jsonParam is null");
                UpdateService.this.doCallback(10000, "", "jsonParam is null");
                return;
            }
            try {
                JSONObject json = JSON.parseObject(jsonParam);
                String code = json.getString("code");
                if (code == null) {
                    ZpLogger.d(UpdateService.TAG, "code is null");
                    UpdateService.this.doCallback(10000, "", "params error");
                    return;
                }
                UserTrackUtil.setCurVersionCode(json.getString("versionCode"));
                if (code.equalsIgnoreCase(UpdatePreference.TVTAOBAO_EXTERNAL)) {
                    ZpLogger.d(UpdateService.TAG, "UpdateService.startUpdate.external call");
                    Update upExternal = Update.get(UpdatePreference.TVTAOBAO_EXTERNAL);
                    if (upExternal != null) {
                        upExternal.stop();
                        Update.remove(UpdatePreference.TVTAOBAO_EXTERNAL);
                    }
                    Update upExternal2 = new Update(UpdateService.this, UpdateService.this.mServiceHandler, jsonParam);
                    Update.add(UpdatePreference.TVTAOBAO_EXTERNAL, upExternal2);
                    upExternal2.start();
                } else {
                    ZpLogger.d(UpdateService.TAG, "UpdateService.startUpdate.internal call");
                    Update upInternal = Update.get("tvtaobao");
                    if (upInternal != null) {
                        upInternal.stop();
                        Update.remove("tvtaobao");
                    }
                    Update upInternal2 = new Update(UpdateService.this, UpdateService.this.mServiceHandler, jsonParam);
                    Update.add("tvtaobao", upInternal2);
                    upInternal2.start();
                }
                UpdateService.this.doCallback(10003, code, "start update");
            } catch (JSONException e) {
                ZpLogger.d(UpdateService.TAG, "incorrect jsonParam format: " + e.getLocalizedMessage());
                UpdateService.this.doCallback(10000, "", "json exception");
            }
        }

        public void stopUpdate(String jsonParam) throws RemoteException {
            ZpLogger.i(UpdateService.TAG, "UpdateService.stopUpdate.jsonParam = " + jsonParam);
            JSONObject json = null;
            try {
                json = JSON.parseObject(jsonParam);
            } catch (JSONException e) {
                ZpLogger.d(UpdateService.TAG, "UpdateService.stopUpdate.json exception: " + e.getLocalizedMessage());
            }
            if (json == null) {
                UpdateService.this.doCallback(10000, "", "json exception");
                return;
            }
            String code = json.getString("code");
            if (code == null) {
                UpdateService.this.doCallback(10000, "", "params error");
                return;
            }
            ZpLogger.d(UpdateService.TAG, "UpdateService.stopUpdate.call stopUpdate, code: " + code);
            Update up = Update.get(code);
            if (up != null) {
                up.stop();
            }
        }
    };
    private Context mContext = null;
    /* access modifiers changed from: private */
    public MyHandler mServiceHandler;

    public MyHandler getmServiceHandler() {
        return this.mServiceHandler;
    }

    private static class MyHandler extends Handler {
        private WeakReference<UpdateService> mOuter;

        public MyHandler(UpdateService updateService) {
            this.mOuter = new WeakReference<>(updateService);
        }

        public void handleMessage(Message msg) {
            UpdateService updateService = (UpdateService) this.mOuter.get();
            if (updateService != null) {
                switch (msg.what) {
                    case 104:
                        LogUtils.getInstance(updateService).logReceive(updateService);
                        return;
                    case 105:
                        LogUtils.getInstance(updateService).stop();
                        return;
                    case 106:
                        String log = msg.obj.toString();
                        String mUpdateParams = updateService.getSharedPreferences("updateInfo", 0).getString(UpdatePreference.UPDATE_OBJECT, "");
                        boolean isMoHeOn = SharePreferences.getBoolean(UpdatePreference.IS_MOHE_LOG_ON, false).booleanValue();
                        boolean isLianMengLogOn = SharePreferences.getBoolean(UpdatePreference.IS_LIANMNEG_LOG_ON, false).booleanValue();
                        boolean isYiTiJiLogOn = SharePreferences.getBoolean(UpdatePreference.IS_YITIJI_LOG_ON, false).booleanValue();
                        if (StringUtil.isEmpty(mUpdateParams)) {
                            return;
                        }
                        if (Config.MOHE.equals(Config.getChannel()) && !isMoHeOn) {
                            return;
                        }
                        if (Config.LIANMENG.equals(Config.getChannel()) && !isLianMengLogOn) {
                            return;
                        }
                        if (!Config.YITIJI.equals(Config.getChannel()) || isYiTiJiLogOn) {
                            JSONObject json = JSON.parseObject(mUpdateParams);
                            String code = json.getString("code");
                            String versionCode = json.getString("versionCode");
                            String versionName = json.getString("versionName");
                            String uuid = json.getString("uuid");
                            String channelId = json.getString("channelId");
                            String systemInfo = json.getString("systemInfo");
                            BusinessRequest.getBusinessRequest().logReceive(json.getString("version"), uuid, channelId, code, versionCode, versionName, systemInfo, log, new LogReceiveListener());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private static class LogReceiveListener implements RequestListener<String> {
        public void onRequestDone(String data, int resultCode, String handleMessagemsg) {
            ZpLogger.d(UpdateService.TAG, "onRequestDone " + data);
        }
    }

    public IBinder onBind(Intent intent) {
        ZpLogger.d(TAG, "UpdateService.onBind.UpdateService.onBinde, return mBinder: " + this.mBinder);
        return this.mBinder;
    }

    public void onCreate() {
        super.onCreate();
        ZpLogger.d(TAG, "UpdateService.service onCreate");
        this.mContext = getApplicationContext();
        this.mServiceHandler = new MyHandler(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        ZpLogger.d(TAG, "UpdateService.service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        ZpLogger.d(TAG, "UpdateService.service onDestroy");
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void doCallback(int status, String code, String info) {
        if (this.mAppUpdateCallback != null) {
            Bundle result = new Bundle();
            result.putString("code", code);
            result.putString("info", info);
            result.putInt("status", status);
            try {
                this.mAppUpdateCallback.onUpdateStatusChanged(result);
            } catch (RemoteException e) {
                ZpLogger.d(TAG, "remote exception: " + e.getLocalizedMessage());
            }
        }
    }
}
