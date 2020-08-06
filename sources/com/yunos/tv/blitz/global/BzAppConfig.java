package com.yunos.tv.blitz.global;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.staticdatastore.IStaticDataStoreComponent;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.security.MessageDigest;
import org.json.JSONObject;

public class BzAppConfig {
    public static final String DEFAULT_TTID = "hybrid@windvane_android_1.0.0";
    public static final String DEFAULT_UA = " WindVane/1.0.0";
    public static final String VERSION = "1.0.0";
    static String appVersionName;
    static int appVersionNum;
    private static BzAppConfig config;
    public static BzAppMain context;
    public static BzEnvEnum env = BzEnvEnum.ONLINE;
    private String appKey;
    private String appSecret;
    private String appTag;
    private String appVersion;
    private String deviceId;
    private String groupName;
    private String groupVersion;
    private String imei;
    private String imsi;
    private String tokenAppkey;
    private String ttid;
    private String uuid;

    private BzAppConfig() {
    }

    public static synchronized BzAppConfig getInstance() {
        BzAppConfig bzAppConfig;
        synchronized (BzAppConfig.class) {
            if (config == null) {
                config = new BzAppConfig();
            }
            bzAppConfig = config;
        }
        return bzAppConfig;
    }

    public boolean initParams(BzAppParams params) {
        if (params == null) {
            return false;
        }
        BzDebugLog.i("windvane", "initParams: deviceId=" + params.deviceId + ",appKey=" + params.appKey + ",uuid=" + params.uuid);
        if (TextUtils.isEmpty(params.appKey)) {
            throw new NullPointerException("initParams error, appKey is empty");
        }
        if (TextUtils.isEmpty(params.ttid)) {
            this.ttid = DEFAULT_TTID;
        } else {
            this.ttid = params.ttid;
        }
        this.imei = params.imei;
        this.imsi = params.imsi;
        this.deviceId = params.deviceId;
        this.appKey = params.appKey;
        this.appSecret = params.appSecret;
        this.appTag = params.appTag;
        this.appVersion = params.appVersion;
        this.tokenAppkey = params.tokenAppKey;
        if (!TextUtils.isEmpty(params.uuid)) {
            this.uuid = params.uuid;
        } else {
            this.uuid = "";
        }
        return true;
    }

    public String getTtid() {
        return this.ttid;
    }

    public String getImei() {
        return this.imei;
    }

    public String getImsi() {
        return this.imsi;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getTokenAppkey() {
        return this.tokenAppkey;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public String getAppTag() {
        return this.appTag;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public static String getMtopUrl() {
        return "http://api." + env.getValue() + ".taobao.com/rest/api3.do";
    }

    public static String getCdnConfigUrlPre() {
        return getH5Host() + "/bizcache/";
    }

    public static String getH5Host() {
        return "http://h5." + env.getValue() + ".taobao.com";
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName2) {
        this.groupName = groupName2;
    }

    public String getGroupVersion() {
        return this.groupVersion;
    }

    public void setGroupVersion(String groupVersion2) {
        this.groupVersion = groupVersion2;
    }

    public String getUuid() {
        if (!TextUtils.isEmpty(this.uuid)) {
            return this.uuid;
        }
        this.uuid = "";
        return "";
    }

    public void setUuid(String uuid2) {
        this.uuid = uuid2;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE.split("-")[0];
    }

    public String getExtraDataFromSecurity(Context ctx, String param, int callback, String authCode) {
        IStaticDataStoreComponent sdsComp;
        String appKey2;
        String pKey;
        Log.d("BLITZ", "getExtraDataFromSecurity start");
        String result = null;
        try {
            JSONObject obj = new JSONObject(param);
            if (obj != null) {
                String str_videoids = obj.optString("videoids");
                String str_ts = obj.optString("ts");
                try {
                    SecurityGuardManager sgMgr = SecurityGuardManager.getInstance(context.getContext());
                    if (!(sgMgr == null || (sdsComp = sgMgr.getStaticDataStoreComp()) == null || (appKey2 = sdsComp.getExtraData("client_i", authCode)) == null || (pKey = sdsComp.getExtraData("client_s", authCode)) == null)) {
                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                        md5.update((str_videoids + str_ts + pKey).getBytes());
                        byte[] m = md5.digest();
                        StringBuilder md_test = new StringBuilder(m.length * 2);
                        int length = m.length;
                        for (int i = 0; i < length; i++) {
                            md_test.append(String.format("%02x", new Object[]{Integer.valueOf(m[i] & OnReminderListener.RET_FULL)}));
                        }
                        String res = md_test.toString().toLowerCase();
                        result = Base64.encodeToString((appKey2 + "_" + str_ts + "_" + res).getBytes(), 0);
                    }
                } catch (SecException e) {
                    System.out.println("error code is " + e.getErrorCode());
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        BzBaseActivity baseActivity = (BzBaseActivity) ((Activity) ctx);
        if (result != null) {
            BzResult ret = new BzResult();
            ret.setSuccess();
            ret.addData("result", result);
            baseActivity.getBlitzContext().replyCallBack(callback, true, ret.toJsonString());
        } else {
            BzResult ret2 = new BzResult();
            ret2.setResult("HY_FAILED");
            baseActivity.getBlitzContext().replyCallBack(callback, true, ret2.toJsonString());
        }
        return result;
    }

    public static String getAppVersionName() {
        if (appVersionNum == 0) {
            try {
                PackageInfo info = context.getContext().getPackageManager().getPackageInfo(context.getContext().getPackageName(), 0);
                appVersionNum = info.versionCode;
                appVersionName = info.versionName;
            } catch (Exception e) {
                Log.e("SystemConfig", "读取版本号异常" + e.toString());
            }
        }
        return appVersionName;
    }

    public static int getAppVersionCode() {
        if (appVersionNum == 0) {
            try {
                PackageInfo info = context.getContext().getPackageManager().getPackageInfo(context.getContext().getPackageName(), 0);
                appVersionNum = info.versionCode;
                appVersionName = info.versionName;
            } catch (Exception e) {
                Log.e("SystemConfig", "读取版本号异常" + e.toString());
            }
        }
        return appVersionNum;
    }
}
