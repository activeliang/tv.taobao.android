package android.taobao.windvane.jsbridge.api;

import android.app.Activity;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.DigestUtils;
import android.text.TextUtils;
import java.io.File;
import org.json.JSONObject;

public class WVScreen extends WVApiPlugin {
    private static final String TAG = "WVScreen";

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("capture".equals(action)) {
            capture(callback, params);
        } else if ("getOrientation".equals(action)) {
            getOrientation(callback, params);
        } else if (!"setOrientation".equals(action)) {
            return false;
        } else {
            setOrientation(callback, params);
        }
        return true;
    }

    public void capture(WVCallBackContext callback, String param) {
        boolean isInAlbum;
        WVResult result = new WVResult();
        String inAlbum = "";
        long time = 0;
        if (!TextUtils.isEmpty(param)) {
            try {
                inAlbum = new JSONObject(param).optString("inAlbum");
            } catch (Exception e) {
                result = new WVResult("HY_PARAM_ERR");
                callback.error(result);
            }
        }
        if ("false".equals(inAlbum)) {
            isInAlbum = false;
        } else {
            isInAlbum = true;
        }
        try {
            time = ScreenCaptureUtil.capture(this.mWebView.getView(), isInAlbum);
        } catch (Exception e2) {
            callback.error();
        }
        String url = WVUtils.getVirtualPath(Long.valueOf(time));
        result.addData("url", url);
        result.addData("localPath", WVCacheManager.getInstance().getCacheDir(true) + File.separator + DigestUtils.md5ToHex(url));
        callback.success(result);
    }

    public void getOrientation(WVCallBackContext callback, String param) {
        String orientationString;
        WVResult result = new WVResult();
        if (!(this.mContext instanceof Activity)) {
            result.addData("error", "Context must be Activty!");
            callback.error(result);
        }
        int orientation = ((Activity) this.mContext).getRequestedOrientation();
        if (orientation == 0) {
            orientationString = "landscape";
        } else if (orientation == 1) {
            orientationString = "portrait";
        } else {
            orientationString = "unknown";
        }
        result.addData("orientation", orientationString);
        callback.success(result);
    }

    public void setOrientation(WVCallBackContext callback, String param) {
        new WVResult();
        String orientation = "";
        if (!TextUtils.isEmpty(param)) {
            try {
                orientation = new JSONObject(param).optString("orientation", "");
            } catch (Exception e) {
                callback.error(new WVResult("HY_PARAM_ERR"));
            }
        }
        if (!(this.mContext instanceof Activity)) {
            WVResult result = new WVResult();
            result.addData("error", "Context must be Activty!");
            callback.error(result);
        }
        Activity activity = (Activity) this.mContext;
        if (orientation.equalsIgnoreCase("landscape")) {
            activity.setRequestedOrientation(0);
        } else if (orientation.equalsIgnoreCase("portrait")) {
            activity.setRequestedOrientation(1);
        } else {
            callback.error();
            return;
        }
        callback.success();
    }
}
