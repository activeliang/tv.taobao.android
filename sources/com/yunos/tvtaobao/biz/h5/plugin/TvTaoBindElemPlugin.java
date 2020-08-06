package com.yunos.tvtaobao.biz.h5.plugin;

import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tvtaobao.biz.activity.TaoBaoBlitzActivity;
import com.yunos.tvtaobao.biz.interfaces.IElemBindCallBack;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class TvTaoBindElemPlugin {
    /* access modifiers changed from: private */
    public static WeakReference<TaoBaoBlitzActivity> mRefActivity;
    private TvTaoBindElemCallback mJsCallback = new TvTaoBindElemCallback(new WeakReference(this));

    public TvTaoBindElemPlugin(WeakReference<TaoBaoBlitzActivity> refActivity) {
        mRefActivity = refActivity;
        BlitzPlugin.bindingJs("tvtaobao_to_bind_elem", this.mJsCallback);
        ZpLogger.d("TvTaoBindElemPlugin", "TvTaoBindElemPluginCreate");
    }

    private static class TvTaoBindElemCallback implements BlitzPlugin.JsCallback {
        private WeakReference<TvTaoBindElemPlugin> refPlugin;

        TvTaoBindElemCallback(WeakReference<TvTaoBindElemPlugin> plugin) {
            ZpLogger.d("TvTaoBindElemPlugin", "TvTaoBindElemPlugin-TvTaoBindElemCallback");
            this.refPlugin = plugin;
        }

        public void onCall(String param, final long cbData) {
            ZpLogger.d("TvTaoBindElemPlugin", "param:" + param + ", cbData:" + cbData);
            ((TaoBaoBlitzActivity) TvTaoBindElemPlugin.mRefActivity.get()).setElemBindCallBack(new IElemBindCallBack() {
                public void onSuccess(Object o) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("bind_success", true);
                        BlitzPlugin.responseJs(true, object.toString(), cbData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void cancel() {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("bind_success", false);
                        BlitzPlugin.responseJs(true, object.toString(), cbData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
