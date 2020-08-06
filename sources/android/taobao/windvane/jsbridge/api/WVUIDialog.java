package android.taobao.windvane.jsbridge.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.tvtaobao.voicesdk.register.type.ActionType;
import org.json.JSONException;
import org.json.JSONObject;

public class WVUIDialog extends WVApiPlugin {
    private static final String TAG = "WVUIDialog";
    /* access modifiers changed from: private */
    public String _index;
    /* access modifiers changed from: private */
    public String cancelBtnText = "";
    protected DialogInterface.OnClickListener confirmClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            WVResult result = new WVResult();
            String btnText = "";
            if (which == -1) {
                btnText = WVUIDialog.this.okBtnText;
            } else if (which == -2) {
                btnText = WVUIDialog.this.cancelBtnText;
            }
            result.addData("type", btnText);
            result.addData("_index", WVUIDialog.this._index);
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVUIDialog", "click: " + btnText);
            }
            result.setSuccess();
            if (WVUIDialog.this.mCallback != null) {
                WVUIDialog.this.mCallback.fireEvent("wv.dialog", result.toJsonString());
                WVUIDialog.this.mCallback.success(result);
            }
        }
    };
    /* access modifiers changed from: private */
    public String identifier;
    /* access modifiers changed from: private */
    public WVCallBackContext mCallback = null;
    /* access modifiers changed from: private */
    public String okBtnText = "";

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (this.mContext instanceof Activity) {
            this.mCallback = callback;
            if ("alert".equals(action)) {
                alert(callback, params);
            } else if (!ActionType.CONFIRM.equals(action)) {
                return false;
            } else {
                confirm(callback, params);
            }
        } else {
            WVResult result = new WVResult();
            result.addData("error", "Context must be Activity!!!");
            callback.error(result);
        }
        return true;
    }

    public synchronized void alert(WVCallBackContext callback, String param) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        if (!TextUtils.isEmpty(param)) {
            try {
                JSONObject jsObj = new JSONObject(param);
                builder.setTitle(jsObj.optString("title", "提示"));
                builder.setMessage(jsObj.optString("message"));
                this.okBtnText = jsObj.optString("okbutton");
                this.identifier = jsObj.optString("identifier");
                builder.setPositiveButton(this.okBtnText, new AlertListener());
            } catch (JSONException e) {
                TaoLog.e("WVUIDialog", "WVUIDialog: param parse to JSON error, param=" + param);
                WVResult result = new WVResult();
                result.setResult("HY_PARAM_ERR");
                callback.error(result);
            }
        }
        this.mCallback = callback;
        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.show();
        TaoLog.d("WVUIDialog", "alert: show");
        return;
    }

    public synchronized void confirm(WVCallBackContext callback, String param) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        if (!TextUtils.isEmpty(param)) {
            try {
                JSONObject jsObj = new JSONObject(param);
                builder.setTitle(jsObj.optString("message"));
                this.okBtnText = jsObj.optString("okbutton");
                builder.setPositiveButton(this.okBtnText, this.confirmClickListener);
                this.cancelBtnText = jsObj.optString("canclebutton");
                builder.setNegativeButton(this.cancelBtnText, this.confirmClickListener);
                this._index = jsObj.optString("_index");
            } catch (JSONException e) {
                TaoLog.e("WVUIDialog", "WVUIDialog: param parse to JSON error, param=" + param);
                WVResult result = new WVResult();
                result.setResult("HY_PARAM_ERR");
                callback.error(result);
            }
        }
        this.mCallback = callback;
        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.show();
        TaoLog.d("WVUIDialog", "confirm: show");
        return;
    }

    public void onDestroy() {
        this.mCallback = null;
        this.cancelBtnText = "";
        this.okBtnText = "";
    }

    protected class AlertListener implements DialogInterface.OnClickListener {
        protected AlertListener() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (WVUIDialog.this.mCallback != null) {
                WVResult result = new WVResult();
                if (!TextUtils.isEmpty(WVUIDialog.this.identifier)) {
                    result.addData("identifier", WVUIDialog.this.identifier);
                }
                result.setSuccess();
                if (WVUIDialog.this.mCallback != null) {
                    WVUIDialog.this.mCallback.fireEvent("WV.Event.Alert", result.toJsonString());
                    WVUIDialog.this.mCallback.success(result);
                }
            }
        }
    }
}
