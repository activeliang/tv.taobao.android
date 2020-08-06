package com.alibaba.analytics.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class PhoneInfoUtils {
    private static final String STORAGE_KEY = "UTCommon";
    private static final Random s_random = new Random();

    public static final String getUniqueID() {
        int t1 = (int) (System.currentTimeMillis() / 1000);
        int t3 = s_random.nextInt();
        int t4 = s_random.nextInt();
        byte[] b1 = IntUtils.getBytes(t1);
        byte[] b2 = IntUtils.getBytes((int) System.nanoTime());
        byte[] b3 = IntUtils.getBytes(t3);
        byte[] b4 = IntUtils.getBytes(t4);
        byte[] bUniqueID = new byte[16];
        System.arraycopy(b1, 0, bUniqueID, 0, 4);
        System.arraycopy(b2, 0, bUniqueID, 4, 4);
        System.arraycopy(b3, 0, bUniqueID, 8, 4);
        System.arraycopy(b4, 0, bUniqueID, 12, 4);
        return Base64.encodeToString(bUniqueID, 2);
    }

    public static String getImei(Context context) {
        String imei = null;
        if (context != null) {
            try {
                String lIE = context.getSharedPreferences(STORAGE_KEY, 0).getString("_ie", "");
                if (!StringUtils.isEmpty(lIE)) {
                    String lIMEI = new String(Base64.decode(lIE.getBytes(), 2), "UTF-8");
                    if (!StringUtils.isEmpty(lIMEI)) {
                        return lIMEI;
                    }
                }
            } catch (Exception e) {
            }
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
                if (tm != null) {
                    imei = tm.getDeviceId();
                }
            } catch (Exception e2) {
            }
        }
        if (StringUtils.isEmpty(imei)) {
            imei = getUniqueID();
        }
        if (context != null) {
            try {
                SharedPreferences.Editor lE = context.getSharedPreferences(STORAGE_KEY, 0).edit();
                lE.putString("_ie", new String(Base64.encode(imei.getBytes("UTF-8"), 2)));
                lE.commit();
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
            }
        }
        return imei;
    }

    public static String getImsi(Context context) {
        String imsi = null;
        if (context != null) {
            try {
                String lIS = context.getSharedPreferences(STORAGE_KEY, 0).getString("_is", "");
                if (!StringUtils.isEmpty(lIS)) {
                    String lIMSI = new String(Base64.decode(lIS.getBytes(), 2), "UTF-8");
                    if (!StringUtils.isEmpty(lIMSI)) {
                        return lIMSI;
                    }
                }
            } catch (Exception e) {
            }
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
                if (tm != null) {
                    imsi = tm.getSubscriberId();
                }
            } catch (Exception e2) {
            }
        }
        if (StringUtils.isEmpty(imsi)) {
            imsi = getUniqueID();
        }
        if (context != null) {
            try {
                SharedPreferences.Editor lE = context.getSharedPreferences(STORAGE_KEY, 0).edit();
                lE.putString("_is", new String(Base64.encode(imsi.getBytes("UTF-8"), 2)));
                lE.commit();
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
            }
        }
        return imsi;
    }

    public static String getImeiBySystem(Context context) {
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            if (tm != null) {
                return tm.getDeviceId();
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    public static String getImsiBySystem(Context context) {
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            if (tm != null) {
                return tm.getSubscriberId();
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }
}
