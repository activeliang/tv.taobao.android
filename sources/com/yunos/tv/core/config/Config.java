package com.yunos.tv.core.config;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.tvtao.user.dclib.ZPDevice;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.ut.device.UTDevice;
import com.yunos.CloudUUIDWrapper;
import com.yunos.ott.sdk.core.Environment;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.R;
import com.yunos.tv.core.aqm.ActivityQueueManager;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tvtaobao.payment.BuildConfig;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.yunos.tvtaobao.payment.utils.TvTaoUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.intf.Mtop;

public class Config {
    public static final String CESHI = "142857";
    public static final String LIANMENG = "10003226";
    public static final String MOHE = "701229";
    private static final String TAG = "CoreConfig";
    public static final String YITIJI = "10004416";
    private static Config instance = null;
    private RunMode RUN_MODE = RunMode.PRODUCTION;
    private String buildId;
    private String channel;
    private String channelName;
    private boolean debug = true;
    private String mtopApiVersion;
    private String ttid;
    private String versionName;

    private Config() {
    }

    private Config(Context context) {
        this.debug = context.getResources().getBoolean(R.bool.isDebug);
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (info != null) {
            this.versionName = info.versionName;
        }
        String appkey = SharePreferences.getString("device_appkey", "");
        if (TextUtils.isEmpty(appkey)) {
            this.channel = context.getString(R.string.channelId);
        } else {
            this.channel = appkey;
        }
        this.ttid = this.channel + "@tvtaobao_android_" + AppInfo.getAppVersionName();
        this.channelName = context.getString(R.string.channel);
        String s = context.getString(R.string.runMode);
        s = this.debug ? SharePreferences.getString("device_env", s) : s;
        if ("PRODUCTION".equals(s)) {
            this.RUN_MODE = RunMode.PRODUCTION;
        } else if ("PREDEPLOY".equals(s)) {
            this.RUN_MODE = RunMode.PREDEPLOY;
            if (this.debug) {
                Toast.makeText(context, "这个是预发环境", 1).show();
            }
        } else if ("DAILY".equals(s)) {
            this.RUN_MODE = RunMode.DAILY;
            if (this.debug) {
                Toast.makeText(context, "这个是日常环境", 1).show();
            }
        } else {
            this.RUN_MODE = RunMode.PRODUCTION;
        }
        this.buildId = context.getString(R.string.buildId);
        this.mtopApiVersion = context.getString(R.string.mtopApiVersion);
        log(toString());
    }

    public String toString() {
        return "Config{debug=" + this.debug + ", RUN_MODE=" + this.RUN_MODE + ", channel='" + this.channel + '\'' + ", channelName='" + this.channelName + '\'' + ", ttid='" + this.ttid + '\'' + ", buildId='" + this.buildId + '\'' + ", mtopApiVersion='" + this.mtopApiVersion + '\'' + ", versionName='" + this.versionName + '\'' + '}';
    }

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config(CoreApplication.getApplication());
                }
            }
        }
        return instance;
    }

    private static void log(String msg) {
        ZpLogger.e(TAG, msg);
    }

    public static String getVersionName(Context context) {
        return getInstance().versionName;
    }

    public static String getAppKey() {
        if (getRunMode() == RunMode.DAILY) {
            return "4272";
        }
        if (getRunMode() == RunMode.PREDEPLOY) {
            return "23039499";
        }
        return "23039499";
    }

    public static String getChannel() {
        return getInstance().channel;
    }

    public static String getChannelName() {
        return getInstance().channelName;
    }

    public static String getAppId() {
        String rtn = getAppKey() + "@tvtaobao_android";
        log("getAppId=" + rtn);
        return rtn;
    }

    public static void setChannel(String channel2) {
        getInstance().channel = channel2;
        getInstance().ttid = getInstance().channel + "@tvtaobao_android_" + AppInfo.getAppVersionName();
        log("setChannel:" + getInstance().channel + "," + getInstance().ttid);
    }

    public static String getTTid() {
        String ttid2 = getInstance().ttid;
        log("getTTid:" + ttid2);
        return ttid2;
    }

    public static void setDebug(boolean debug2) {
        getInstance().debug = debug2;
        log("setDebug =" + getInstance().debug);
    }

    public static RunMode getRunMode() {
        return getInstance().RUN_MODE;
    }

    public static String getMtopDomain() {
        if (!Mtop.instance((Context) null).isInited()) {
            return null;
        }
        switch (getRunMode()) {
            case DAILY:
                return Mtop.instance((Context) null).getMtopConfig().mtopDomain.getDomain(EnvModeEnum.TEST);
            case PREDEPLOY:
                return Mtop.instance((Context) null).getMtopConfig().mtopDomain.getDomain(EnvModeEnum.PREPARE);
            case PRODUCTION:
                return Mtop.instance((Context) null).getMtopConfig().mtopDomain.getDomain(EnvModeEnum.ONLINE);
            default:
                return Mtop.instance((Context) null).getMtopConfig().mtopDomain.getDomain(EnvModeEnum.ONLINE);
        }
    }

    public static boolean isDebug() {
        return getInstance().debug;
    }

    public static String getBuildId() {
        return getInstance().buildId;
    }

    public static String getMtopApiVersion() {
        if ("default".equals(getInstance().mtopApiVersion)) {
            return "";
        }
        return getInstance().mtopApiVersion;
    }

    public static String getModelInfo(Context context) {
        if (context == null) {
            return "";
        }
        JSONObject jsonObject = new JSONObject();
        try {
            double d = new BigDecimal(getAvailMemory(context) / 1000.0d).setScale(0, 4).doubleValue();
            jsonObject.put("model", (Object) Build.MODEL);
            jsonObject.put("ram", (Object) d + "GB");
            jsonObject.put("cpu", (Object) Build.CPU_ABI + "，" + getMaxCpuFreq() + "GHz*");
            jsonObject.put("display", (Object) Build.DISPLAY);
            jsonObject.put("baseband_ver", (Object) getBaseband_Ver());
            jsonObject.put("brand", (Object) Build.BRAND);
            jsonObject.put("device", (Object) Build.DEVICE);
            jsonObject.put("codename", (Object) Build.VERSION.CODENAME);
            jsonObject.put(IWaStat.KEY_SDK_INT, (Object) Integer.valueOf(Build.VERSION.SDK_INT));
            jsonObject.put("hardware", (Object) Build.HARDWARE);
            jsonObject.put("host", (Object) Build.HOST);
            jsonObject.put("id", (Object) Build.ID);
            jsonObject.put("manufacturer", (Object) Build.MANUFACTURER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject == null ? "" : jsonObject.toString();
    }

    public static String getBaseband_Ver() {
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            return (String) cl.getMethod("get", new Class[]{String.class, String.class}).invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static double getMaxCpuFreq() {
        String result = "";
        try {
            InputStream in = new ProcessBuilder(new String[]{"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"}).start().getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return new BigDecimal(((double) Integer.parseInt(result.trim())) / 1000000.0d).setScale(1, 4).doubleValue();
    }

    private static double getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        try {
            am.getMemoryInfo(mi);
            return ((double) mi.totalMem) / 1048576.0d;
        } catch (NoSuchFieldError e) {
            e.printStackTrace();
            return ClientTraceData.b.f47a;
        } catch (Exception e2) {
            e2.printStackTrace();
            return ClientTraceData.b.f47a;
        }
    }

    public static String getUmtoken(Context context) {
        try {
            return SecurityGuardManager.getInstance(context).getUMIDComp().getSecurityToken(0);
        } catch (SecException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static String getWua(Context context) {
        try {
            return SecurityGuardManager.getInstance(context).getSecurityBodyComp().getSecurityBodyDataEx(String.valueOf(System.currentTimeMillis()), getAppKey(), (String) null, (HashMap<String, String>) null, 0, 0);
        } catch (SecException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static boolean isSimulator(Context context) {
        try {
            return SecurityGuardManager.getInstance(context).getSimulatorDetectComp().isSimulator();
        } catch (SecException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String getAndroidSystem(Context context) {
        ZpLogger.e(TAG, "android " + Build.VERSION.RELEASE);
        return "android " + Build.VERSION.RELEASE;
    }

    public static String getDeviceAppKey(Context context) {
        String appkey;
        if (context == null) {
            context = ActivityQueueManager.getTop();
        }
        if (Environment.getInstance().isYunos()) {
            appkey = (String) TvTaoSharedPerference.getSp(context, "device_appkey", "", context.getPackageName() + "_preferences");
            if (TextUtils.isEmpty(appkey)) {
                appkey = MOHE;
            }
        } else if (isDebug()) {
            appkey = (String) TvTaoSharedPerference.getSp(context, "device_appkey", "", context.getPackageName() + "_preferences");
            if (TextUtils.isEmpty(appkey)) {
                appkey = BuildConfig.CHANNELID;
            }
        } else {
            appkey = BuildConfig.CHANNELID;
        }
        ZpLogger.e(TAG, "appkey" + appkey);
        return appkey;
    }

    public static String getPackInfoAndAppkey(Context context) throws PackageManager.NameNotFoundException {
        if (!Environment.getInstance().isYunos()) {
            return BuildConfig.CHANNELID;
        }
        String appkey = (String) TvTaoSharedPerference.getSp(context, "device_appkey", "", context.getPackageName() + "_preferences");
        if (TextUtils.isEmpty(appkey)) {
            return MOHE;
        }
        return appkey;
    }

    public static String getExtParams() {
        JSONObject extParams = new JSONObject();
        extParams.put("uuid", (Object) CloudUUIDWrapper.getCloudUUID());
        extParams.put("wua", (Object) getWua(CoreApplication.getApplication()));
        extParams.put("utdid", (Object) UTDevice.getUtdid(CoreApplication.getApplication()));
        extParams.put("umtoken", (Object) TvTaoUtils.getUmtoken(CoreApplication.getApplication()));
        String zpDid = ZPDevice.getZpDid((Context) null);
        String uid = ZPDevice.getAugurZpId((Context) null);
        if (!TextUtils.isEmpty(zpDid)) {
            extParams.put("zpDid", (Object) zpDid);
        }
        if (!TextUtils.isEmpty(uid)) {
            extParams.put("augurZpUid", (Object) uid);
        }
        extParams.put("versionName", (Object) AppInfo.getAppVersionName());
        extParams.put("appkey", (Object) getChannel());
        extParams.put("isSimulator", (Object) Boolean.valueOf(isSimulator(CoreApplication.getApplication())));
        extParams.put("subkey", (Object) SharePreferences.getString("hy_device_subkey", ""));
        extParams.put("userAgent", (Object) getAndroidSystem(CoreApplication.getApplication()));
        extParams.put("mac", (Object) DeviceUtil.getStbID());
        return extParams.toString();
    }
}
