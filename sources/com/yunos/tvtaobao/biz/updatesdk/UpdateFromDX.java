package com.yunos.tvtaobao.biz.updatesdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.ihome.android.market2.aidl.AppOperateAidl;
import com.tvtaobao.voicesdk.register.type.RegisterType;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.BuildConfig;
import com.yunos.tvtaobao.payment.request.TvtaobaoSwitchRequest;
import com.zhiping.dev.android.logger.ZpLogger;

public class UpdateFromDX {
    private static final String TAG = "UpdateFromDX";
    private static UpdateFromDX updateFromDX;
    private ServiceConnection appOperateConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppOperateAidl unused = UpdateFromDX.this.appOperateService = AppOperateAidl.Stub.asInterface(service);
            UpdateFromDX.this.appCheckUpdate();
        }

        public void onServiceDisconnected(ComponentName name) {
            AppOperateAidl unused = UpdateFromDX.this.appOperateService = null;
        }
    };
    /* access modifiers changed from: private */
    public AppOperateAidl appOperateService = null;
    private final String jujingcaiAppId = "custom00012018070411223000000000101685";
    private Context mContext;
    private final String yuemeAppId = "customAPPIDspS225440000000000000001327";

    public static UpdateFromDX getInstance(Context context) {
        if (updateFromDX == null) {
            synchronized (UpdateFromDX.class) {
                if (updateFromDX == null) {
                    updateFromDX = new UpdateFromDX(context);
                }
            }
        }
        return updateFromDX;
    }

    private UpdateFromDX(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.mContext = context;
    }

    /* access modifiers changed from: private */
    public void bindService() {
        this.mContext.bindService(new Intent("com.ihome.android.market2.aidl.AppOperateAidl"), this.appOperateConn, 1);
    }

    /* access modifiers changed from: private */
    public void unbindService() {
        new Intent("com.ihome.android.market2.aidl.AppOperateAidl");
        this.mContext.unbindService(this.appOperateConn);
    }

    public void appCheckUpdate() {
        new Thread() {
            public void run() {
                try {
                    if (UpdateFromDX.this.appOperateService == null) {
                        ZpLogger.e(UpdateFromDX.TAG, "Service was not  bind");
                        UpdateFromDX.this.bindService();
                        sleep(AbstractClientManager.BIND_SERVICE_TIMEOUT);
                    }
                    if (UpdateFromDX.this.appOperateService == null) {
                        ZpLogger.e(UpdateFromDX.TAG, "appCheckUpdate > 检测异常");
                    } else if (UpdateFromDX.this.appOperateService.appUpdateCheck()) {
                        ZpLogger.e(UpdateFromDX.TAG, "appCheckUpdate > 发现新版本，可以升级！");
                        UpdateFromDX.this.appUpdate();
                    } else {
                        ZpLogger.e(UpdateFromDX.TAG, "appCheckUpdate > 无需升级！");
                        UpdateFromDX.this.unbindService();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Thread.currentThread().interrupt();
                }
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void appUpdate() {
        String appId;
        if ("2017050920".equals(Config.getChannel())) {
            appId = "custom00012018070411223000000000101685";
        } else {
            appId = "customAPPIDspS225440000000000000001327";
        }
        final AppOperateReqInfo info = new AppOperateReqInfo(appId, TvtaobaoSwitchRequest.PLATFORM, BuildConfig.APPLICATION_ID, RegisterType.UPDATE);
        new Thread() {
            public void run() {
                try {
                    if (UpdateFromDX.this.appOperateService == null) {
                        UpdateFromDX.this.bindService();
                        ZpLogger.e(UpdateFromDX.TAG, "Service : not  bind");
                        sleep(AbstractClientManager.BIND_SERVICE_TIMEOUT);
                    }
                    if (UpdateFromDX.this.appOperateService != null) {
                        String result = UpdateFromDX.this.appOperateService.appOperate(new Gson().toJson((Object) info));
                        ZpLogger.e(UpdateFromDX.TAG, "appUpdate > result = " + result);
                        if (!TextUtils.isEmpty(result)) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Thread.currentThread().interrupt();
                }
            }
        }.start();
    }
}
