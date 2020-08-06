package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.text.TextUtils;
import android.widget.Toast;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import java.net.URLDecoder;
import org.json.JSONObject;

public class WVUIToast extends WVApiPlugin {
    private static final String TAG = "WVUIToast";

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (!"toast".equals(action)) {
            return false;
        }
        toast(callback, params);
        return true;
    }

    public synchronized void toast(WVCallBackContext callback, String param) {
        String message = "";
        int duration = 0;
        if (!TextUtils.isEmpty(param)) {
            try {
                param = URLDecoder.decode(param, "utf-8");
                JSONObject jsObj = new JSONObject(param);
                message = jsObj.optString("message");
                duration = jsObj.optInt(VPMConstants.MEASURE_DURATION);
            } catch (Exception e) {
                Toast toast = Toast.makeText(this.mContext, param, 1);
                toast.setGravity(17, 0, 0);
                toast.show();
            }
        }
        if (!TextUtils.isEmpty(message)) {
            if (duration > 3) {
                duration = 1;
            }
            Toast toast2 = Toast.makeText(this.mContext, message, duration);
            toast2.setGravity(17, 0, 0);
            toast2.show();
        }
        callback.success();
        return;
    }
}
