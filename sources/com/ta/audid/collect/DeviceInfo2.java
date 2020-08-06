package com.ta.audid.collect;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.ta.audid.utils.FileUtils;
import com.ta.audid.utils.SystemProperties;
import com.ta.audid.utils.UtdidLogger;
import com.ta.utdid2.android.utils.StringUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.List;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public class DeviceInfo2 {
    public static String getIMEI(Context context) {
        String imei = "";
        if (context != null) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
                if (tm != null) {
                    imei = tm.getDeviceId();
                }
                if (StringUtils.isBlank(imei)) {
                    return "";
                }
                return imei;
            } catch (Exception e) {
                UtdidLogger.i("", e.toString());
            }
        }
        return imei;
    }

    public static String getIMSI(Context context) {
        String imsi = "";
        if (context != null) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
                if (tm != null) {
                    imsi = tm.getSubscriberId();
                }
                if (StringUtils.isBlank(imsi)) {
                    return "";
                }
                return imsi;
            } catch (Exception e) {
                UtdidLogger.i("", e.toString());
            }
        }
        return imsi;
    }

    public static String getWifiMacID(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getWifiMacID23();
        }
        return getWifiMacID22(context);
    }

    private static String getWifiMacID23() {
        try {
            byte[] mac = NetworkInterface.getByName("wlan0").getHardwareAddress();
            StringBuilder b = new StringBuilder();
            int i = 0;
            while (i < mac.length) {
                Object[] objArr = new Object[2];
                objArr[0] = Byte.valueOf(mac[i]);
                objArr[1] = i < mac.length + -1 ? SymbolExpUtil.SYMBOL_COLON : "";
                b.append(String.format("%02X%s", objArr));
                i++;
            }
            return b.toString();
        } catch (Exception e) {
            UtdidLogger.i("", e);
            return "";
        }
    }

    private static String getWifiMacID22(Context context) {
        if (context != null) {
            try {
                WifiInfo wifiinfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
                if (wifiinfo == null) {
                    return "";
                }
                String address = wifiinfo.getMacAddress();
                if (TextUtils.isEmpty(address)) {
                    return "";
                }
                return address;
            } catch (Throwable th) {
            }
        }
        return "";
    }

    public static String getBluetoothMac() {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
                return "";
            }
            String address = defaultAdapter.getAddress();
            if (StringUtils.isBlank(address)) {
                return "";
            }
            return address;
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getSerialNum() {
        return SystemProperties.get("ro.serialno", "");
    }

    public static String getSimSerialNum(Context context) {
        String simSerialNumber;
        try {
            simSerialNumber = ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
        } catch (Exception e) {
            simSerialNumber = "";
        }
        return StringUtils.isBlank(simSerialNumber) ? "" : simSerialNumber;
    }

    public static String getNandID() {
        return FileUtils.readFileLine("/sys/block/mmcblk0/device/cid");
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0061 A[SYNTHETIC, Splitter:B:28:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0066 A[SYNTHETIC, Splitter:B:31:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x006f A[SYNTHETIC, Splitter:B:36:0x006f] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0074 A[SYNTHETIC, Splitter:B:39:0x0074] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCPUSerial() {
        /*
            java.lang.String r6 = ""
            r1 = 0
            r3 = 0
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            java.lang.Runtime r7 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            java.lang.String r8 = "cat /proc/cpuinfo | grep Serial"
            java.lang.Process r7 = r7.exec(r8)     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            java.io.InputStream r7 = r7.getInputStream()     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            r2.<init>(r7)     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            java.io.LineNumberReader r4 = new java.io.LineNumberReader     // Catch:{ Throwable -> 0x0087, all -> 0x0080 }
            r4.<init>(r2)     // Catch:{ Throwable -> 0x0087, all -> 0x0080 }
            r0 = 0
        L_0x001f:
            r7 = 100
            if (r0 >= r7) goto L_0x0047
            java.lang.String r5 = r4.readLine()     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            if (r5 == 0) goto L_0x0055
            java.lang.String r7 = "Serial"
            int r7 = r5.indexOf(r7)     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            if (r7 < 0) goto L_0x0055
            java.lang.String r7 = ":"
            int r7 = r5.indexOf(r7)     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            int r7 = r7 + 1
            int r8 = r5.length()     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            java.lang.String r7 = r5.substring(r7, r8)     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            java.lang.String r6 = r7.trim()     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
        L_0x0047:
            if (r2 == 0) goto L_0x004c
            r2.close()     // Catch:{ Exception -> 0x0078 }
        L_0x004c:
            if (r4 == 0) goto L_0x0051
            r4.close()     // Catch:{ Exception -> 0x005a }
        L_0x0051:
            r3 = r4
            r1 = r2
        L_0x0053:
            return r6
        L_0x0054:
            r7 = move-exception
        L_0x0055:
            int r0 = r0 + 1
            int r0 = r0 + 1
            goto L_0x001f
        L_0x005a:
            r7 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x0053
        L_0x005e:
            r7 = move-exception
        L_0x005f:
            if (r1 == 0) goto L_0x0064
            r1.close()     // Catch:{ Exception -> 0x007a }
        L_0x0064:
            if (r3 == 0) goto L_0x0053
            r3.close()     // Catch:{ Exception -> 0x006a }
            goto L_0x0053
        L_0x006a:
            r7 = move-exception
            goto L_0x0053
        L_0x006c:
            r7 = move-exception
        L_0x006d:
            if (r1 == 0) goto L_0x0072
            r1.close()     // Catch:{ Exception -> 0x007c }
        L_0x0072:
            if (r3 == 0) goto L_0x0077
            r3.close()     // Catch:{ Exception -> 0x007e }
        L_0x0077:
            throw r7
        L_0x0078:
            r7 = move-exception
            goto L_0x004c
        L_0x007a:
            r7 = move-exception
            goto L_0x0064
        L_0x007c:
            r8 = move-exception
            goto L_0x0072
        L_0x007e:
            r8 = move-exception
            goto L_0x0077
        L_0x0080:
            r7 = move-exception
            r1 = r2
            goto L_0x006d
        L_0x0083:
            r7 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x006d
        L_0x0087:
            r7 = move-exception
            r1 = r2
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.collect.DeviceInfo2.getCPUSerial():java.lang.String");
    }

    public static String getPhoneNumber(Context context) {
        String str = null;
        if (context != null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                str = telephonyManager != null ? telephonyManager.getLine1Number() : null;
            } catch (Exception e) {
            }
        }
        return StringUtils.isBlank(str) ? "" : str;
    }

    public static String getCpuCount() {
        try {
            return String.valueOf(new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]+", file.getName());
                }
            }).length);
        } catch (Exception e) {
            return "1";
        }
    }

    public static String getMaxCpuFreq() {
        return FileUtils.readFileLine("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
    }

    public static String getMinCpuFreq() {
        return FileUtils.readFileLine("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
    }

    public static String getMemTotalSize() {
        String memTotal = FileUtils.readFileLine("/proc/meminfo");
        if (memTotal != null) {
            return memTotal.split("\\s+")[1];
        }
        return "";
    }

    public static String getMemFreeSize(Context context) {
        try {
            ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
            ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getMemoryInfo(info);
            return String.valueOf(info.availMem >> 10);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getLowMem(Context context) {
        try {
            ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
            ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getMemoryInfo(info);
            return String.valueOf(info.threshold >> 10);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMemThreshold(Context context) {
        try {
            ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
            ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getMemoryInfo(info);
            if (info.lowMemory) {
                return "1";
            }
            return "0";
        } catch (Exception e) {
            return "0";
        }
    }

    public static String getTotalExternalMemorySize(Context context) {
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return String.valueOf(((long) stat.getBlockCount()) * ((long) stat.getBlockSize()));
        } catch (Throwable th) {
            return "";
        }
    }

    private String getAvailableSize() {
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return String.valueOf(((long) stat.getBlockCount()) * ((long) stat.getAvailableBlocks()));
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getScreenDpi(Context context) {
        try {
            return "" + context.getResources().getDisplayMetrics().densityDpi;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getScreenResolution(Context context) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Integer.toString(displayMetrics.widthPixels) + "*" + Integer.toString(displayMetrics.heightPixels);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean checkTfCard(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                return checkTfCardN(context);
            }
            return checkTfCard0(context);
        } catch (Exception e) {
            return false;
        }
    }

    @TargetApi(24)
    private static boolean checkTfCardN(Context context) {
        List<StorageVolume> storageVolumeList = ((StorageManager) context.getSystemService("storage")).getStorageVolumes();
        if (storageVolumeList != null) {
            for (StorageVolume storageVolume : storageVolumeList) {
                if (storageVolume.isRemovable() && storageVolume.getState().equals("mounted")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkTfCard0(Context context) {
        StorageManager sm = (StorageManager) context.getSystemService("storage");
        try {
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList", new Class[0]);
            getVolumeList.setAccessible(true);
            Object[] results = (Object[]) getVolumeList.invoke(sm, new Object[0]);
            if (results == null) {
                return false;
            }
            for (Object result : results) {
                if (((Boolean) result.getClass().getMethod("isRemovable", new Class[0]).invoke(result, new Object[0])).booleanValue() && ((String) result.getClass().getMethod("getState", new Class[0]).invoke(result, new Object[0])).equals("mounted")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkSensor(Context context, int sensorType) {
        List<Sensor> list = ((SensorManager) context.getSystemService("sensor")).getSensorList(sensorType);
        if (list == null || list.size() <= 0) {
            return false;
        }
        return true;
    }

    public static boolean hasGPSDevice(Context context) {
        List<String> providers;
        LocationManager mgr = (LocationManager) context.getSystemService("location");
        if (mgr == null || (providers = mgr.getAllProviders()) == null) {
            return false;
        }
        return providers.contains("gps");
    }

    @TargetApi(23)
    public static boolean hasFingerprintDevice(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                if (((FingerprintManager) context.getSystemService("fingerprint")).isHardwareDetected()) {
                    return true;
                }
            } catch (Throwable e) {
                UtdidLogger.d("", e);
            }
        }
        return false;
    }

    public static boolean hasBluetoothDevice(Context context) {
        try {
            if (context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getBattery(Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            return getBattery0();
        }
        int battery = getBatteryL(context);
        if (battery > 0) {
            return "" + battery;
        }
        return getBattery0();
    }

    @TargetApi(21)
    private static int getBatteryL(Context context) {
        try {
            return ((BatteryManager) context.getSystemService("batterymanager")).getIntProperty(4);
        } catch (Throwable th) {
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0061 A[SYNTHETIC, Splitter:B:28:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0066 A[SYNTHETIC, Splitter:B:31:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x006f A[SYNTHETIC, Splitter:B:36:0x006f] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0074 A[SYNTHETIC, Splitter:B:39:0x0074] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getBattery0() {
        /*
            java.lang.String r6 = ""
            r1 = 0
            r3 = 0
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            java.lang.Runtime r7 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            java.lang.String r8 = "cat /sys/class/power_supply/battery/uevent"
            java.lang.Process r7 = r7.exec(r8)     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            java.io.InputStream r7 = r7.getInputStream()     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            r2.<init>(r7)     // Catch:{ Throwable -> 0x005e, all -> 0x006c }
            java.io.LineNumberReader r4 = new java.io.LineNumberReader     // Catch:{ Throwable -> 0x0087, all -> 0x0080 }
            r4.<init>(r2)     // Catch:{ Throwable -> 0x0087, all -> 0x0080 }
            r0 = 0
        L_0x001f:
            r7 = 100
            if (r0 >= r7) goto L_0x0047
            java.lang.String r5 = r4.readLine()     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            if (r5 == 0) goto L_0x0047
            java.lang.String r7 = "POWER_SUPPLY_CAPACITY"
            boolean r7 = r5.contains(r7)     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            if (r7 == 0) goto L_0x0055
            java.lang.String r7 = "="
            int r7 = r5.indexOf(r7)     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            int r7 = r7 + 1
            int r8 = r5.length()     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            java.lang.String r7 = r5.substring(r7, r8)     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
            java.lang.String r6 = r7.trim()     // Catch:{ Throwable -> 0x0054, all -> 0x0083 }
        L_0x0047:
            if (r2 == 0) goto L_0x004c
            r2.close()     // Catch:{ Exception -> 0x0078 }
        L_0x004c:
            if (r4 == 0) goto L_0x0051
            r4.close()     // Catch:{ Exception -> 0x005a }
        L_0x0051:
            r3 = r4
            r1 = r2
        L_0x0053:
            return r6
        L_0x0054:
            r7 = move-exception
        L_0x0055:
            int r0 = r0 + 1
            int r0 = r0 + 1
            goto L_0x001f
        L_0x005a:
            r7 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x0053
        L_0x005e:
            r7 = move-exception
        L_0x005f:
            if (r1 == 0) goto L_0x0064
            r1.close()     // Catch:{ Exception -> 0x007a }
        L_0x0064:
            if (r3 == 0) goto L_0x0053
            r3.close()     // Catch:{ Exception -> 0x006a }
            goto L_0x0053
        L_0x006a:
            r7 = move-exception
            goto L_0x0053
        L_0x006c:
            r7 = move-exception
        L_0x006d:
            if (r1 == 0) goto L_0x0072
            r1.close()     // Catch:{ Exception -> 0x007c }
        L_0x0072:
            if (r3 == 0) goto L_0x0077
            r3.close()     // Catch:{ Exception -> 0x007e }
        L_0x0077:
            throw r7
        L_0x0078:
            r7 = move-exception
            goto L_0x004c
        L_0x007a:
            r7 = move-exception
            goto L_0x0064
        L_0x007c:
            r8 = move-exception
            goto L_0x0072
        L_0x007e:
            r8 = move-exception
            goto L_0x0077
        L_0x0080:
            r7 = move-exception
            r1 = r2
            goto L_0x006d
        L_0x0083:
            r7 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x006d
        L_0x0087:
            r7 = move-exception
            r1 = r2
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.collect.DeviceInfo2.getBattery0():java.lang.String");
    }
}
