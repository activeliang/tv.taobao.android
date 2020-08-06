package com.yunos.tvtaobao.biz.h5.plugin;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import com.ut.mini.UTAnalytics;
import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.util.ActivityPathRecorder;
import com.yunos.tv.core.util.DeviceUtil;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class TvTaoBaoBlitzPlugin {
    private static String ERROR = "error";
    private static String KEY = "key";
    /* access modifiers changed from: private */
    public static String RESULT = "result";
    /* access modifiers changed from: private */
    public static String TAG = "CommonPlugin";
    private static String VALUE = "value";
    private static Handler handler = new Handler();
    private static TvTaoBaoBlitzPlugin instance;
    private static BlitzPlugin.JsCallback mExtParamsGetCallback = new BlitzPlugin.JsCallback() {
        public void onCall(String param, long cdData) {
            String extParams = Config.getExtParams();
            if (!TextUtils.isEmpty(extParams)) {
                BzResult result = new BzResult();
                result.addData("extParams", extParams);
                result.setSuccess();
                BlitzPlugin.responseJs(true, result.toJsonString(), cdData);
                return;
            }
            TvTaoBaoBlitzPlugin.responseFalse(cdData);
        }
    };
    private static BlitzPlugin.JsCallback mGetAppkeyCallback = new BlitzPlugin.JsCallback() {
        public void onCall(String param, long cdData) {
            String appKey = Config.getChannel();
            ZpLogger.i(TvTaoBaoBlitzPlugin.TAG, "mGetAppkeyCallback  appKey == " + appKey);
            if (appKey != null) {
                BzResult result = new BzResult();
                result.addData(TvTaoBaoBlitzPlugin.RESULT, "true");
                result.addData("appKey", appKey);
                result.setSuccess();
                BlitzPlugin.responseJs(true, result.toJsonString(), cdData);
                return;
            }
            TvTaoBaoBlitzPlugin.responseFalse(cdData);
        }
    };
    private static BlitzPlugin.JsCallback mStbIDGetCallback = new BlitzPlugin.JsCallback() {
        public void onCall(String param, long cdData) {
            String stbId = DeviceUtil.initMacAddress(TvTaoBaoBlitzPlugin.getInstance().currActivity);
            ZpLogger.i(TvTaoBaoBlitzPlugin.TAG, "mStbIDGetCallback  stb == " + stbId);
            if (stbId != null) {
                BzResult result = new BzResult();
                result.addData(TvTaoBaoBlitzPlugin.RESULT, "true");
                result.addData("stbID", stbId);
                result.setSuccess();
                BlitzPlugin.responseJs(true, result.toJsonString(), cdData);
                return;
            }
            TvTaoBaoBlitzPlugin.responseFalse(cdData);
        }
    };
    private static BlitzPlugin.JsCallback updateNextPageProperties = new BlitzPlugin.JsCallback() {
        public void onCall(String param, long cdData) {
            try {
                String spm_url = new JSONObject(param).getString("spm_url");
                if (!TextUtils.isEmpty(spm_url)) {
                    Map<String, String> nextparam = new HashMap<>();
                    ZpLogger.i(TvTaoBaoBlitzPlugin.TAG, spm_url);
                    nextparam.put(Constants.PARAM_OUTER_SPM_URL, spm_url);
                    UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(nextparam);
                    BzResult result = new BzResult();
                    result.addData(TvTaoBaoBlitzPlugin.RESULT, "true");
                    result.setSuccess();
                    BlitzPlugin.responseJs(true, result.toJsonString(), cdData);
                    return;
                }
                TvTaoBaoBlitzPlugin.responseFalse(cdData);
            } catch (Exception e) {
                TvTaoBaoBlitzPlugin.responseFalse(cdData);
                e.printStackTrace();
            }
        }
    };
    /* access modifiers changed from: private */
    public Activity currActivity;
    private BlitzPlugin.JsCallback mGetActivityPathCallback = new BlitzPlugin.JsCallback() {
        public void onCall(String param, long cdData) {
            if (TvTaoBaoBlitzPlugin.getInstance().currActivity instanceof ActivityPathRecorder.PathNode) {
                List<String> pathList = ActivityPathRecorder.getInstance().getCurrentPath((ActivityPathRecorder.PathNode) TvTaoBaoBlitzPlugin.getInstance().currActivity);
                if (pathList != null) {
                    BzResult result = new BzResult();
                    result.addData(TvTaoBaoBlitzPlugin.RESULT, "true");
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < pathList.size(); i++) {
                        array.put(pathList.get(i));
                    }
                    result.addData("activityPath", array != null ? array.toString() : "");
                    ZpLogger.i(TvTaoBaoBlitzPlugin.TAG, new StringBuilder().append("mGetActivityPathCallback activityPath == ").append(array).toString() != null ? array.toString() : "");
                    result.setSuccess();
                    BlitzPlugin.responseJs(true, result.toJsonString(), cdData);
                    return;
                }
                TvTaoBaoBlitzPlugin.responseFalse(cdData);
            }
        }
    };

    private TvTaoBaoBlitzPlugin() {
        ZpLogger.i(TAG, "mStbIDGetCallback");
        BlitzPlugin.bindingJs("tvtaobao_stbId_get", mStbIDGetCallback);
        ZpLogger.i(TAG, "onInitupdateNextPageProperties");
        BlitzPlugin.bindingJs("tvtaobao_spm_update_next_page_properties", updateNextPageProperties);
        ZpLogger.i(TAG, "mGetAppkeyCallback");
        BlitzPlugin.bindingJs("tvtaobao_appkey_get", mGetAppkeyCallback);
        ZpLogger.i(TAG, "mGetActivityPathCallback");
        BlitzPlugin.bindingJs("tvtaobao_activity_path_get", this.mGetActivityPathCallback);
        ZpLogger.i(TAG, "mExtParamsGetCallback");
        BlitzPlugin.bindingJs("tvtaobao_extParams_get", mExtParamsGetCallback);
    }

    public static TvTaoBaoBlitzPlugin getInstance() {
        if (instance == null) {
            synchronized (TvTaoBaoBlitzPlugin.class) {
                if (instance == null) {
                    instance = new TvTaoBaoBlitzPlugin();
                }
            }
        }
        return instance;
    }

    public static void register(Activity activity) {
        getInstance().currActivity = activity;
    }

    public static void unregister(Activity activity) {
        if (getInstance().currActivity == activity) {
            getInstance().currActivity = null;
        }
    }

    /* access modifiers changed from: private */
    public static void responseFalse(long cdData) {
        BzResult result = new BzResult();
        result.addData(RESULT, "false");
        BlitzPlugin.responseJs(false, result.toJsonString(), cdData);
    }
}
