package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.view.PopupWindowController;
import android.text.TextUtils;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WVUIActionSheet extends WVApiPlugin {
    private static final String TAG = "WVUIActionSheet";
    /* access modifiers changed from: private */
    public String _index;
    /* access modifiers changed from: private */
    public WVCallBackContext mCallback = null;
    /* access modifiers changed from: private */
    public PopupWindowController mPopupWindowController;
    private View.OnClickListener popupClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            WVResult result = new WVResult();
            result.addData("type", (String) v.getTag());
            result.addData("_index", WVUIActionSheet.this._index);
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVUIActionSheet", "ActionSheet: click: 8.0.0");
            }
            WVUIActionSheet.this.mPopupWindowController.hide();
            result.setSuccess();
            WVUIActionSheet.this.mCallback.success(result);
            WVUIActionSheet.this.mCallback.fireEvent("wv.actionsheet", result.toJsonString());
        }
    };

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (!"show".equals(action)) {
            return false;
        }
        show(callback, params);
        return true;
    }

    public synchronized void show(WVCallBackContext callback, String param) {
        String title = null;
        String[] mPopupMenuTags = null;
        if (!TextUtils.isEmpty(param)) {
            try {
                JSONObject jsObj = new JSONObject(param);
                title = jsObj.optString("title");
                this._index = jsObj.optString("_index");
                JSONArray jsArr = jsObj.optJSONArray("buttons");
                if (jsArr != null) {
                    if (jsArr.length() > 8) {
                        TaoLog.w("WVUIActionSheet", "WVUIDialog: ActionSheet is too long, limit 8");
                        WVResult result = new WVResult();
                        result.setResult("HY_PARAM_ERR");
                        result.addData("msg", "ActionSheet is too long. limit 8");
                        callback.error(result);
                    } else {
                        mPopupMenuTags = new String[jsArr.length()];
                        for (int i = 0; i < jsArr.length(); i++) {
                            mPopupMenuTags[i] = jsArr.optString(i);
                        }
                    }
                }
            } catch (JSONException e) {
                TaoLog.e("WVUIActionSheet", "WVUIDialog: param parse to JSON error, param=" + param);
                WVResult result2 = new WVResult();
                result2.setResult("HY_PARAM_ERR");
                callback.error(result2);
            }
        }
        this.mCallback = callback;
        this.mPopupWindowController = new PopupWindowController(this.mContext, this.mWebView.getView(), title, mPopupMenuTags, this.popupClickListener);
        this.mPopupWindowController.show();
        TaoLog.d("WVUIActionSheet", "ActionSheet: show");
        return;
    }

    public void onDestroy() {
        this.mCallback = null;
    }
}
