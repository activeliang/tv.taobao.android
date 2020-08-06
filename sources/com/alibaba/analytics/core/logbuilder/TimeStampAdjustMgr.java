package com.alibaba.analytics.core.logbuilder;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.HttpUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;
import com.alibaba.analytics.utils.TaskExecutor;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.json.JSONObject;

public class TimeStampAdjustMgr {
    public static final String TAG_TIME_ADJUST_HOST_PORT = "time_adjust_host";
    private static TimeStampAdjustMgr instance = new TimeStampAdjustMgr();
    /* access modifiers changed from: private */
    public String defaultHost = "acs.m.taobao.com";
    private String defaultUrl = "http://acs.m.taobao.com/gw/mtop.common.getTimestamp/*";
    /* access modifiers changed from: private */
    public long diff = 0;
    /* access modifiers changed from: private */
    public boolean flag = false;
    /* access modifiers changed from: private */
    public String scheme = "http://";
    /* access modifiers changed from: private */
    public String urlFile = "/gw/mtop.common.getTimestamp/*";

    public static TimeStampAdjustMgr getInstance() {
        return instance;
    }

    public void startSync() {
        TaskExecutor.getInstance().schedule((ScheduledFuture) null, new Runnable() {
            public void run() {
                long now = System.currentTimeMillis();
                String host = TimeStampAdjustMgr.this.defaultHost;
                String userSettingHost = SpSetting.get(Variables.getInstance().getContext(), TimeStampAdjustMgr.TAG_TIME_ADJUST_HOST_PORT);
                if (!TextUtils.isEmpty(userSettingHost)) {
                    host = userSettingHost;
                }
                String url = TimeStampAdjustMgr.this.scheme + host + TimeStampAdjustMgr.this.urlFile;
                HttpUtils.HttpResponse response = HttpUtils.sendRequest(1, url, (Map<String, Object>) null, false);
                Logger.d("TimeStampAdjustMgr", "url", url, "response", response);
                if (response != null && response.data != null) {
                    try {
                        JSONObject data = new JSONObject(new String(response.data, 0, response.data.length)).optJSONObject("data");
                        if (data != null) {
                            String tString = data.optString("t");
                            if (!TextUtils.isEmpty(tString)) {
                                try {
                                    long unused = TimeStampAdjustMgr.this.diff = Long.parseLong(tString) - now;
                                    boolean unused2 = TimeStampAdjustMgr.this.flag = true;
                                    Logger.d("TimeStampAdjustMgr", "t", tString, "now", Long.valueOf(now), "diff", Long.valueOf(TimeStampAdjustMgr.this.diff));
                                } catch (Throwable th) {
                                }
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0);
    }

    public long getCurrentMils() {
        return System.currentTimeMillis() + this.diff;
    }

    public long getCurrentMils(String time) {
        long recordTime = 0;
        try {
            recordTime = Long.parseLong(time);
        } catch (Exception e) {
            Logger.d("TimeStampAdjustMgr", e);
        }
        if (recordTime == 0) {
            recordTime = System.currentTimeMillis();
        }
        return this.diff + recordTime;
    }

    public boolean getAdjustFlag() {
        return this.flag;
    }
}
