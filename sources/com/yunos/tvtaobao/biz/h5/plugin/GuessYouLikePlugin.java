package com.yunos.tvtaobao.biz.h5.plugin;

import android.app.Activity;
import android.content.Intent;
import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tvtaobao.biz.activity.TaoBaoBlitzActivity;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class GuessYouLikePlugin {
    private GuessYouLikeJsCallback mJsCallback = new GuessYouLikeJsCallback(new WeakReference(this));
    /* access modifiers changed from: private */
    public WeakReference<TaoBaoBlitzActivity> mRefActivity;

    public GuessYouLikePlugin(WeakReference<TaoBaoBlitzActivity> refActivity) {
        this.mRefActivity = refActivity;
        BlitzPlugin.bindingJs("tvtaobao_home_to_like", this.mJsCallback);
        ZpLogger.d("GuessYouLikePlugin", "GuessYouLikePluginCreate");
    }

    private static class GuessYouLikeJsCallback implements BlitzPlugin.JsCallback {
        private WeakReference<GuessYouLikePlugin> refPlugin;

        GuessYouLikeJsCallback(WeakReference<GuessYouLikePlugin> plugin) {
            ZpLogger.d("GuessYouLikePlugin", "GuessYouLikePlugin-GuessYouLikeJsCallback");
            this.refPlugin = plugin;
        }

        public void onCall(String param, long cbData) {
            ZpLogger.d("GuessYouLikePlugin", "param:" + param + ", cbData:" + cbData);
            String bg = "";
            try {
                bg = new JSONObject(param).getString("bg");
                BlitzPlugin.responseJs(true, bg, cbData);
            } catch (JSONException e) {
                e.printStackTrace();
                ZpLogger.d("GuessYouLikePlugin", "error" + e.toString());
            }
            GuessYouLikePlugin.homeToGuessYouLike((Activity) ((GuessYouLikePlugin) this.refPlugin.get()).mRefActivity.get(), bg);
        }
    }

    public static void homeToGuessYouLike(Activity activity, String bgUrl) {
        Intent intent = new Intent();
        intent.setClassName(activity, BaseConfig.SWITCH_TO_GUESS_YOU_LIKE_ACTIVITY);
        intent.putExtra("guess_like_from", "home");
        intent.putExtra("guess_like_bg_url", bgUrl);
        activity.startActivity(intent);
    }
}
