package com.yunos.tvtaobao.blitz;

import android.content.Context;
import android.text.TextUtils;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.listener.BzJsCallUIListener;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class TaobaoUIBzJsCallUIListener implements BzJsCallUIListener {
    private final String TAG = "TaobaoUIBzJsCallUIListener";

    public String onUILoading(Context context, String param) {
        ZpLogger.i("TaobaoUIBzJsCallUIListener", "onUILoading , param  = " + param);
        Context ctx = (Context) new WeakReference<>(context).get();
        BzResult result = new BzResult();
        try {
            final boolean isShow = new JSONObject(param).optBoolean("show");
            if (ctx == null || !(ctx instanceof BaseActivity)) {
                result.setResult("HY_FAILED");
                return result.toJsonString();
            }
            final BaseActivity taoBaoBlitzActivity = (BaseActivity) ctx;
            result.setSuccess();
            taoBaoBlitzActivity.runOnUiThread(new Runnable() {
                public void run() {
                    ZpLogger.d("TaobaoUIBzJsCallUIListener", "thread id:" + Thread.currentThread().getId() + ",thread name:" + Thread.currentThread().getName() + ",isShow:" + isShow);
                    taoBaoBlitzActivity.OnWaitProgressDialog(isShow);
                }
            });
            return result.toJsonString();
        } catch (JSONException e) {
            ZpLogger.e("TaobaoUIBzJsCallUIListener", "TaobaoUI: param parse to JSON error, param=" + param);
            result.setResult("HY_FAILED");
        }
    }

    public String onUIDialog(Context context, String param) {
        ZpLogger.i("TaobaoUIBzJsCallUIListener", "onUIDialog , param  = " + param);
        Context ctx = (Context) new WeakReference<>(context).get();
        BzResult result = new BzResult();
        try {
            JSONObject jsObj = new JSONObject(param);
            final boolean isFinishActivity = jsObj.optBoolean("isFinishActivity");
            final String message = jsObj.optString("message");
            if (!TextUtils.isEmpty(message) && ctx != null && (ctx instanceof BaseActivity)) {
                final BaseActivity taoBaoBlitzActivity = (BaseActivity) ctx;
                result.setSuccess();
                taoBaoBlitzActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        taoBaoBlitzActivity.showErrorDialog(message, isFinishActivity);
                    }
                });
            }
        } catch (JSONException e) {
            ZpLogger.e("TaobaoUIBzJsCallUIListener", "TaobaoUI: param parse to JSON error, param=" + param);
        }
        return result.toJsonString();
    }

    public String onUINetworkDialog(Context context, String param) {
        ZpLogger.i("TaobaoUIBzJsCallUIListener", "onUINetworkDialog , param  = " + param);
        Context ctx = (Context) new WeakReference<>(context).get();
        BzResult result = new BzResult();
        try {
            final boolean isFinishActivity = new JSONObject(param).optBoolean("isFinishActivity");
            if (ctx != null && (ctx instanceof BaseActivity)) {
                final BaseActivity taoBaoBlitzActivity = (BaseActivity) ctx;
                result.setSuccess();
                taoBaoBlitzActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        taoBaoBlitzActivity.showNetworkErrorDialog(isFinishActivity);
                    }
                });
            }
        } catch (JSONException e) {
            ZpLogger.e("TaobaoUIBzJsCallUIListener", "TaobaoUI: param parse to JSON error, param=" + param);
        }
        return result.toJsonString();
    }

    public String onUIStopLoading(Context context, String s) {
        ZpLogger.i("TaobaoUIBzJsCallUIListener", "onUIStopLoading");
        return null;
    }
}
