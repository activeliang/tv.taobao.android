package com.yunos.tvtaobao.blitz;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.yunos.tv.blitz.listener.BzPageStatusListener;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.yunos.tv.core.util.MonitorUtil;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class TaobaoBzPageStatusListener implements BzPageStatusListener {
    private static final String ERROR_CODE = "errorcode";
    private static final String TAG = "TaobaoBzPageStatusListener";
    private long loadBeginTime;
    private LOAD_MODE mLoadMode = LOAD_MODE.URL_MODE;
    private String url;

    public void onPageLoadError(Context context, String url2, String param) {
        ZpLogger.i(TAG, "onPageLoadError -->  mLoadMode = " + this.mLoadMode + ";  url = " + url2 + "; param = " + param);
        String errorcode = "";
        if (!TextUtils.isEmpty(param)) {
            try {
                JSONObject error_param = new JSONObject(param);
                if (error_param != null) {
                    errorcode = error_param.optString(ERROR_CODE);
                }
            } catch (JSONException e) {
            }
        }
        Context ctx = (Context) new WeakReference<>(context).get();
        if (ctx != null && (ctx instanceof BaseActivity)) {
            final BaseActivity taoBaoBlitzActivity = (BaseActivity) ctx;
            if (!TextUtils.isEmpty(url2)) {
                Map<String, String> params = Utils.getProperties();
                params.put("url", url2);
                if (!TextUtils.isEmpty(errorcode)) {
                    params.put(ERROR_CODE, errorcode);
                }
                Utils.utCustomHit(taoBaoBlitzActivity.getFullPageName(), "load_h5_error", params);
            }
            taoBaoBlitzActivity.runOnUiThread(new Runnable() {
                public void run() {
                    taoBaoBlitzActivity.OnWaitProgressDialog(false);
                    taoBaoBlitzActivity.showErrorDialog("加载页面失败,请稍后重试", true);
                }
            });
        }
        if (this.url != null && this.url.equals(url2)) {
            DimensionValueSet dimensionValueSet = MonitorUtil.createDimensionValueSet(context);
            dimensionValueSet.setValue(BlitzServiceUtils.CSUCCESS, "false");
            Uri uri = Uri.parse(url2);
            if (uri.isHierarchical()) {
                dimensionValueSet.setValue("domain", uri.getHost());
                dimensionValueSet.setValue("url", uri.getPath());
            }
            MeasureValueSet measureValueSet = MeasureValueSet.create();
            measureValueSet.setValue("loadTime", (double) (System.currentTimeMillis() - this.loadBeginTime));
            AppMonitor.Stat.commit("tvtaobao", "pageload", dimensionValueSet, measureValueSet);
        }
    }

    public void onPageLoadFinished(Context context, final String url2) {
        ZpLogger.i(TAG, "onPageLoadFinished -->  mLoadMode = " + this.mLoadMode + ";  url = " + url2 + "; context = " + context);
        Context ctx = (Context) new WeakReference<>(context).get();
        if (ctx != null && (ctx instanceof BaseActivity)) {
            final BaseActivity taoBaoBlitzActivity = (BaseActivity) ctx;
            Map<String, String> p = Utils.getProperties();
            p.put("url", url2);
            Utils.utCustomHit(taoBaoBlitzActivity.getFullPageName(), "load_h5_finish", p);
            taoBaoBlitzActivity.runOnUiThread(new Runnable() {
                public void run() {
                    taoBaoBlitzActivity.OnWaitProgressDialog(false);
                    if (!TextUtils.isEmpty(url2)) {
                        taoBaoBlitzActivity.onWebviewPageDone(url2);
                    }
                }
            });
        }
        if (this.url != null && this.url.equals(url2)) {
            DimensionValueSet dimensionValueSet = MonitorUtil.createDimensionValueSet(context);
            dimensionValueSet.setValue(BlitzServiceUtils.CSUCCESS, "true");
            Uri uri = Uri.parse(url2);
            if (uri.isHierarchical()) {
                dimensionValueSet.setValue("domain", uri.getHost());
                dimensionValueSet.setValue("url", uri.getPath());
            }
            MeasureValueSet measureValueSet = MeasureValueSet.create();
            measureValueSet.setValue("loadTime", (double) (System.currentTimeMillis() - this.loadBeginTime));
            AppMonitor.Stat.commit("tvtaobao", "pageload", dimensionValueSet, measureValueSet);
        }
    }

    public void onPageLoadStart(Context context, String url2) {
        ZpLogger.i(TAG, "onPageLoadStart -->  mLoadMode = " + this.mLoadMode + ";  url = " + url2);
        this.url = url2;
        try {
            JSONObject data = new JSONObject(url2);
            if (data != null && data.has("url")) {
                this.url = data.optString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.loadBeginTime = System.currentTimeMillis();
        Context ctx = (Context) new WeakReference<>(context).get();
        if (!TextUtils.isEmpty(url2)) {
            Map<String, String> p = Utils.getProperties();
            p.put("url", url2);
            if (ctx != null && (ctx instanceof BaseActivity)) {
                Utils.utCustomHit(((BaseActivity) ctx).getFullPageName(), "load_h5_start", p);
            }
        }
    }

    public enum LOAD_MODE {
        URL_MODE(0),
        DATA_MODE(1);
        
        private int value;

        private LOAD_MODE(int num) {
            this.value = num;
        }

        public int getValue() {
            return this.value;
        }
    }
}
