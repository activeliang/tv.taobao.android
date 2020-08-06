package com.tvtao.user.dclib.impl;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.alibaba.baichuan.trade.common.adapter.mtop.AlibcMtop;
import com.alibaba.baichuan.trade.common.adapter.mtop.NetworkClient;
import com.alibaba.baichuan.trade.common.adapter.mtop.NetworkResponse;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import org.json.JSONException;
import org.json.JSONObject;

public class ZPDeviceImpl {
    /* access modifiers changed from: private */
    public static volatile boolean initialized = false;
    /* access modifiers changed from: private */
    public static volatile boolean initializing = false;
    private static Context sAppContext;
    /* access modifiers changed from: private */
    public static SDKScene sdkScene;
    /* access modifiers changed from: private */
    public static SharedPreferences sharedPreferences;
    /* access modifiers changed from: private */
    public static String zpDid;
    /* access modifiers changed from: private */
    public static String zpUid;

    public enum SDKScene {
        OPNE,
        INNER
    }

    public static void init(Context context, Map<String, String> extParams, SDKScene scene) {
        if (!initialized && !initializing && context != null) {
            sdkScene = scene;
            sAppContext = context.getApplicationContext();
            initializing = true;
            sharedPreferences = context.getSharedPreferences("tvtao_dc", 0);
            new Thread(new InitializeTask(context, extParams)).start();
        }
    }

    public static String getUserId(Context context) {
        if (!initialized) {
            doInit(context);
        }
        return zpUid;
    }

    public static String getZpDid(Context context) {
        if (!initialized) {
            doInit(context);
        }
        return zpDid;
    }

    private static void doInit(Context context) {
        Context curContext;
        String localData;
        if (sAppContext == null && context != null) {
            sAppContext = context.getApplicationContext();
        }
        if (context == null) {
            curContext = sAppContext;
        } else {
            curContext = context;
        }
        if (!initialized && !initializing && curContext != null) {
            if (sharedPreferences == null) {
                sharedPreferences = curContext.getSharedPreferences("tvtao_dc", 0);
            }
            if (zpDid == null && (localData = sharedPreferences.getString("zpdata", (String) null)) != null) {
                try {
                    JSONObject jsonObject = new JSONObject(localData);
                    zpDid = jsonObject.optString("zpDid", (String) null);
                    zpUid = jsonObject.optString("augurZpUid", (String) null);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class InitializeTask implements Runnable {
        private Context context;
        private Map<String, String> extParams;

        public InitializeTask(Context context2, Map<String, String> extParams2) {
            this.context = context2;
            this.extParams = extParams2;
        }

        public void run() {
            JSONObject json = new JSONObject();
            putJson(json, "imei", getOriginalImei());
            putJson(json, "imsi", getOriginalImsi());
            putJson(json, "mac", getMacAddress());
            putJson(json, "model", Build.MODEL);
            putJson(json, "gsd", getAndroidId());
            putJson(json, "sn", getSerialNum());
            putJson(json, "cpuSerial", CPUUtil.getSerial(this.context));
            putJson(json, "mmcCid", getMMCID(this.context));
            putJson(json, "buildBrand", Build.BRAND);
            putJson(json, "buildProduct", Build.PRODUCT);
            putJson(json, "buildVersionRelease", Build.VERSION.RELEASE);
            putJson(json, "buildVersionSdk", Build.VERSION.SDK);
            putJson(json, "buildType", Build.TYPE);
            putJson(json, "buildTags", Build.TAGS);
            putJson(json, "buildId", Build.ID);
            putJson(json, "buildDisplayVersion", Build.DISPLAY);
            putJson(json, "buildTime", "" + Build.TIME);
            putJson(json, "buildBoard", Build.BOARD);
            putJson(json, "buildManufacturer", Build.MANUFACTURER);
            putJson(json, "buildDevice", Build.DEVICE);
            putJson(json, "buildIncremental", Build.VERSION.INCREMENTAL);
            putJson(json, "clientTime", "" + System.currentTimeMillis());
            putJson(json, "wifiInterface", getSystemProperty(this.context, "wifi.interface", (String) null));
            putJson(json, "yunosVersion", getSystemProperty(this.context, "ro.yunos.version", (String) null));
            putJson(json, "networkType", getNetworkType());
            putJson(json, "kernelQemu", getSystemProperty(this.context, "ro.kernel.qemu", "1".equals(getSystemProperty(this.context, "ro.kernel.qemu", "unknown")) ? "true" : "false"));
            putJson(json, "yunosVersion", getSystemProperty(this.context, "ro.yunos.version.release", (String) null));
            try {
                putJson(json, "simulator", SecurityGuardManager.getInstance(this.context).getSimulatorDetectComp().isSimulator() ? "true" : "false");
            } catch (SecException e) {
                e.printStackTrace();
            }
            putJson(json, "screenDpi", getDisplayDPI());
            putJson(json, "cpuCount", CPUUtil.getCPUCount(this.context));
            putJson(json, "extMemSize", getRomSize());
            String localData = ZPDeviceImpl.sharedPreferences.getString("zpdata", (String) null);
            if (TextUtils.isEmpty(localData)) {
                doSendRequest(json);
                return;
            }
            try {
                JSONObject local = new JSONObject(localData);
                long expire = local.optLong("maxAgeMillisecond");
                long time = local.optLong("time");
                String unused = ZPDeviceImpl.zpUid = local.optString("augurZpUid");
                String unused2 = ZPDeviceImpl.zpDid = local.optString("zpDid");
                if (System.currentTimeMillis() < time || System.currentTimeMillis() > expire + time) {
                    putJson(json, "zpDid", local.optString("zpDid"));
                    doSendRequest(json);
                    return;
                }
                boolean unused3 = ZPDeviceImpl.initialized = true;
                boolean unused4 = ZPDeviceImpl.initializing = false;
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }

        private JSONObject convertExtParams() {
            JSONObject jsonObject = new JSONObject();
            if (this.extParams != null && !this.extParams.isEmpty()) {
                for (String key : this.extParams.keySet()) {
                    try {
                        jsonObject.put(key, this.extParams.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonObject;
        }

        private void doSendRequest(JSONObject params) {
            if (SDKScene.INNER == ZPDeviceImpl.sdkScene) {
                doSendInnerRequest(params);
            } else {
                doSendOpenRequest(params);
            }
        }

        private void doSendInnerRequest(final JSONObject params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("params", params.toString());
                jsonObject.put("extParams", convertExtParams().toString());
                Mtop.instance(Mtop.Id.INNER, this.context).build((MtopRequest) new ReportInnerRequest(jsonObject.toString()), (String) null).reqMethod(MethodEnum.POST).addListener(new MtopCallback.MtopFinishListener() {
                    public void onFinished(MtopFinishEvent mtopFinishEvent, Object o) {
                        MtopResponse response = mtopFinishEvent.getMtopResponse();
                        if (!response.isApiSuccess() || response.getDataJsonObject() == null) {
                            InitializeTask.this.uploadUt(params);
                        } else {
                            InitializeTask.this.reportSuccess(params, response.getDataJsonObject().toString());
                        }
                    }
                }).asyncRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void doSendOpenRequest(final JSONObject params) {
            try {
                ReportRequest reportRequest = new ReportRequest();
                reportRequest.setParams(params);
                reportRequest.setExtParams(convertExtParams());
                AlibcMtop.getInstance().sendRequest(new NetworkClient.NetworkRequestListener() {
                    public void onSuccess(int i, NetworkResponse networkResponse) {
                        InitializeTask.this.reportSuccess(params, networkResponse.jsonData);
                    }

                    public void onError(int i, NetworkResponse networkResponse) {
                        InitializeTask.this.uploadUt(params);
                    }
                }, reportRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* access modifiers changed from: private */
        public void reportSuccess(JSONObject params, String jsonData) {
            try {
                JSONObject data = new JSONObject(jsonData);
                if (isValid(data)) {
                    String uid = data.optString("augurZpUid");
                    if (!TextUtils.isEmpty(uid)) {
                        String unused = ZPDeviceImpl.zpUid = uid;
                    } else if (!TextUtils.isEmpty(ZPDeviceImpl.zpUid)) {
                        data.put("augurZpUid", ZPDeviceImpl.zpUid);
                    }
                    String unused2 = ZPDeviceImpl.zpDid = data.optString("zpDid");
                    data.put("time", System.currentTimeMillis());
                    ZPDeviceImpl.sharedPreferences.edit().putString("zpdata", data.toString()).apply();
                    params.put("zpDid", ZPDeviceImpl.zpDid);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            uploadUt(params);
            boolean unused3 = ZPDeviceImpl.initialized = true;
            boolean unused4 = ZPDeviceImpl.initializing = false;
        }

        private boolean isValid(JSONObject jsonObject) {
            if (jsonObject != null && !TextUtils.isEmpty(jsonObject.optString("zpDid", (String) null))) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: private */
        public void uploadUt(JSONObject params) {
            HashMap<String, String> map = new HashMap<>();
            if (params != null) {
                Iterator<String> iterator = params.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    map.put(key, params.optString(key, ""));
                }
            }
            if (!TextUtils.isEmpty(ZPDeviceImpl.zpDid)) {
                map.put("zpDid", ZPDeviceImpl.zpDid);
            }
            if (!TextUtils.isEmpty(ZPDeviceImpl.zpUid)) {
                map.put("augurZpUid", ZPDeviceImpl.zpUid);
            }
            UTHitBuilders.UTCustomHitBuilder builder = new UTHitBuilders.UTCustomHitBuilder("DeviceFingerprinting");
            builder.setDurationOnEvent(2341);
            builder.setProperty("curent_time", System.currentTimeMillis() + "");
            builder.setProperty("curent_thread", Thread.currentThread().getId() + "");
            builder.setProperties(map);
            UTAnalytics.getInstance().getDefaultTracker().send(builder.build());
        }

        private String getDisplayDPI() {
            try {
                DisplayMetrics dm = new DisplayMetrics();
                ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
                return "" + dm.densityDpi;
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        private String getSubscriberID() {
            try {
                return ((TelephonyManager) this.context.getSystemService("phone")).getSubscriberId();
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        private String getAndroidId() {
            String id = null;
            if (this.context == null) {
                return null;
            }
            try {
                id = Settings.Secure.getString(this.context.getContentResolver(), "android_id");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return id;
        }

        private String getNetworkType() {
            try {
                return ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo().getTypeName();
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        private void putJson(JSONObject json, String key, String value) {
            if (value == null) {
                value = "";
            }
            try {
                json.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String getMacAddress() {
            byte[] mac;
            try {
                for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    if (intf.getName().equalsIgnoreCase("eth0") && (mac = intf.getHardwareAddress()) != null) {
                        StringBuilder buf = new StringBuilder();
                        for (int idx = 0; idx < mac.length; idx++) {
                            buf.append(String.format("%02X:", new Object[]{Byte.valueOf(mac[idx])}));
                        }
                        if (buf.length() > 0) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        return buf.toString();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        private String getWifiMac(Context context2) {
            if (context2.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") != 0) {
                return null;
            }
            if (Build.VERSION.SDK_INT < 23) {
                WifiInfo info = ((WifiManager) context2.getSystemService("wifi")).getConnectionInfo();
                if (info != null) {
                    return info.getMacAddress();
                }
                return null;
            }
            try {
                byte[] hwAddr = NetworkInterface.getByName("wlan0").getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (i < hwAddr.length) {
                    Object[] objArr = new Object[2];
                    objArr[0] = Byte.valueOf(hwAddr[i]);
                    objArr[1] = i < hwAddr.length + -1 ? SymbolExpUtil.SYMBOL_COLON : "";
                    sb.append(String.format("%02X%s", objArr));
                    i++;
                }
                return sb.toString();
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        private String getBluetoothAddress(Context context2) {
            Object address;
            if (Build.VERSION.SDK_INT <= 23) {
                try {
                    return BluetoothAdapter.getDefaultAdapter().getAddress();
                } catch (Throwable th) {
                    return null;
                }
            } else {
                try {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Field field = bluetoothAdapter.getClass().getDeclaredField("mService");
                    field.setAccessible(true);
                    Object bluetoothManagerService = field.get(bluetoothAdapter);
                    if (bluetoothManagerService == null || (address = bluetoothManagerService.getClass().getMethod("getAddress", new Class[0]).invoke(bluetoothManagerService, new Object[0])) == null || !(address instanceof String)) {
                        return null;
                    }
                    return (String) address;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        private String getCurProcessName() {
            try {
                for (ActivityManager.RunningAppProcessInfo info : ((ActivityManager) this.context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                    if (info.pid == Process.myPid()) {
                        return info.processName;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getSerialNum() {
            if (Build.VERSION.SDK_INT <= 27) {
                return getSystemProperty(this.context, "ro.serialno", "unknown");
            }
            try {
                return Build.getSerial();
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        private String getSimSerialNum() {
            try {
                return ((TelephonyManager) this.context.getSystemService("phone")).getSimSerialNumber();
            } catch (Throwable th) {
                return null;
            }
        }

        private String getRomSize() {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            long blockSize = (long) stat.getBlockSize();
            long totalBlocks = (long) stat.getBlockCount();
            long availableBlocks = ((long) stat.getAvailableBlocks()) * blockSize;
            return "" + (totalBlocks * blockSize);
        }

        private String getSystemProperty(Context context2, String name, String defVal) {
            try {
                return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(context2, new Object[]{name, defVal});
            } catch (Throwable e) {
                e.printStackTrace();
                return defVal;
            }
        }

        private String getMMCID(Context context2) {
            try {
                return new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/block/mmcblk0/device/cid").getInputStream())).readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String getOriginalImei() {
            String imei = null;
            if (this.context == null) {
                return null;
            }
            try {
                imei = ((TelephonyManager) this.context.getSystemService("phone")).getDeviceId();
                if (imei != null) {
                    imei = imei.trim();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return imei;
        }

        private String getOriginalImsi() {
            String imsi = null;
            if (this.context == null) {
                return null;
            }
            try {
                imsi = ((TelephonyManager) this.context.getSystemService("phone")).getSubscriberId();
                if (imsi != null) {
                    imsi = imsi.trim();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return imsi;
        }
    }
}
