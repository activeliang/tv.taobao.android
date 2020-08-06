package android.taobao.windvane.jsbridge.api;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.taobao.windvane.util.TaoLog;

public class ShakeListener implements SensorEventListener {
    private static final int SPEED_THRESHOLD = 10;
    protected long TIME_INTERVAL_THRESHOLD = 1000;
    private Context mContext;
    private long mLastUpdateTime;
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    private SensorManager mSensorManager;
    private OnShakeListener mShakeListener;

    public interface OnShakeListener {
        void onShake();
    }

    public ShakeListener(Context context) {
        this.mContext = context;
        start();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.mShakeListener = listener;
    }

    public void start() {
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        if (this.mSensorManager == null) {
            TaoLog.w("ShakeListener", "start: Sensors not supported");
        } else if (!this.mSensorManager.registerListener(this, this.mSensorManager.getDefaultSensor(1), 2)) {
            this.mSensorManager.unregisterListener(this);
            TaoLog.w("ShakeListener", "start: Accelerometer not supported");
        }
    }

    public void pause() {
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
    }

    public void resume() {
        if (this.mSensorManager != null && !this.mSensorManager.registerListener(this, this.mSensorManager.getDefaultSensor(1), 2)) {
            this.mSensorManager.unregisterListener(this);
            TaoLog.w("ShakeListener", "start: Accelerometer not supported");
        }
    }

    public void stop() {
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
            this.mSensorManager = null;
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 1) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.mLastUpdateTime >= this.TIME_INTERVAL_THRESHOLD) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float deltaX = x - this.mLastX;
                float deltaY = y - this.mLastY;
                float deltaZ = z - this.mLastZ;
                if (Math.sqrt((double) ((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ))) > 10.0d && this.mShakeListener != null && this.mShakeListener != null && Math.abs(this.mLastX) > 0.0f && Math.abs(this.mLastY) > 0.0f && Math.abs(this.mLastZ) > 0.0f) {
                    this.mShakeListener.onShake();
                }
                this.mLastUpdateTime = currentTime;
                this.mLastX = x;
                this.mLastY = y;
                this.mLastZ = z;
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
