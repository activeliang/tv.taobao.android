package android.taobao.windvane.jsbridge.api;

import android.media.ToneGenerator;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.util.TaoLog;
import org.json.JSONException;
import org.json.JSONObject;

public class WVNotification extends WVApiPlugin {
    private static final String TAG = "WVNotification";

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (!"beep".equals(action)) {
            return false;
        }
        beep(params, callback);
        return true;
    }

    public final void beep(String params, WVCallBackContext callback) {
        try {
            final int repeatTime = Integer.parseInt(new JSONObject(params).optString("count"));
            new Thread() {
                public void run() {
                    ToneGenerator toneGenerator = new ToneGenerator(1, 100);
                    for (int i = 0; i < repeatTime; i++) {
                        toneGenerator.startTone(24);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    toneGenerator.stopTone();
                    toneGenerator.release();
                }
            }.start();
            callback.success("{}");
        } catch (JSONException e) {
            TaoLog.e("WVNotification", "openWindow: param parse to JSON error, param=" + params);
            callback.error("param error");
        }
    }
}
