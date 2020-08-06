package com.yunos.tvtaobao.biz.h5.plugin;

import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.core.RtEnv;
import com.yunos.tvtaobao.biz.activity.TaoBaoBlitzActivity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class TvTaoSdkGetSubkeyPlugin {
    private TvTaoSdkGetSubkeyCallback mJsCallback = new TvTaoSdkGetSubkeyCallback(new WeakReference(this));
    private WeakReference<TaoBaoBlitzActivity> mRefActivity;

    public TvTaoSdkGetSubkeyPlugin(WeakReference<TaoBaoBlitzActivity> refActivity) {
        this.mRefActivity = refActivity;
        BlitzPlugin.bindingJs("tvtaobao_get_subkey", this.mJsCallback);
    }

    private static class TvTaoSdkGetSubkeyCallback implements BlitzPlugin.JsCallback {
        private WeakReference<TvTaoSdkGetSubkeyPlugin> refPlugin;

        TvTaoSdkGetSubkeyCallback(WeakReference<TvTaoSdkGetSubkeyPlugin> plugin) {
            this.refPlugin = plugin;
        }

        public void onCall(String param, long cbData) {
            ZpLogger.d("TvTaoSdkGetSubkeyPlugin", "param:" + param + ", cbData:" + cbData);
            try {
                JSONObject object = new JSONObject();
                object.put("appKey", (String) RtEnv.get("APPKEY"));
                object.put("subkey", (String) RtEnv.get(RtEnv.SUBKEY));
                BlitzPlugin.responseJs(true, object.toString(), cbData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
