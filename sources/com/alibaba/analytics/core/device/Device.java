package com.alibaba.analytics.core.device;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.utils.Base64_2;
import com.alibaba.analytics.utils.PhoneInfoUtils2;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.analytics.utils.SystemUtils;
import com.ut.device.UTDevice;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;

public class Device {
    private static String PATH_AND_BIN_MONKEY = "/system/bin/monkey";
    private static String PATH_AND_BIN_SETPROP = "/system/bin/setprop";
    private static DeviceInfo mDeviceInfo = null;

    static void saveDeviceMetadataToNewPPC(Context aContext, String pIMEI, String pIMSI) {
        PersistentConfiguration lNewDevicePPC;
        if (aContext != null && !StringUtils.isEmpty(pIMEI) && !StringUtils.isEmpty(pIMSI) && (lNewDevicePPC = HardConfig.getNewDevicePersistentConfig(aContext)) != null) {
            String lBase64IMEI = null;
            String lBase64IMSI = null;
            try {
                lBase64IMEI = Base64_2.encodeBase64String(pIMEI.getBytes("UTF-8"));
                lBase64IMSI = Base64_2.encodeBase64String(pIMSI.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!StringUtils.isEmpty(lBase64IMEI) && !StringUtils.isEmpty(lBase64IMSI)) {
                lNewDevicePPC.putString("EI", lBase64IMEI);
                lNewDevicePPC.putString("SI", lBase64IMSI);
                lNewDevicePPC.commit();
            }
        }
    }

    private static void _checkIMEISI(Context aContext) {
        SharedPreferences lAlvin3SP = aContext.getSharedPreferences("Alvin3", 0);
        SharedPreferences lUTCommonSP = aContext.getSharedPreferences("UTCommon", 0);
        if (lUTCommonSP != null && lAlvin3SP != null) {
            String lAlvin3Imei = lAlvin3SP.getString("EI", (String) null);
            String lAlvin3Imsi = lAlvin3SP.getString("SI", (String) null);
            if (!TextUtils.isEmpty(lAlvin3Imei) && !TextUtils.isEmpty(lAlvin3Imsi)) {
                String lUTCommonImei = lUTCommonSP.getString("EI", (String) null);
                String lUTCommonImsi = lUTCommonSP.getString("SI", (String) null);
                if (!lAlvin3Imei.equals(lUTCommonImei)) {
                    SharedPreferences.Editor lEditor = lUTCommonSP.edit();
                    lEditor.putString("EI", lAlvin3Imei);
                    lEditor.commit();
                }
                if (!lAlvin3Imsi.equals(lUTCommonImsi)) {
                    SharedPreferences.Editor lEditor2 = lUTCommonSP.edit();
                    lEditor2.putString("SI", lAlvin3Imsi);
                    lEditor2.commit();
                }
            }
        }
    }

    static DeviceInfo getDeviceMetadataFromPPC(Context aContext) {
        if (aContext != null) {
            _checkIMEISI(aContext);
            PersistentConfiguration lNewDevicePPC = HardConfig.getNewDevicePersistentConfig(aContext);
            if (lNewDevicePPC != null) {
                String lBase64IMEI = lNewDevicePPC.getString("EI");
                String lBase64IMSI = lNewDevicePPC.getString("SI");
                String lBase64DID = lBase64IMEI;
                if (!StringUtils.isEmpty(lBase64IMEI) && !StringUtils.isEmpty(lBase64IMSI) && !StringUtils.isEmpty(lBase64DID)) {
                    String lIMEI = null;
                    String lIMSI = null;
                    String lDID = null;
                    try {
                        String lIMEI2 = new String(Base64_2.decode(lBase64IMEI.getBytes("UTF-8")), "UTF-8");
                        try {
                            String lIMSI2 = new String(Base64_2.decode(lBase64IMSI.getBytes("UTF-8")), "UTF-8");
                            try {
                                lDID = new String(Base64_2.decode(lBase64DID.getBytes("UTF-8")), "UTF-8");
                                lIMSI = lIMSI2;
                                lIMEI = lIMEI2;
                            } catch (UnsupportedEncodingException e) {
                                e = e;
                                lIMSI = lIMSI2;
                                lIMEI = lIMEI2;
                                e.printStackTrace();
                                DeviceInfo lDeviceInfo = new DeviceInfo();
                                lDeviceInfo.setDeviceId(lDID);
                                lDeviceInfo.setImei(lIMEI);
                                lDeviceInfo.setImsi(lIMSI);
                                return lDeviceInfo;
                            } catch (IOException e2) {
                                e = e2;
                                lIMSI = lIMSI2;
                                lIMEI = lIMEI2;
                                e.printStackTrace();
                                DeviceInfo lDeviceInfo2 = new DeviceInfo();
                                lDeviceInfo2.setDeviceId(lDID);
                                lDeviceInfo2.setImei(lIMEI);
                                lDeviceInfo2.setImsi(lIMSI);
                                return lDeviceInfo2;
                            }
                        } catch (UnsupportedEncodingException e3) {
                            e = e3;
                            lIMEI = lIMEI2;
                            e.printStackTrace();
                            DeviceInfo lDeviceInfo22 = new DeviceInfo();
                            lDeviceInfo22.setDeviceId(lDID);
                            lDeviceInfo22.setImei(lIMEI);
                            lDeviceInfo22.setImsi(lIMSI);
                            return lDeviceInfo22;
                        } catch (IOException e4) {
                            e = e4;
                            lIMEI = lIMEI2;
                            e.printStackTrace();
                            DeviceInfo lDeviceInfo222 = new DeviceInfo();
                            lDeviceInfo222.setDeviceId(lDID);
                            lDeviceInfo222.setImei(lIMEI);
                            lDeviceInfo222.setImsi(lIMSI);
                            return lDeviceInfo222;
                        }
                    } catch (UnsupportedEncodingException e5) {
                        e = e5;
                        e.printStackTrace();
                        DeviceInfo lDeviceInfo2222 = new DeviceInfo();
                        lDeviceInfo2222.setDeviceId(lDID);
                        lDeviceInfo2222.setImei(lIMEI);
                        lDeviceInfo2222.setImsi(lIMSI);
                        return lDeviceInfo2222;
                    } catch (IOException e6) {
                        e = e6;
                        e.printStackTrace();
                        DeviceInfo lDeviceInfo22222 = new DeviceInfo();
                        lDeviceInfo22222.setDeviceId(lDID);
                        lDeviceInfo22222.setImei(lIMEI);
                        lDeviceInfo22222.setImsi(lIMSI);
                        return lDeviceInfo22222;
                    }
                    if (!StringUtils.isEmpty(lIMEI) && !StringUtils.isEmpty(lIMSI) && !StringUtils.isEmpty(lDID)) {
                        DeviceInfo lDeviceInfo222222 = new DeviceInfo();
                        lDeviceInfo222222.setDeviceId(lDID);
                        lDeviceInfo222222.setImei(lIMEI);
                        lDeviceInfo222222.setImsi(lIMSI);
                        return lDeviceInfo222222;
                    }
                }
            }
            PersistentConfiguration ppc = HardConfig.getDevicePersistentConfig(aContext);
            if (ppc != null) {
                String imei = ppc.getString("EI");
                String imsi = ppc.getString("SI");
                String did = ppc.getString("DID");
                if (!StringUtils.isEmpty(imei) && !StringUtils.isEmpty(imsi)) {
                    String lIMEI3 = null;
                    String lIMSI3 = null;
                    try {
                        String lIMEI4 = new String(Base64_2.decode(imei.getBytes("UTF-8")), "UTF-8");
                        try {
                            lIMSI3 = new String(Base64_2.decode(imsi.getBytes("UTF-8")), "UTF-8");
                            lIMEI3 = lIMEI4;
                        } catch (UnsupportedEncodingException e7) {
                            e = e7;
                            lIMEI3 = lIMEI4;
                            e.printStackTrace();
                            DeviceInfo lDeviceInfo3 = new DeviceInfo();
                            lDeviceInfo3.setDeviceId(did);
                            lDeviceInfo3.setImei(imei);
                            lDeviceInfo3.setImsi(imsi);
                            saveDeviceMetadataToNewPPC(aContext, lIMEI3, lIMSI3);
                            return lDeviceInfo3;
                        } catch (IOException e8) {
                            e = e8;
                            lIMEI3 = lIMEI4;
                            e.printStackTrace();
                            DeviceInfo lDeviceInfo32 = new DeviceInfo();
                            lDeviceInfo32.setDeviceId(did);
                            lDeviceInfo32.setImei(imei);
                            lDeviceInfo32.setImsi(imsi);
                            saveDeviceMetadataToNewPPC(aContext, lIMEI3, lIMSI3);
                            return lDeviceInfo32;
                        }
                    } catch (UnsupportedEncodingException e9) {
                        e = e9;
                        e.printStackTrace();
                        DeviceInfo lDeviceInfo322 = new DeviceInfo();
                        lDeviceInfo322.setDeviceId(did);
                        lDeviceInfo322.setImei(imei);
                        lDeviceInfo322.setImsi(imsi);
                        saveDeviceMetadataToNewPPC(aContext, lIMEI3, lIMSI3);
                        return lDeviceInfo322;
                    } catch (IOException e10) {
                        e = e10;
                        e.printStackTrace();
                        DeviceInfo lDeviceInfo3222 = new DeviceInfo();
                        lDeviceInfo3222.setDeviceId(did);
                        lDeviceInfo3222.setImei(imei);
                        lDeviceInfo3222.setImsi(imsi);
                        saveDeviceMetadataToNewPPC(aContext, lIMEI3, lIMSI3);
                        return lDeviceInfo3222;
                    }
                    DeviceInfo lDeviceInfo32222 = new DeviceInfo();
                    lDeviceInfo32222.setDeviceId(did);
                    lDeviceInfo32222.setImei(imei);
                    lDeviceInfo32222.setImsi(imsi);
                    saveDeviceMetadataToNewPPC(aContext, lIMEI3, lIMSI3);
                    return lDeviceInfo32222;
                }
            }
        }
        DeviceInfo lDeviceInfo4 = new DeviceInfo();
        String lIMEI5 = PhoneInfoUtils2.getImei(aContext);
        String lIMSI4 = PhoneInfoUtils2.getImsi(aContext);
        lDeviceInfo4.setImei(lIMEI5);
        lDeviceInfo4.setImsi(lIMSI4);
        lDeviceInfo4.setDeviceId(lIMEI5);
        saveDeviceMetadataToNewPPC(aContext, lIMEI5, lIMSI4);
        return lDeviceInfo4;
    }

    private static DeviceInfo _initDeviceMetadata(Context aContext) {
        if (aContext == null) {
            return null;
        }
        DeviceInfo lDeviceInfo = getDeviceMetadataFromPPC(aContext);
        lDeviceInfo.setUtdid(UTDevice.getUtdid(aContext));
        if (StringUtils.isEmpty(lDeviceInfo.getImei())) {
            lDeviceInfo.setImei(PhoneInfoUtils2.getImei(aContext));
        }
        if (!StringUtils.isEmpty(lDeviceInfo.getImsi())) {
            return lDeviceInfo;
        }
        lDeviceInfo.setImsi(PhoneInfoUtils2.getImsi(aContext));
        return lDeviceInfo;
    }

    public static synchronized DeviceInfo getDevice(Context context) {
        DeviceInfo lDeviceInfo;
        synchronized (Device.class) {
            if (mDeviceInfo != null) {
                lDeviceInfo = mDeviceInfo;
            } else if (context != null) {
                lDeviceInfo = _initDeviceMetadata(context);
                if (lDeviceInfo != null) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                    if (telephonyManager == null) {
                        lDeviceInfo = null;
                    } else {
                        lDeviceInfo.setDeviceModel(Build.MODEL);
                        try {
                            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                            String versionName = packageInfo.versionName;
                            lDeviceInfo.setVersionCode("" + packageInfo.versionCode);
                            lDeviceInfo.setAppVersion(versionName);
                        } catch (Exception e) {
                            lDeviceInfo.setVersionCode("Unknown");
                            lDeviceInfo.setAppVersion("Unknown");
                        }
                        try {
                            lDeviceInfo.setBrand(Build.BRAND);
                            lDeviceInfo.setOsName("Android");
                            if (isYunOSSystem()) {
                                lDeviceInfo.setOsName("aliyunos");
                            }
                            lDeviceInfo.setOsVersion(Build.VERSION.RELEASE);
                            Configuration configuration = new Configuration();
                            Settings.System.getConfiguration(context.getContentResolver(), configuration);
                            if (configuration == null || configuration.locale == null) {
                                lDeviceInfo.setCountry("Unknown");
                                lDeviceInfo.setLanguage("Unknown");
                                lDeviceInfo.setTimezone(Constants.LogTransferLevel.HIGH);
                            } else {
                                lDeviceInfo.setCountry(configuration.locale.getCountry());
                                lDeviceInfo.setLanguage(configuration.locale.toString());
                                Calendar calendar = Calendar.getInstance(configuration.locale);
                                if (calendar != null) {
                                    TimeZone timezone = calendar.getTimeZone();
                                    if (timezone != null) {
                                        lDeviceInfo.setTimezone("" + (timezone.getRawOffset() / 3600000));
                                    } else {
                                        lDeviceInfo.setTimezone(Constants.LogTransferLevel.HIGH);
                                    }
                                } else {
                                    lDeviceInfo.setTimezone(Constants.LogTransferLevel.HIGH);
                                }
                            }
                            try {
                                DisplayMetrics dm = new DisplayMetrics();
                                ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
                                int width = dm.widthPixels;
                                int height = dm.heightPixels;
                                lDeviceInfo.setScreenWidth(width);
                                lDeviceInfo.setScreenHeight(height);
                                if (width > height) {
                                    int width2 = width ^ height;
                                    height ^= width2;
                                    width = width2 ^ height;
                                }
                                lDeviceInfo.setResolution(height + "*" + width);
                            } catch (Exception e2) {
                                lDeviceInfo.setResolution("Unknown");
                            }
                            lDeviceInfo.setAccess(NetworkUtil.getAccess(Variables.getInstance().getContext()));
                            lDeviceInfo.setAccessSubType(NetworkUtil.getAccess(Variables.getInstance().getContext()));
                            String networkName = telephonyManager.getNetworkOperatorName();
                            if (StringUtils.isEmpty(networkName)) {
                                networkName = "";
                            }
                            lDeviceInfo.setCarrier(networkName);
                            lDeviceInfo.setCpu(SystemUtils.getCpuInfo());
                            lDeviceInfo.setBinTime(String.valueOf(new File(PATH_AND_BIN_SETPROP).lastModified()).concat("_").concat(String.valueOf(new File(PATH_AND_BIN_MONKEY).lastModified())));
                            if (Build.VERSION.SDK_INT >= 9) {
                                lDeviceInfo.setSerialNo(getSerialNo());
                            }
                        } catch (Exception e3) {
                            lDeviceInfo = null;
                        }
                    }
                }
                mDeviceInfo = lDeviceInfo;
            } else {
                lDeviceInfo = null;
            }
        }
        return lDeviceInfo;
    }

    @TargetApi(9)
    private static String getSerialNo() {
        try {
            return Build.SERIAL;
        } catch (Throwable th) {
            return "";
        }
    }

    private static boolean isYunOSSystem() {
        if ((System.getProperty("java.vm.name") == null || !System.getProperty("java.vm.name").toLowerCase().contains("lemur")) && System.getProperty("ro.yunos.version") == null) {
            return false;
        }
        return true;
    }
}
