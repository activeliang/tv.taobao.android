package com.ta.audid.store;

import android.content.Context;
import android.text.TextUtils;
import com.ta.audid.Variables;
import com.ta.audid.collect.DeviceInfo2;
import com.ta.audid.upload.UtdidKeyFile;
import com.ta.audid.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class SdcardDeviceModle {
    private static final String MODULE_GSID = "gsid";
    private static final String MODULE_IMEI = "imei";
    private static final String MODULE_IMSI = "imsi";
    private static Map<String, String> mSdcardDeviceModle;

    public static String getModuleImei() {
        try {
            return getModule("imei");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getModuleImsi() {
        try {
            return getModule("imsi");
        } catch (Exception e) {
            return "";
        }
    }

    public static synchronized void writeSdcardDeviceModle(String imei, String imsi) {
        synchronized (SdcardDeviceModle.class) {
            Context context = Variables.getInstance().getContext();
            if (context != null) {
                try {
                    if (checkSdcardDeviceModle()) {
                        if (!TextUtils.isEmpty(imei)) {
                            mSdcardDeviceModle.put("imei", imei);
                        }
                        if (!TextUtils.isEmpty(imsi)) {
                            mSdcardDeviceModle.put("imsi", imsi);
                        }
                    } else {
                        mSdcardDeviceModle = new HashMap();
                        if (!TextUtils.isEmpty(imei)) {
                            mSdcardDeviceModle.put("imei", imei);
                        }
                        if (!TextUtils.isEmpty(imsi)) {
                            mSdcardDeviceModle.put("imsi", imsi);
                        }
                        String androidid = DeviceInfo2.getAndroidID(context);
                        if (!TextUtils.isEmpty(androidid)) {
                            mSdcardDeviceModle.put(MODULE_GSID, androidid);
                        }
                        UtdidKeyFile.writeSdcardDeviceModleFile(UtdidContentUtil.getEncodedContent(new JSONObject(mSdcardDeviceModle).toString()));
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private static synchronized String getModule(String module) {
        String str;
        synchronized (SdcardDeviceModle.class) {
            if (Variables.getInstance().getContext() == null) {
                str = "";
            } else if (mSdcardDeviceModle != null) {
                str = mSdcardDeviceModle.get(module);
            } else if (checkSdcardDeviceModle()) {
                str = mSdcardDeviceModle.get(module);
            } else {
                clearSdcardDeviceModle();
                str = "";
            }
        }
        return str;
    }

    private static void clearSdcardDeviceModle() {
        try {
            mSdcardDeviceModle.clear();
            mSdcardDeviceModle = null;
            UtdidKeyFile.writeSdcardDeviceModleFile("");
        } catch (Exception e) {
        }
    }

    private static boolean checkSdcardDeviceModle() {
        if (Variables.getInstance().getContext() == null) {
            return false;
        }
        try {
            mSdcardDeviceModle = JsonUtils.jsonToMap(UtdidContentUtil.getDecodedContent(UtdidKeyFile.readSdcardDeviceModleFile()));
            if (DeviceInfo2.getAndroidID(Variables.getInstance().getContext()).equals(mSdcardDeviceModle.get(MODULE_GSID))) {
                return true;
            }
            clearSdcardDeviceModle();
            return false;
        } catch (Exception e) {
            clearSdcardDeviceModle();
            return false;
        }
    }
}
