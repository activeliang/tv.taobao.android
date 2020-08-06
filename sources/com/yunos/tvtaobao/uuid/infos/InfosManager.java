package com.yunos.tvtaobao.uuid.infos;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import com.yunos.tvtaobao.uuid.TVAppUUIDImpl;
import com.yunos.tvtaobao.uuid.client.exception.InfosInCompleteException;
import com.yunos.tvtaobao.uuid.constants.Constants;
import com.yunos.tvtaobao.uuid.utils.Logger;
import com.yunos.tvtaobao.uuid.utils.SGMWrapper;
import com.yunos.tvtaobao.uuid.utils.StringUtil;
import com.yunos.tvtaobao.uuid.utils.SystemProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import mtopsdk.common.util.SymbolExpUtil;

public class InfosManager {
    public static final String default_mac = "00:11:22:33:44:55";
    public static final String default_uuid_FORD = "32CF0BD8B69435E2FAADECD2CCD0D3FC";
    private String mBuildTime;
    private Context mContext;
    private String mHardwareID;
    private String mMac;
    private String mProductModel;
    private String mProfile;
    private String mRealProductModel;
    private String mTTID;
    private String mUUID;

    public InfosManager(Context context, String productName, String ttid) throws InfosInCompleteException {
        this.mContext = context;
        this.mProductModel = productName;
        this.mTTID = ttid;
        collectAll();
        Logger.log_d("InfoManager: " + toString());
    }

    public String getAllInfos() {
        if (this.mMac == null) {
            return this.mHardwareID + this.mProductModel + this.mBuildTime;
        }
        return this.mHardwareID + this.mMac + this.mProductModel + this.mBuildTime;
    }

    public String getWifiMac() {
        return this.mMac;
    }

    public String getRealProductModel() {
        return this.mRealProductModel;
    }

    public String getProductModel() {
        return this.mProductModel;
    }

    public String getHardwareID() {
        return this.mHardwareID;
    }

    public String getProfile() {
        return this.mProfile;
    }

    public String getBuildTime() {
        return this.mBuildTime;
    }

    public String getLocalUUID() {
        return this.mUUID;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("uuid:");
        builder.append(this.mUUID);
        builder.append(" wifi-mac:");
        builder.append(this.mMac + " ");
        builder.append("realproductmodel:");
        builder.append(this.mRealProductModel + " ");
        builder.append("productmodel:");
        builder.append(this.mProductModel + " ");
        builder.append("hardwareid(android id):");
        builder.append(this.mHardwareID);
        builder.append(" profile:");
        builder.append(this.mProfile);
        return builder.toString();
    }

    private void collectAll() throws InfosInCompleteException {
        if (this.mProductModel == null) {
            this.mProductModel = SystemProperties.get("ro.product.model", (String) null);
            Logger.log("get model:" + this.mProductModel);
        }
        this.mMac = getWifiMac(this.mContext);
        Logger.log("try get wifi-mac, mMac=" + this.mMac);
        if (TextUtils.isEmpty(this.mMac) || default_mac.equalsIgnoreCase(this.mMac)) {
            Logger.log("can not get wifi-mac, try get eth0 mac");
            this.mMac = getEth0Mac();
            Logger.log("try get eth0-mac, mMac=" + this.mMac);
        }
        if (!TextUtils.isEmpty(this.mMac)) {
            this.mHardwareID = String.valueOf(System.currentTimeMillis());
        } else if (TVAppUUIDImpl.usingAndroidID) {
            Logger.log("can not get mac, using android id.");
            this.mHardwareID = Constants.HARDWARE_ID_PREFIX + getAndroidID();
        } else {
            Logger.log("can not get mac,used default.");
            this.mMac = default_mac;
            return;
        }
        Logger.log("--- hardwareid:" + this.mHardwareID);
        Logger.log("--- wifi:" + this.mMac);
        this.mRealProductModel = Build.MODEL;
        this.mBuildTime = SystemProperties.get("ro.build.date.utc", "");
        if (this.mBuildTime == null || this.mBuildTime.length() < 10) {
            this.mBuildTime = "1388505600";
        }
        this.mProfile = collectProfile();
        Logger.log("before SGMWrapper");
        SGMWrapper sgm = new SGMWrapper(this.mContext, TVAppUUIDImpl.mSMGAuthcode);
        this.mUUID = sgm.getUUID();
        sgm.saveWlan(this.mMac);
        Logger.log("after SGMWrapper and save mac");
    }

    private static String getFileContent(String filePath) {
        File wlan_addr_dev = new File(filePath);
        if (!wlan_addr_dev.exists()) {
            return null;
        }
        try {
            StringBuffer fileData = new StringBuffer(32);
            BufferedReader reader = new BufferedReader(new FileReader(wlan_addr_dev));
            char[] buf = new char[32];
            int numRead = reader.read(buf, 0, 17);
            Logger.log(" get len from sys :" + numRead);
            if (numRead != 17) {
                return null;
            }
            fileData.append(String.valueOf(buf, 0, numRead));
            reader.close();
            return fileData.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private String getWifiMacFromSysNode() {
        String mac_from_dev = null;
        try {
            mac_from_dev = getFileContent("/sys/class/net/wlan0/address");
            if (mac_from_dev != null) {
                Logger.log(" get mac from sys " + mac_from_dev);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac_from_dev;
    }

    private String getWifiMac(Context context) {
        int waitTimes = 20;
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        Method isWifiApEnabledMethod = null;
        try {
            isWifiApEnabledMethod = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled", new Class[0]);
        } catch (SecurityException e1) {
            e1.printStackTrace();
            Logger.loge("can not get isWifiApEnabled by reflection");
        } catch (NoSuchMethodException e12) {
            e12.printStackTrace();
            Logger.loge("can not get isWifiApEnabled by reflection");
        }
        boolean isWifiApEnabled = false;
        if (isWifiApEnabledMethod != null) {
            isWifiApEnabledMethod.setAccessible(true);
            try {
                isWifiApEnabled = ((Boolean) isWifiApEnabledMethod.invoke(wifiManager, new Object[0])).booleanValue();
                Logger.log("isWifiApEnabled: " + isWifiApEnabled);
            } catch (IllegalArgumentException e13) {
                e13.printStackTrace();
                Logger.loge("can not call isWifiApEnabled by reflection");
            } catch (IllegalAccessException e14) {
                e14.printStackTrace();
                Logger.loge("can not call isWifiApEnabled by reflection");
            } catch (InvocationTargetException e15) {
                e15.printStackTrace();
                Logger.loge("can not call isWifiApEnabled by reflection");
            }
        }
        if (wifiManager.isWifiEnabled() || isWifiApEnabled) {
            Logger.log_d("wifi is enabled.");
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo == null) {
                Logger.loge("getConnectionInfo error");
                return default_mac;
            }
            String wifiMAC = wifiInfo.getMacAddress();
            if (wifiMAC == null || wifiMAC.contains("00:00:00:00")) {
                return getWifiMacFromSysNode();
            }
            return wifiMAC;
        }
        Logger.log_d("wifi is not enabled, open it.");
        if (!setWifiEnabled(context, true)) {
            Logger.loge("setWifiEnable failed");
            return default_mac;
        }
        while (true) {
            int waitTimes2 = waitTimes;
            waitTimes = waitTimes2 - 1;
            if (waitTimes2 != 0 && 3 != wifiManager.getWifiState()) {
                try {
                    Logger.log_d("sleep");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Logger.loge("sleep exception");
                }
            }
        }
        if (3 == wifiManager.getWifiState()) {
            Logger.log_d("wifi is opened.");
            WifiInfo wifiInfo2 = wifiManager.getConnectionInfo();
            if (wifiInfo2 == null) {
                Logger.loge("getConnectionInfo error");
                return default_mac;
            }
            String wifiMAC2 = wifiInfo2.getMacAddress();
            if (wifiMAC2 == null || wifiMAC2.contains("00:00:00:00")) {
                wifiMAC2 = getWifiMacFromSysNode();
            }
            setWifiEnabled(context, false);
            return wifiMAC2;
        }
        Logger.loge("we can not open wifi.");
        return default_mac;
    }

    private boolean setWifiEnabled(Context context, boolean enabled) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                return wifiManager.setWifiEnabled(enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getEth0Mac() {
        try {
            Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();
            while (localEnumeration.hasMoreElements()) {
                NetworkInterface localNetworkInterface = localEnumeration.nextElement();
                String interfaceName = localNetworkInterface.getDisplayName();
                if (interfaceName != null && interfaceName.equals("eth0")) {
                    String mac = convertToMac(localNetworkInterface.getHardwareAddress());
                    if (mac == null || !mac.startsWith("0:")) {
                        return mac;
                    }
                    return "0" + mac;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertToMac(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            byte b = mac[i];
            if (b >= 0 && b < 16) {
                sb.append("0" + Integer.toHexString(b));
            } else if (b >= 16) {
                sb.append(Integer.toHexString(b));
            } else {
                sb.append(Integer.toHexString(b + 256));
            }
            if (i != mac.length - 1) {
                sb.append(SymbolExpUtil.SYMBOL_COLON);
            }
        }
        return sb.toString();
    }

    private String getAndroidID() {
        return Settings.Secure.getString(this.mContext.getContentResolver(), "android_id");
    }

    private String collectProfile() {
        String firmwareVersion;
        StringBuilder sb = new StringBuilder();
        if (this.mMac == null) {
            sb.append(getPropertyFormat("uuid.identifier", "hardware.id"));
        }
        if ("user".equalsIgnoreCase(Build.TYPE)) {
            firmwareVersion = "0.9.0-R-20170301.0000";
        } else {
            firmwareVersion = "0.9.0-D-20170301.0000";
        }
        sb.append("[ro.build.version.release]:[");
        sb.append(firmwareVersion);
        sb.append("]");
        sb.append(getPropertyFormat("ro.product.model"));
        sb.append(getPropertyFormat("uuid.register", Constants.UUID_VERSION));
        sb.append(getPropertyFormat("ro.yunos.romvendor", this.mTTID));
        return sb.toString();
    }

    private String getPropertyFormat(String string) {
        String s = SystemProperties.get(string, "");
        if (!StringUtil.isEmpty(s)) {
            return getPropertyFormat(string, s);
        }
        return null;
    }

    private String getPropertyFormat(String name, String value) {
        return "[" + name + "]" + SymbolExpUtil.SYMBOL_COLON + "[" + value + "]";
    }
}
