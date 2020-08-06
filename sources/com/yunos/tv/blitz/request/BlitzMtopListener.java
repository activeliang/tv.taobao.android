package com.yunos.tv.blitz.request;

import android.content.Context;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.request.common.AppDebug;
import com.yunos.tv.blitz.request.common.RequestListener;
import com.yunos.tv.blitz.request.core.ServiceCode;
import java.lang.ref.WeakReference;
import org.json.JSONArray;
import org.json.JSONObject;

public class BlitzMtopListener implements RequestListener<JSONObject> {
    public static final String TAG = "BlitzMtopListener";
    private int mAddrCallback;
    WeakReference<Context> mContext = null;
    private WeakReference<BzBaseActivity> mzActivityRef = null;
    public String param_str;

    public void setRequestStr(String param) {
        this.param_str = param;
    }

    public BlitzMtopListener(WeakReference<Context> ctx, int addr_callback) {
        this.mAddrCallback = addr_callback;
        if (ctx != null) {
            this.mContext = ctx;
        }
    }

    public boolean onError(int resultCode, String msg) {
        AppDebug.i(TAG, "BlitzMtopListener --> onError -->  resultCode = " + resultCode + ";  msg = " + msg);
        BzResult result = new BzResult();
        result.addData("code", resultCode);
        result.addData("msg", msg);
        replyCallBack(result, false);
        if (!(ServiceCode.ANDROID_SYS_NO_NETWORK.getCode() == resultCode || ServiceCode.FAIL_SYS_SESSION_EXPIRED.getCode() == resultCode || ServiceCode.ANDROID_SYS_JSONDATA_BLANK.getCode() != resultCode)) {
        }
        return false;
    }

    public void onSuccess(JSONObject data, JSONArray ret) {
        AppDebug.i(TAG, "BlitzMtopListener --> onSuccess --> data = " + data);
        JSONObject data_data = null;
        if (data != null) {
            try {
                data_data = data.optJSONObject("data");
            } catch (Exception e) {
            }
        }
        BzResult result = new BzResult();
        if (data_data == null) {
            result.addData("data", data);
        } else {
            result.addData("data", data_data);
        }
        result.addData("code", 200);
        result.setSuccess();
        replyCallBack(result, true);
    }

    /* access modifiers changed from: package-private */
    public boolean replyCallBack(BzResult bzResult, boolean success) {
        if (this.mContext != null) {
            BzAppConfig.context.replyCallBack(this.mAddrCallback, success, bzResult.toJsonString());
        }
        return false;
    }

    public void onRequestDone(JSONObject data, int resultCode, String msg, JSONArray ret, String raw_data) {
        String str_cooked;
        JSONObject cookedData = data;
        String cookedMsg = msg;
        try {
            if (!(BzAppConfig.context.getMtopParamListener() == null || this.mContext.get() == null || (str_cooked = BzAppConfig.context.getMtopParamListener().getCookedDataForJs((Context) this.mContext.get(), raw_data)) == null)) {
                cookedMsg = str_cooked;
                cookedData = new JSONObject(str_cooked);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (resultCode == 200) {
            onSuccess(cookedData, (JSONArray) null);
        } else {
            onError(resultCode, cookedMsg);
        }
        if (BzAppConfig.context.getMiscListener() != null) {
            String data_s = "";
            if (data != null) {
                data_s = data.toString();
            }
            BzAppConfig.context.getMiscListener().onGetMtopResponse(this.param_str, resultCode, data_s, msg);
        }
    }
}
