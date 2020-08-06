package com.taobao.taobaoavsdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ta.utdid2.device.UTDevice;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AndroidUtils {
    private static final String TAG = "CpuManager";
    private static float mMaxCpuFreq = 0.0f;
    private static double mScreenSize = ClientTraceData.b.f47a;
    private static boolean sSupportH256Goted = false;

    public static double getScreenSize(Context context) {
        int realWidth;
        int realHeight;
        if (mScreenSize != ClientTraceData.b.f47a) {
            return mScreenSize;
        }
        try {
            if (context instanceof Activity) {
                Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
                DisplayMetrics dm = new DisplayMetrics();
                display.getMetrics(dm);
                if (Build.VERSION.SDK_INT >= 17) {
                    Point size = new Point();
                    display.getRealSize(size);
                    realWidth = size.x;
                    realHeight = size.y;
                } else if (Build.VERSION.SDK_INT >= 17 || Build.VERSION.SDK_INT < 14) {
                    realWidth = dm.widthPixels;
                    realHeight = dm.heightPixels;
                } else {
                    Method getRawW = Display.class.getMethod("getRawWidth", new Class[0]);
                    Method getRawH = Display.class.getMethod("getRawHeight", new Class[0]);
                    realWidth = ((Integer) getRawW.invoke(display, new Object[0])).intValue();
                    realHeight = ((Integer) getRawH.invoke(display, new Object[0])).intValue();
                }
                mScreenSize = Math.sqrt(Math.pow((double) (((float) realWidth) / dm.xdpi), 2.0d) + Math.pow((double) (((float) realHeight) / dm.ydpi), 2.0d));
            }
        } catch (Exception e) {
        }
        return mScreenSize;
    }

    public static boolean parseBoolean(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            return false;
        }
    }

    public static int parseInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long parseLong(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float parseFloat(String str) {
        if (str == null) {
            return 0.0f;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public static Double parseDouble(String str) {
        double result;
        if (str == null) {
            return Double.valueOf(ClientTraceData.b.f47a);
        }
        try {
            result = Double.parseDouble(str);
        } catch (Exception e) {
            result = ClientTraceData.b.f47a;
        }
        return Double.valueOf(result);
    }

    public static String appendUri(String uri, StringBuilder appendQuery) {
        String newQuery;
        try {
            Uri olduri = Uri.parse(uri);
            String newQuery2 = olduri.getEncodedQuery();
            if (TextUtils.isEmpty(newQuery2)) {
                newQuery = appendQuery.toString();
            } else {
                newQuery = appendQuery + "&" + newQuery2;
            }
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(olduri.getScheme()).encodedAuthority(olduri.getEncodedAuthority()).encodedPath(olduri.getEncodedPath()).encodedQuery(newQuery).fragment(olduri.getEncodedFragment());
            return builder.build().toString();
        } catch (Exception e) {
            return uri;
        }
    }

    public static boolean isInListWildcards(String checkStr, String listStr, String wildcard) {
        List<String> list = null;
        try {
            JSONArray parseObj = JSON.parseArray(listStr);
            if (parseObj.size() > 0) {
                List<String> list2 = new ArrayList<>();
                int i = 0;
                while (i < parseObj.size()) {
                    try {
                        if (!TextUtils.isEmpty(parseObj.getString(i))) {
                            list2.add(parseObj.getString(i));
                        }
                        i++;
                    } catch (Throwable th) {
                        List<String> list3 = list2;
                    }
                }
                list = list2;
            }
            if (!TextUtils.isEmpty(checkStr) && list != null) {
                for (String s : list) {
                    if (!TextUtils.isEmpty(s) && (s.equalsIgnoreCase(checkStr) || s.equals(wildcard))) {
                        return true;
                    }
                }
            }
        } catch (Throwable th2) {
        }
        return false;
    }

    public static boolean isInList(String checkStr, String listStr) {
        List<String> list = null;
        try {
            JSONArray parseObj = JSON.parseArray(listStr);
            if (parseObj.size() > 0) {
                List<String> list2 = new ArrayList<>();
                int i = 0;
                while (i < parseObj.size()) {
                    try {
                        if (!TextUtils.isEmpty(parseObj.getString(i))) {
                            list2.add(parseObj.getString(i));
                        }
                        i++;
                    } catch (Throwable th) {
                        list = list2;
                    }
                }
                list = list2;
            }
        } catch (Throwable th2) {
        }
        if (!TextUtils.isEmpty(checkStr) && list != null) {
            for (String s : list) {
                if (!TextUtils.isEmpty(s) && s.equalsIgnoreCase(checkStr)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getPlayerCore(ConfigAdapter configAdapter, String configGroup, String keyStr, int defaultValue) {
        String result = null;
        try {
            JSONObject parseObj = JSON.parseObject(configAdapter.getConfig(configGroup, "PlayerCoreType", (String) null));
            result = parseObj.getString(keyStr.toUpperCase());
            if (TextUtils.isEmpty(result)) {
                result = parseObj.getString("ALL_BIZCODE");
            }
        } catch (Throwable th) {
        }
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        }
        if ("mediaplayer".equals(result)) {
            return 2;
        }
        if ("ijkplayer".equals(result)) {
            return 1;
        }
        if ("taobaoplayer".equals(result)) {
            return 3;
        }
        return defaultValue;
    }

    public static String getCPUName() {
        if ("qcom".equals(Build.HARDWARE)) {
            return Build.BOARD;
        }
        return Build.HARDWARE;
    }

    public static String getMaxCpuFreq(int cpuNo) {
        String result = "";
        InputStream in = null;
        try {
            InputStream in2 = new ProcessBuilder(new String[]{"/system/bin/cat", String.format("/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq", new Object[]{Integer.valueOf(cpuNo)})}).start().getInputStream();
            if (in2 != null) {
                byte[] re = new byte[24];
                while (in2.read(re) != -1) {
                    result = result + new String(re);
                }
                in2.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        DWLogUtils.d(TAG, "CPU max freq: " + result.trim());
        return result.trim();
    }

    public static boolean isSupportH265(String maxFreq) {
        try {
            if (TextUtils.isEmpty(maxFreq)) {
                return false;
            }
            float maxTargetFreq = Float.parseFloat(maxFreq);
            if (maxTargetFreq < 1.2f) {
                maxTargetFreq = 1.8f;
            }
            if (!sSupportH256Goted || mMaxCpuFreq <= maxTargetFreq || maxTargetFreq < 1.2f) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public static int getNumCores() {
        try {
            File[] files = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            DWLogUtils.d(TAG, "CPU Count: " + files.length);
            return files.length;
        } catch (Exception e) {
            DWLogUtils.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            return 1;
        }
    }

    public static void getCpuMaxFreq() {
        int cpuCores;
        try {
            if (Build.VERSION.SDK_INT >= 23 && (cpuCores = getNumCores()) >= 4) {
                float maxCpuFreq = 0.0f;
                for (int i = 0; i < cpuCores; i++) {
                    float cpuFreq = parseFloat(getMaxCpuFreq(i)) / 1000000.0f;
                    if (cpuFreq > maxCpuFreq) {
                        maxCpuFreq = cpuFreq;
                    }
                }
                mMaxCpuFreq = maxCpuFreq;
            }
        } catch (Throwable th) {
            mMaxCpuFreq = 0.0f;
        } finally {
            sSupportH256Goted = true;
        }
    }

    public static String getVideoDefinition(String videoCodec, String definition) {
        return videoCodec + "_" + definition;
    }

    public static String GenerateVideoToken(Activity context) {
        try {
            return UTDevice.getUtdid(context) + System.currentTimeMillis();
        } catch (Throwable th) {
            return String.valueOf(System.currentTimeMillis());
        }
    }

    public static String getUserAgent(Context context) {
        if (context == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("appVersion/");
        try {
            stringBuilder.append(context.getPackageManager().getPackageInfo(context.getPackageName(), 16384).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            stringBuilder.append("unknown");
        }
        stringBuilder.append(";appID/");
        stringBuilder.append(context.getPackageName());
        stringBuilder.append(";systemVersion/");
        stringBuilder.append(Build.VERSION.SDK_INT);
        stringBuilder.append(";systemName/Android");
        return stringBuilder.toString();
    }
}
