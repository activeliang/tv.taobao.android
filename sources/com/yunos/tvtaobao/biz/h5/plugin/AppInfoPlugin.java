package com.yunos.tvtaobao.biz.h5.plugin;

import android.content.Context;
import com.ut.device.UTDevice;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tvtaobao.biz.activity.TaoBaoBlitzActivity;
import com.yunos.tvtaobao.payment.utils.TvTaoUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class AppInfoPlugin {
    private static final String CHANNEL = "channel";
    private static final String DATA = "data";
    private static final String PACKAGE_NAME = "packageName";
    private static final String STBID = "stbid";
    /* access modifiers changed from: private */
    public static final String TAG = AppInfoPlugin.class.getSimpleName();
    private static final String UMTOKEN = "umtoken";
    private static final String UTDID = "utdid";
    private static final String UUID = "uuid";
    private static final String VERSION_NAME = "version_name";
    private static final String WUA = "wua";
    private static final String jsFuncName = "getExAppInfo";
    private AppInfoJsCallback mAppInfoJsCallback;
    private WeakReference<TaoBaoBlitzActivity> mTaoBaoBlitzActivityWeakReference;

    public AppInfoPlugin(WeakReference<TaoBaoBlitzActivity> taoBaoBlitzActivityWeakReference) {
        this.mTaoBaoBlitzActivityWeakReference = taoBaoBlitzActivityWeakReference;
        onInitPlugin();
    }

    private void onInitPlugin() {
        this.mAppInfoJsCallback = new AppInfoJsCallback(new WeakReference(this));
        BlitzPlugin.bindingJs(jsFuncName, this.mAppInfoJsCallback);
    }

    /* access modifiers changed from: private */
    public void onHandleCall(String param, long cbData) {
        String stbid = DeviceUtil.getStbID();
        String channelId = Config.getChannel();
        String packageName = AppInfo.getPackageName();
        String versionName = AppInfo.getAppVersionName();
        JSONObject data = new JSONObject();
        try {
            data.put(STBID, stbid);
            data.put("channel", channelId);
            data.put("packageName", packageName);
            data.put(VERSION_NAME, versionName);
            data.put(UUID, CloudUUIDWrapper.getCloudUUID());
            data.put("utdid", UTDevice.getUtdid((Context) this.mTaoBaoBlitzActivityWeakReference.get()));
            data.put("wua", Config.getWua((Context) this.mTaoBaoBlitzActivityWeakReference.get()));
            data.put(UMTOKEN, TvTaoUtils.getUmtoken((Context) this.mTaoBaoBlitzActivityWeakReference.get()));
        } catch (JSONException e) {
            ZpLogger.e(TAG, e.getMessage());
        }
        successResponseJs(data, cbData);
    }

    private static void successResponseJs(JSONObject data, long cbData) {
        BzResult result = new BzResult("HY_SUCCESS");
        if (data != null) {
            result.addData("data", data);
        }
        BlitzPlugin.responseJs(true, result.toJsonString(), cbData);
    }

    private static class AppInfoJsCallback implements BlitzPlugin.JsCallback {
        private WeakReference<AppInfoPlugin> mReference;

        private AppInfoJsCallback(WeakReference<AppInfoPlugin> reference) {
            this.mReference = reference;
        }

        public void onCall(String param, long cbData) {
            ZpLogger.v(AppInfoPlugin.TAG, "onCall --> param  =" + param + ";  cbData = " + cbData);
            if (this.mReference != null && this.mReference.get() != null) {
                ((AppInfoPlugin) this.mReference.get()).onHandleCall(param, cbData);
            }
        }
    }
}
