package com.yunos.tv.core.location;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.core.CoreApplication;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class LocationAssist {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /* access modifiers changed from: private */
    public static final String TAG = LocationAssist.class.getSimpleName();
    private static LocationAssist instance;
    /* access modifiers changed from: private */
    public AMapLocation location;
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLocationListener;
    private AMapLocationClientOption mLocationOption;
    OnAMapLocationChange onAMapLocationChange;

    public interface OnAMapLocationChange {
        void onLocationChanged(AMapLocation aMapLocation);
    }

    private LocationAssist() {
        this.onAMapLocationChange = null;
        this.mLocationClient = null;
        this.mLocationListener = null;
        this.mLocationOption = null;
        this.location = null;
        this.mLocationListener = new AMapLocationListener() {
            public void onLocationChanged(AMapLocation aMapLocation) {
                ZpLogger.i(LocationAssist.TAG, aMapLocation != null ? aMapLocation.toString() : Constant.NULL);
                AMapLocation unused = LocationAssist.this.location = aMapLocation;
                if (LocationAssist.this.onAMapLocationChange != null) {
                    try {
                        LocationAssist.this.onAMapLocationChange.onLocationChanged(LocationAssist.this.location);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.mLocationClient = new AMapLocationClient(CoreApplication.getApplication());
        this.mLocationClient.setLocationListener(this.mLocationListener);
        this.mLocationOption = new AMapLocationClientOption();
        this.mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        this.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        this.mLocationOption.setOnceLocationLatest(true);
        this.mLocationOption.setNeedAddress(true);
        this.mLocationClient.setLocationOption(this.mLocationOption);
    }

    public static LocationAssist getInstance() {
        if (instance == null) {
            synchronized (LocationAssist.class) {
                if (instance == null) {
                    instance = new LocationAssist();
                }
            }
        }
        return instance;
    }

    public void requestLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (activity.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0 || activity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            activity.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 1);
            Log.d(TAG, "TAG  ---> requestLocationPermission ");
        }
    }

    public void startLocation(OnAMapLocationChange onAMapLocationChange2) {
        this.onAMapLocationChange = onAMapLocationChange2;
        this.mLocationClient.startLocation();
    }

    public void stopLocation(OnAMapLocationChange onAMapLocationChange2) {
        if (this.onAMapLocationChange == onAMapLocationChange2) {
            this.onAMapLocationChange = null;
        }
    }

    public LocationInfo getLocation() {
        return new LocationInfo(this.location);
    }

    public String getLongitude() {
        if (getLocation() == null || getLocation().getLocation() == null) {
            return "";
        }
        return "" + getLocation().getLocation().getLongitude();
    }

    public String getLatitude() {
        if (getLocation() == null || getLocation().getLocation() == null) {
            return "";
        }
        return "" + getLocation().getLocation().getLatitude();
    }

    public String getCityCode() {
        if (getLocation() == null || getLocation().getLocation() == null) {
            return "";
        }
        return "" + getLocation().getLocation().getCityCode();
    }

    public static class LocationInfo {
        Map<String, String> ext = new HashMap();
        AMapLocation location;

        public LocationInfo(AMapLocation location2) {
            this.location = location2;
        }

        public LocationInfo ext(String key, String val) {
            this.ext.put(key, val);
            return this;
        }

        public String toString() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.putOpt("location", this.location.toJson(1));
                JSONArray array = new JSONArray();
                if (this.ext != null && !this.ext.isEmpty()) {
                    for (Map.Entry<String, String> entry : this.ext.entrySet()) {
                        JSONObject item = new JSONObject();
                        item.putOpt(entry.getKey(), entry.getValue());
                        array.put(item);
                    }
                }
                jsonObject.putOpt("ext", array);
                return jsonObject.toString();
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        public String toJSONString() {
            return toString();
        }

        public AMapLocation getLocation() {
            return this.location;
        }
    }
}
