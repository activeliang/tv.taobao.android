package com.ut.mini.internal;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.logbuilder.TimeStampAdjustMgr;
import com.alibaba.analytics.core.sync.HttpsHostPortMgr;
import com.alibaba.analytics.core.sync.TnetHostPortMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;
import com.alibaba.analytics.utils.StringUtils;
import com.ta.utdid2.device.UTDevice;
import com.ut.mini.UTAnalytics;
import com.ut.mini.exposure.ExposureUtils;
import com.ut.mini.exposure.TrackerManager;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class UTTeamWork {
    private static final String TAG = "UTTeamWork";
    private static UTTeamWork s_instance = null;

    public static synchronized UTTeamWork getInstance() {
        UTTeamWork uTTeamWork;
        synchronized (UTTeamWork.class) {
            if (s_instance == null) {
                s_instance = new UTTeamWork();
            }
            uTTeamWork = s_instance;
        }
        return uTTeamWork;
    }

    public void initialized() {
    }

    public void turnOnRealTimeDebug(Map<String, String> aMap) {
        Logger.d(TAG, "", aMap.entrySet().toArray());
        UTAnalytics.getInstance().turnOnRealTimeDebug(aMap);
    }

    public void turnOffRealTimeDebug() {
        Logger.e();
        UTAnalytics.getInstance().turnOffRealTimeDebug();
    }

    public void dispatchLocalHits() {
        UTAnalytics.getInstance().dispatchLocalHits();
    }

    public void saveCacheDataToLocal() {
        UTAnalytics.getInstance().saveCacheDataToLocal();
    }

    public void setToAliyunOsPlatform() {
        UTAnalytics.getInstance().setToAliyunOsPlatform();
    }

    public void closeAuto1010Track() {
        ClientVariables.getInstance().set1010AutoTrackClose();
    }

    public String getUtsid() {
        try {
            String appKey = ClientVariables.getInstance().getAppKey();
            String utdid = UTDevice.getUtdid(ClientVariables.getInstance().getContext());
            long sessionTimestamp = Long.parseLong(AnalyticsMgr.getValue("session_timestamp"));
            if (!StringUtils.isEmpty(appKey) && !StringUtils.isEmpty(utdid)) {
                return utdid + "_" + appKey + "_" + sessionTimestamp;
            }
        } catch (Exception e) {
            Logger.w("", e, new Object[0]);
        }
        return null;
    }

    public void setHostPort4Tnet(Context context, String host, int port) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else if (TextUtils.isEmpty(host)) {
            Log.w("UTAnalytics", "host or port is empty");
        } else {
            SpSetting.put(context, TnetHostPortMgr.TAG_TNET_HOST_PORT, host + SymbolExpUtil.SYMBOL_COLON + port);
        }
    }

    public void clearHostPort4Tnet(Context context) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else {
            SpSetting.put(context, TnetHostPortMgr.TAG_TNET_HOST_PORT, (String) null);
        }
    }

    public void setHost4Https(Context context, String host) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else if (TextUtils.isEmpty(host)) {
            Log.w("UTAnalytics", "host or port is empty");
        } else {
            SpSetting.put(context, HttpsHostPortMgr.TAG_HTTPS_HOST_PORT, host);
        }
    }

    public void clearHost4Https(Context context) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else {
            SpSetting.put(context, HttpsHostPortMgr.TAG_HTTPS_HOST_PORT, (String) null);
        }
    }

    public void setHostPort4Http(Context context, String host) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else if (TextUtils.isEmpty(host)) {
            Log.w("UTAnalytics", "host  is empty");
        } else {
            SpSetting.put(context, Constants.UT.TAG_SP_HTTP_TRANSFER_HOST, host);
        }
    }

    public void clearHostPort4Http(Context context) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else {
            SpSetting.put(context, Constants.UT.TAG_SP_HTTP_TRANSFER_HOST, (String) null);
        }
    }

    public void setHost4TimeAdjustService(Context context, String host) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else if (TextUtils.isEmpty(host)) {
            Log.w("UTAnalytics", "host is empty");
        } else {
            SpSetting.put(context, TimeStampAdjustMgr.TAG_TIME_ADJUST_HOST_PORT, host);
        }
    }

    public void clearHost4TimeAdjustService(Context context) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else {
            SpSetting.put(context, TimeStampAdjustMgr.TAG_TIME_ADJUST_HOST_PORT, (String) null);
        }
    }

    public void registerExposureViewHandler(ExposureViewHandle handle) {
        TrackerManager.getInstance().registerExposureViewHandler(handle);
    }

    public void unRegisterExposureViewHandler(ExposureViewHandle handle) {
        TrackerManager.getInstance().unRegisterExposureViewHandler(handle);
    }

    public ExposureViewHandle getExposureViewHandler(Activity activity) {
        return TrackerManager.getInstance().getExposureViewHandle();
    }

    public void setExposureTagForWeex(View view) {
        ExposureUtils.setExposureForWeex(view);
    }

    public boolean startExpoTrack(Activity activity) {
        return TrackerManager.getInstance().addToTrack(activity);
    }

    public boolean stopExpoTrack(Activity activity) {
        return TrackerManager.getInstance().removeToTrack(activity);
    }

    public void setIgnoreTagForExposureView(View view) {
        ExposureUtils.setIgnoreTagForExposureView(view);
    }

    public void clearIgnoreTagForExposureView(View view) {
        ExposureUtils.clearIgnoreTagForExposureView(view);
    }
}
