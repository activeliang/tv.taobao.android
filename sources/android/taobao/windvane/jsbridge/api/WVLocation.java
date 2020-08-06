package android.taobao.windvane.jsbridge.api;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class WVLocation extends WVApiPlugin implements LocationListener, Handler.Callback {
    private static final int GPS_TIMEOUT = 15000;
    private static final String TAG = "WVLocation";
    private int MIN_DISTANCE;
    private int MIN_TIME;
    private boolean enableAddress;
    private boolean getLocationSuccess;
    private LocationManager locationManager;
    private WVCallBackContext mCallback;
    /* access modifiers changed from: private */
    public Handler mHandler;

    public WVLocation() {
        this.MIN_TIME = 20000;
        this.MIN_DISTANCE = 30;
        this.mHandler = null;
        this.mCallback = null;
        this.getLocationSuccess = false;
        this.enableAddress = false;
        this.locationManager = null;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (!"getLocation".equals(action)) {
            return false;
        }
        getLocation(callback, params);
        return true;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void getLocation(final android.taobao.windvane.jsbridge.WVCallBackContext r5, final java.lang.String r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            android.taobao.windvane.webview.IWVWebView r0 = r5.getWebview()     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            android.content.Context r0 = r0.getContext()     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            r1 = 1
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            r2 = 0
            java.lang.String r3 = "android.permission.ACCESS_FINE_LOCATION"
            r1[r2] = r3     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            android.taobao.windvane.runtimepermission.PermissionProposer$PermissionRequestTask r0 = android.taobao.windvane.runtimepermission.PermissionProposer.buildPermissionTask(r0, r1)     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            android.taobao.windvane.jsbridge.api.WVLocation$2 r1 = new android.taobao.windvane.jsbridge.api.WVLocation$2     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            r1.<init>(r5, r6)     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            android.taobao.windvane.runtimepermission.PermissionProposer$PermissionRequestTask r0 = r0.setTaskOnPermissionGranted(r1)     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            android.taobao.windvane.jsbridge.api.WVLocation$1 r1 = new android.taobao.windvane.jsbridge.api.WVLocation$1     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            android.taobao.windvane.runtimepermission.PermissionProposer$PermissionRequestTask r0 = r0.setTaskOnPermissionDenied(r1)     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            r0.execute()     // Catch:{ Exception -> 0x0030, all -> 0x002d }
        L_0x002b:
            monitor-exit(r4)
            return
        L_0x002d:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        L_0x0030:
            r0 = move-exception
            goto L_0x002b
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVLocation.getLocation(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    public void requestLocation(WVCallBackContext callback, String param) {
        this.mCallback = callback;
        boolean enableHighAcuracy = false;
        if (!TextUtils.isEmpty(param)) {
            try {
                JSONObject jsObj = new JSONObject(param);
                enableHighAcuracy = jsObj.optBoolean("enableHighAcuracy");
                this.enableAddress = jsObj.optBoolean("address");
            } catch (JSONException e) {
                TaoLog.e("WVLocation", "getLocation: param parse to JSON error, param=" + param);
                WVResult result = new WVResult();
                result.setResult("HY_PARAM_ERR");
                callback.error(result);
                return;
            }
        }
        registerLocation(enableHighAcuracy);
        WVThreadPool.getInstance().execute(new Runnable() {
            public void run() {
                WVLocation.this.mHandler.sendEmptyMessageDelayed(1, 15000);
            }
        });
    }

    private void registerLocation(boolean enableHighAcuracy) {
        if (this.locationManager == null) {
            this.locationManager = (LocationManager) this.mContext.getSystemService("location");
        }
        try {
            this.getLocationSuccess = false;
            this.locationManager.requestLocationUpdates("network", (long) this.MIN_TIME, (float) this.MIN_DISTANCE, this);
            this.locationManager.requestLocationUpdates("gps", (long) this.MIN_TIME, (float) this.MIN_DISTANCE, this);
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVLocation", " registerLocation start provider GPS and NETWORK");
            }
        } catch (Exception e) {
            TaoLog.e("WVLocation", "registerLocation error: " + e.getMessage());
        }
    }

    private Address getAddress(double latitude, double longitude) {
        try {
            List<Address> list = new Geocoder(this.mContext).getFromLocation(latitude, longitude, 1);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            TaoLog.e("WVLocation", "getAddress: getFromLocation error. " + e.getMessage());
        }
        return null;
    }

    public void onLocationChanged(Location location) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVLocation", " onLocationChanged. ");
        }
        if (this.locationManager != null) {
            wrapResult(location);
            this.locationManager.removeUpdates(this);
            this.getLocationSuccess = true;
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVLocation", " onStatusChanged. provider: " + provider + ";status: " + status);
        }
    }

    public void onProviderEnabled(String provider) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVLocation", " onProviderEnabled. provider: " + provider);
        }
    }

    public void onProviderDisabled(String provider) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVLocation", " onProviderDisabled. provider: " + provider);
        }
    }

    private void wrapResult(Location location) {
        if (this.mCallback == null) {
            TaoLog.d("WVLocation", "GetLocation wrapResult callbackContext is null");
        } else if (location == null) {
            TaoLog.w("WVLocation", "getLocation: location is null");
            this.mCallback.error(new WVResult());
        } else {
            WVResult result = new WVResult();
            JSONObject coords = new JSONObject();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            try {
                coords.put(ClientTraceData.b.f54c, longitude);
                coords.put(ClientTraceData.b.d, latitude);
                coords.put("altitude", location.getAltitude());
                coords.put("accuracy", (double) location.getAccuracy());
                coords.put("heading", (double) location.getBearing());
                coords.put("speed", (double) location.getSpeed());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            result.addData("coords", coords);
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVLocation", " getLocation success. latitude: " + latitude + "; longitude: " + longitude);
            }
            if (this.enableAddress) {
                Address addr = getAddress(latitude, longitude);
                JSONObject address = new JSONObject();
                if (addr != null) {
                    try {
                        address.put("country", addr.getCountryName());
                        address.put("province", addr.getAdminArea());
                        address.put("city", addr.getLocality());
                        address.put("cityCode", addr.getPostalCode());
                        address.put("area", addr.getSubLocality());
                        address.put("road", addr.getThoroughfare());
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i <= 2; i++) {
                            if (!TextUtils.isEmpty(addr.getAddressLine(i))) {
                                sb.append(addr.getAddressLine(i));
                            }
                        }
                        address.put("addressLine", sb.toString());
                        if (TaoLog.getLogStatus()) {
                            TaoLog.d("WVLocation", " getAddress success. " + addr.getAddressLine(0));
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                } else if (TaoLog.getLogStatus()) {
                    TaoLog.w("WVLocation", " getAddress fail. ");
                }
                result.addData("address", address);
            }
            this.mCallback.success(result);
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVLocation", "callback success. retString: " + result.toJsonString());
            }
        }
    }

    public void onDestroy() {
        if (this.locationManager != null) {
            if (!this.getLocationSuccess) {
                try {
                    this.locationManager.removeUpdates(this);
                } catch (Exception e) {
                }
            }
            this.locationManager = null;
        }
        this.mCallback = null;
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                if (this.locationManager != null) {
                    try {
                        this.locationManager.removeUpdates(this);
                        if (!this.getLocationSuccess) {
                            this.mCallback.error(new WVResult());
                            if (TaoLog.getLogStatus()) {
                                TaoLog.d("WVLocation", "GetLocation timeout");
                            }
                        }
                    } catch (Exception e) {
                        TaoLog.e("WVLocation", "GetLocation timeout" + e.getMessage());
                        this.mCallback.error(new WVResult());
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
