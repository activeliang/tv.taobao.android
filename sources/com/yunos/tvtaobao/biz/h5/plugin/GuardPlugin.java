package com.yunos.tvtaobao.biz.h5.plugin;

import android.content.Context;
import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.activity.TaoBaoBlitzActivity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class GuardPlugin {
    private GuardJsCallback mJsCallback = new GuardJsCallback(new WeakReference(this), this.mRefActivity);
    private WeakReference<TaoBaoBlitzActivity> mRefActivity;

    public GuardPlugin(WeakReference<TaoBaoBlitzActivity> refActivity) {
        this.mRefActivity = refActivity;
        BlitzPlugin.bindingJs("tvtaobao_guard", this.mJsCallback);
    }

    private static class GuardJsCallback implements BlitzPlugin.JsCallback {
        private WeakReference<TaoBaoBlitzActivity> mRefActivity;
        private WeakReference<GuardPlugin> refPlugin;

        GuardJsCallback(WeakReference<GuardPlugin> plugin, WeakReference<TaoBaoBlitzActivity> refActivity) {
            this.refPlugin = plugin;
            this.mRefActivity = refActivity;
        }

        public void onCall(String param, long cbData) {
            ZpLogger.d("GuardPlugin", "param:" + param + ", cbData:" + cbData);
            try {
                JSONObject object = new JSONObject();
                object.put("umToken", Config.getUmtoken((Context) this.mRefActivity.get()));
                object.put("wua", Config.getWua((Context) this.mRefActivity.get()));
                object.put("isSimulator", Config.isSimulator((Context) this.mRefActivity.get()));
                object.put("userAgent", Config.getAndroidSystem((Context) this.mRefActivity.get()));
                BlitzPlugin.responseJs(true, object.toString(), cbData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
