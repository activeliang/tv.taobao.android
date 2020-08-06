package com.ut.mini.internal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.utils.Logger;
import com.ut.mini.module.appstatus.UTAppStatusCallbacks;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import java.util.HashMap;
import java.util.Map;

public class RealtimeDebugSwitch implements UTAppStatusCallbacks {
    static int i = 0;

    public void onSwitchBackground() {
    }

    public void onSwitchForeground() {
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (i == 0) {
            Intent i2 = activity.getIntent();
            if (i2 != null) {
                Uri uri = i2.getData();
                if (uri != null) {
                    String scheme = uri.getScheme();
                    if (scheme != null && scheme.startsWith("ut.")) {
                        String debugKey = uri.getQueryParameter("debugkey");
                        String isApSampling = uri.getQueryParameter("from");
                        if (scheme == null || !scheme.startsWith("ut.")) {
                            Logger.w((String) null, "scheme", scheme);
                            return;
                        }
                        Map<String, String> map = new HashMap<>();
                        map.put(Constants.RealTimeDebug.DEBUG_API_URL, "http://muvp.alibaba-inc.com/online/UploadRecords.do");
                        map.put(Constants.RealTimeDebug.DEBUG_KEY, debugKey);
                        map.put("from", isApSampling);
                        map.put(Constants.RealTimeDebug.DEBUG_SAMPLING_OPTION, "true");
                        UTTeamWork.getInstance().turnOnRealTimeDebug(map);
                        return;
                    }
                    return;
                }
                Logger.w((String) null, HuasuVideo.TAG_URI, uri);
                return;
            }
            Logger.w((String) null, "i ", i2);
        }
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
        i--;
    }

    public void onActivityResumed(Activity activity) {
        i++;
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }
}
