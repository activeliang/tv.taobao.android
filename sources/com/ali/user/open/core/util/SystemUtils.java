package com.ali.user.open.core.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import com.ali.user.open.core.context.KernelContext;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import mtopsdk.common.util.SymbolExpUtil;

public class SystemUtils {
    public static final String GLOBAL_SHARED_PREFERENCES = "aliuserSP";
    public static final String IMEI = "imei";
    public static final String IMSI = "imsi";

    public static String getApkPublicKeyDigest() {
        try {
            PackageInfo packageInfo = KernelContext.getApplicationContext().getPackageManager().getPackageInfo(KernelContext.getApplicationContext().getPackageName(), 64);
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getPublicKey().toString().getBytes());
            return CommonUtils.getHashString(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }

    public static Application getSystemApp() {
        try {
            Class<?> activitythread = Class.forName("android.app.ActivityThread");
            Method m_currentActivityThread = activitythread.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field f_mInitialApplication = activitythread.getDeclaredField("mInitialApplication");
            f_mInitialApplication.setAccessible(true);
            return (Application) f_mInitialApplication.get(m_currentActivityThread.invoke((Object) null, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String generateImei() {
        StringBuffer imei = new StringBuffer();
        long time = System.currentTimeMillis();
        String currentTime = Long.toString(time);
        imei.append(currentTime.substring(currentTime.length() - 5));
        StringBuffer model = new StringBuffer();
        model.append(Build.MODEL.replaceAll(" ", ""));
        while (model.length() < 6) {
            model.append('0');
        }
        imei.append(model.substring(0, 6));
        Random random = new Random(time);
        long tmp = 0;
        while (tmp < PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
            tmp = random.nextLong();
        }
        imei.append(Long.toHexString(tmp).substring(0, 4));
        return imei.toString();
    }

    public static String getImei(Context context) {
        SharedPreferences sp = context.getSharedPreferences(GLOBAL_SHARED_PREFERENCES, 0);
        String imei = sp.getString("imei", (String) null);
        if (imei == null || imei.length() == 0) {
            try {
                imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            } catch (Throwable th) {
            }
            if (imei == null || imei.length() == 0) {
                imei = generateImei();
            }
            imei = imei.replaceAll(" ", "").trim();
            while (imei.length() < 15) {
                imei = "0" + imei;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("imei", imei);
            editor.commit();
        }
        return imei.trim();
    }

    public static String getImsi(Context context) {
        SharedPreferences sp = context.getSharedPreferences(GLOBAL_SHARED_PREFERENCES, 0);
        String imsi = sp.getString("imsi", (String) null);
        if (imsi == null || imsi.length() == 0) {
            try {
                imsi = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
            } catch (Throwable th) {
            }
            if (imsi == null || imsi.length() == 0) {
                imsi = generateImei();
            }
            imsi = imsi.replaceAll(" ", "").trim();
            while (imsi.length() < 15) {
                imsi = "0" + imsi;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("imsi", imsi);
            editor.commit();
        }
        return imsi;
    }

    public static String getDeviceMac() {
        try {
            Iterator<NetworkInterface> it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                NetworkInterface ni = it.next();
                Iterator<InetAddress> it2 = Collections.list(ni.getInetAddresses()).iterator();
                while (true) {
                    if (it2.hasNext()) {
                        InetAddress address = it2.next();
                        if (!address.isLoopbackAddress() && (address instanceof Inet4Address)) {
                            return toHexString(ni.getHardwareAddress(), SymbolExpUtil.SYMBOL_COLON, false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toHexString(byte[] aBytes, String aSeparator, boolean aUpperCase) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < aBytes.length; i++) {
            String str = Integer.toHexString(aBytes[i] & OnReminderListener.RET_FULL);
            if (aUpperCase) {
                str = str.toUpperCase();
            }
            if (str.length() == 1) {
                hexString.append("0");
            }
            hexString.append(str);
            if (i != aBytes.length - 1) {
                hexString.append(aSeparator);
            }
        }
        return hexString.toString();
    }

    public static String getSSID(Context aContext) {
        try {
            WifiInfo wifiInfo = ((WifiManager) aContext.getSystemService("wifi")).getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getSSID();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBSSID(Context aContext) {
        try {
            WifiInfo wifiInfo = ((WifiManager) aContext.getSystemService("wifi")).getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getBSSID();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toHexString(byte[] aBytes, boolean aUpperCase) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : aBytes) {
            String str = Integer.toHexString(b & OnReminderListener.RET_FULL);
            if (aUpperCase) {
                str = str.toUpperCase();
            }
            if (str.length() == 1) {
                hexString.append("0");
            }
            hexString.append(str);
        }
        return hexString.toString();
    }

    public static double[] getLocation(Context aContext) {
        if (aContext == null) {
            return null;
        }
        try {
            LocationManager locationManager = (LocationManager) aContext.getSystemService("location");
            Criteria criteria = new Criteria();
            criteria.setCostAllowed(false);
            criteria.setAccuracy(2);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            if (bestProvider != null) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                if (lastKnownLocation == null) {
                    return null;
                }
                return new double[]{lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()};
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }
}
