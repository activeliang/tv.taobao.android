package android.taobao.windvane.jsbridge.api;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.api.ShakeListener;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

public class WVMotion extends WVApiPlugin implements Handler.Callback {
    private static final int SHAKE_STOP = 1;
    private static final String TAG = "WVMotion";
    private BlowSensor blowSensor;
    /* access modifiers changed from: private */
    public long currentTime = 0;
    /* access modifiers changed from: private */
    public long frequency = 0;
    private Handler handler = new Handler(Looper.getMainLooper(), this);
    /* access modifiers changed from: private */
    public WVCallBackContext mCallback = null;
    protected SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (9 == event.sensor.getType() && WVMotion.this.frequency <= System.currentTimeMillis() - WVMotion.this.currentTime) {
                float[] values = event.values;
                String data = "{\"x\":\"" + ((-values[0]) / 10.0f) + "\",\"y\":\"" + ((-values[1]) / 10.0f) + "\",\"z\":\"" + ((-values[2]) / 10.0f) + "\"}";
                if (WVMotion.this.mCallback != null) {
                    WVMotion.this.mCallback.fireEvent("motion.gyro", data);
                } else {
                    WVMotion.this.stopListenGyro();
                }
                long unused = WVMotion.this.currentTime = System.currentTimeMillis();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private ShakeListener mShakeListener = null;
    private SensorManager sm = null;
    private Vibrator vibrator;

    public boolean execute(String action, final String params, final WVCallBackContext callback) {
        if ("listeningShake".equals(action)) {
            listeningShake(callback, params);
        } else if ("vibrate".equals(action)) {
            vibrate(callback, params);
        } else if ("listenBlow".equals(action)) {
            try {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.RECORD_AUDIO"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        WVMotion.this.listenBlow(callback, params);
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult result = new WVResult();
                        result.addData("msg", "NO_PERMISSION");
                        callback.error(result);
                    }
                }).execute();
            } catch (Exception e) {
            }
        } else if ("stopListenBlow".equals(action)) {
            stopListenBlow(callback, params);
        } else if (!"listenGyro".equals(action)) {
            return false;
        } else {
            listenGyro(callback, params);
        }
        return true;
    }

    public synchronized void listeningShake(WVCallBackContext callback, String param) {
        WVResult result = new WVResult();
        boolean on = false;
        long frequency2 = 500;
        boolean isFail = false;
        if (!TextUtils.isEmpty(param)) {
            try {
                param = URLDecoder.decode(param, "utf-8");
            } catch (Exception e) {
                TaoLog.e("WVMotion", "listeningShake: param decode error, param=" + param);
                isFail = true;
            }
            try {
                JSONObject jsObj = new JSONObject(param);
                on = jsObj.getBoolean("on");
                frequency2 = jsObj.optLong("frequency");
            } catch (JSONException e2) {
                TaoLog.e("WVMotion", "listeningShake: param parse to JSON error, param=" + param);
                result.setResult("HY_PARAM_ERR");
                callback.error(result);
            }
        }
        if (isFail) {
            if (TaoLog.getLogStatus()) {
                TaoLog.w("WVMotion", "listeningShake: isFail");
            }
            callback.error(result);
        } else if (on) {
            TaoLog.d("WVMotion", "listeningShake: start ...");
            if (this.mShakeListener == null) {
                this.mShakeListener = new ShakeListener(this.mContext);
            }
            this.mShakeListener.setOnShakeListener(new MyShakeListener(callback, frequency2));
            callback.success(result);
        } else {
            TaoLog.d("WVMotion", "listeningShake: stop.");
            Message message = new Message();
            message.what = 1;
            message.obj = callback;
            if (this.handler != null) {
                this.handler.sendMessage(message);
            }
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                stopShake();
                if (msg.obj instanceof WVCallBackContext) {
                    ((WVCallBackContext) msg.obj).success(new WVResult());
                }
                return true;
            case BlowSensor.BLOW_HANDLER_BLOWING:
                if (!this.isAlive) {
                    return true;
                }
                WVResult result = new WVResult();
                result.setSuccess();
                result.addData("pass", "1");
                if (this.mCallback != null) {
                    this.mCallback.fireEvent("motion.blow", result.toJsonString());
                }
                return true;
            case BlowSensor.BLOW_HANDLER_FAIL:
                if (this.mCallback != null) {
                    this.mCallback.error(new WVResult());
                }
                return true;
            default:
                return false;
        }
    }

    protected class MyShakeListener implements ShakeListener.OnShakeListener {
        private long frequency = 0;
        private long mLastUpdateTime = 0;
        private WVCallBackContext wvCallback = null;

        public MyShakeListener(WVCallBackContext callback, long frequency2) {
            this.wvCallback = callback;
            this.frequency = frequency2;
        }

        public void onShake() {
            if (WVMotion.this.isAlive) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - this.mLastUpdateTime >= this.frequency) {
                    WVResult result = new WVResult();
                    result.setSuccess();
                    if (this.wvCallback != null) {
                        this.wvCallback.fireEvent("motion.shake", result.toJsonString());
                    }
                    this.mLastUpdateTime = currentTime;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void stopListenGyro() {
        if (this.sm != null) {
            if (this.mSensorListener != null) {
                this.sm.unregisterListener(this.mSensorListener);
            }
            this.sm = null;
        }
    }

    private void stopShake() {
        if (this.mShakeListener != null) {
            this.mShakeListener.stop();
            this.mShakeListener = null;
        }
    }

    public void onDestroy() {
        stopShake();
        stopListenGyro();
        if (this.vibrator != null) {
            this.vibrator.cancel();
            this.vibrator = null;
        }
        this.mCallback = null;
        if (this.blowSensor != null) {
            this.blowSensor.stop();
        }
    }

    @TargetApi(9)
    public void onResume() {
        if (!(this.sm == null || this.mSensorListener == null)) {
            this.sm.registerListener(this.mSensorListener, this.sm.getDefaultSensor(9), 3);
        }
        if (this.mShakeListener != null) {
            this.mShakeListener.resume();
        }
        if (this.blowSensor != null) {
            this.blowSensor.start();
        }
        super.onResume();
    }

    public void onPause() {
        if (!(this.sm == null || this.mSensorListener == null)) {
            this.sm.unregisterListener(this.mSensorListener);
        }
        if (this.mShakeListener != null) {
            this.mShakeListener.pause();
        }
        if (this.blowSensor != null) {
            this.blowSensor.stop();
        }
        super.onPause();
    }

    public synchronized void vibrate(WVCallBackContext callback, String param) {
        WVResult result = new WVResult();
        try {
            int duration = new JSONObject(param).optInt(VPMConstants.MEASURE_DURATION, 350);
            if (duration < 0) {
                duration = 350;
            }
            if (this.vibrator == null) {
                this.vibrator = (Vibrator) this.mContext.getSystemService("vibrator");
            }
            this.vibrator.vibrate((long) duration);
            TaoLog.d("WVMotion", "vibrate: start ...");
            callback.success(new WVResult());
        } catch (JSONException e) {
            TaoLog.e("WVMotion", "vibrate: param parse to JSON error, param=" + param);
            result.setResult("HY_PARAM_ERR");
            callback.error(result);
        }
        return;
    }

    public synchronized void stopListenBlow(WVCallBackContext callback, String params) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVMotion", "stopListenBlow: stopped. " + params);
        }
        if (this.blowSensor != null) {
            this.blowSensor.stop();
            this.blowSensor = null;
        }
        callback.success(new WVResult());
    }

    public synchronized void listenBlow(WVCallBackContext callback, String params) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVMotion", "listenBlow: start. " + params);
        }
        this.mCallback = callback;
        if (this.blowSensor != null) {
            this.blowSensor.stop();
        }
        this.blowSensor = new BlowSensor(this.handler);
        this.blowSensor.start();
        callback.success(new WVResult());
    }

    public synchronized void listenGyro(WVCallBackContext callback, String params) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVMotion", "listenGyro:  " + params);
        }
        WVResult result = new WVResult();
        try {
            JSONObject jsObj = new JSONObject(params);
            this.frequency = (long) jsObj.optInt("frequency", 100);
            boolean on = jsObj.optBoolean("on");
            this.mCallback = callback;
            if (this.sm == null) {
                this.sm = (SensorManager) this.mContext.getSystemService("sensor");
            }
            if (on) {
                this.sm.registerListener(this.mSensorListener, this.sm.getDefaultSensor(9), 3);
                this.currentTime = System.currentTimeMillis();
            } else {
                stopListenGyro();
            }
            callback.success(new WVResult());
        } catch (JSONException e) {
            TaoLog.e("WVMotion", "vibrate: param parse to JSON error, param=" + params);
            result.setResult("HY_PARAM_ERR");
            callback.error(result);
        }
        return;
    }
}
