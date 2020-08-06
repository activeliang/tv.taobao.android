package android.taobao.windvane.extra.jsbridge;

import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.util.Log;
import java.util.Enumeration;
import org.json.JSONException;
import org.json.JSONObject;

public class WVWebPerformance extends WVApiPlugin {
    private static final String TAG = "WVWebPerformance";

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (TextUtils.equals("timing", action)) {
            timing(callback);
        } else if (!TextUtils.equals("jsBridgeHistory", action)) {
            return false;
        } else {
            jsBridgeHistory(callback);
        }
        return true;
    }

    public void timing(WVCallBackContext callback) {
        WVResult result = new WVResult("HY_NO_PERMISSION");
        if (this.mWebView instanceof WVUCWebView) {
            result = new WVResult("HY_SUCCESS");
            try {
                JSONObject jsonObject = ((WVUCWebView) this.mWebView).getH5MonitorDatas();
                Log.i(TAG, jsonObject.toString());
                result.setData(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                result.setResult("HY_FAILED");
            }
            callback.success(result);
        }
        callback.error(result);
    }

    public void jsBridgeHistory(WVCallBackContext callback) {
        WVResult result = new WVResult();
        try {
            IWVWebView iWVWebView = this.mWebView;
            Enumeration<String> keys = IWVWebView.JsbridgeHis.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                IWVWebView iWVWebView2 = this.mWebView;
                result.addData(key, (Object) IWVWebView.JsbridgeHis.get(key));
            }
            callback.success(result);
        } catch (Exception e) {
            result.addData("msg", e.getMessage());
            callback.error(result);
        }
    }
}
