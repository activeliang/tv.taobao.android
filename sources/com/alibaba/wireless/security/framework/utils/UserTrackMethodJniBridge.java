package com.alibaba.wireless.security.framework.utils;

import android.content.Context;
import android.os.Process;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.alimama.global.Constants;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public class UserTrackMethodJniBridge {
    private static Context a;
    private static String b = null;
    private static int c = 0;
    private static int d = 0;
    private static int e = 0;
    private static int f = 0;
    private static Class g = null;
    private static Class h = null;
    private static Class i = null;
    private static Constructor j = null;
    private static Method k = null;
    private static Method l = null;
    private static Method m = null;
    private static Method n = null;
    private static final char[] o = "0123456789abcdef".toCharArray();

    private static synchronized String a() {
        String substring;
        synchronized (UserTrackMethodJniBridge.class) {
            if (b == null || b.length() == 0) {
                b = b();
            }
            substring = b.substring(0, b.length() / 8);
        }
        return substring;
    }

    private static String a(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static int addUtRecord(String str, int i2, int i3, String str2, long j2, String str3, String str4, String str5, String str6, String str7) {
        Map map;
        Object invoke;
        Object invoke2;
        if (!(utAvaiable() == 0 || str == null || str.length() == 0)) {
            if (str2 == null) {
                str2 = "";
            }
            try {
                String valueOf = String.valueOf(i2 % 100);
                HashMap hashMap = new HashMap();
                hashMap.put("plugin", String.valueOf(i3));
                hashMap.put("pid", String.valueOf(Process.myPid()));
                hashMap.put(BaseConfig.INTENT_KEY_TID, String.valueOf(Thread.currentThread().getId()));
                hashMap.put("time", String.valueOf(j2));
                if (d == 0) {
                    c = f.c(a) ? 1 : 0;
                    d = 1;
                }
                hashMap.put("ui", String.valueOf(c));
                hashMap.put("sid", a());
                hashMap.put("uuid", b());
                hashMap.put("msg", a(str3));
                hashMap.put("rsv1", a(str4));
                hashMap.put("rsv2", a(str5));
                hashMap.put("rsv3", a(str6));
                hashMap.put("rsv4", a(str7));
                Object newInstance = j.newInstance(new Object[]{"Page_SecurityGuardSDK", Integer.valueOf(Constants.UtEventId.CUSTOM), str, str2, valueOf, hashMap});
                if (!(newInstance == null || (map = (Map) k.invoke(newInstance, new Object[0])) == null || map.size() == 0 || (invoke = l.invoke(h, new Object[0])) == null || (invoke2 = m.invoke(invoke, new Object[0])) == null)) {
                    n.invoke(invoke2, new Object[]{map});
                }
            } catch (Exception e2) {
            }
        }
        return 0;
    }

    private static String b() {
        try {
            String uuid = UUID.randomUUID().toString();
            return bytesToHex(MessageDigest.getInstance(MessageDigestAlgorithms.SHA_1).digest((uuid + String.valueOf(System.nanoTime())).getBytes("UTF-8")));
        } catch (Exception e2) {
            return "";
        }
    }

    public static String bytesToHex(byte[] bArr) {
        char[] cArr = new char[(bArr.length * 2)];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            byte b2 = bArr[i2] & OnReminderListener.RET_FULL;
            cArr[i2 * 2] = o[b2 >>> 4];
            cArr[(i2 * 2) + 1] = o[b2 & 15];
        }
        return new String(cArr);
    }

    public static String getStackTrace(int i2, int i3) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace != null) {
            if (stackTrace.length <= 0) {
                return "";
            }
            if (i2 > 0) {
                if (i3 <= 0) {
                    return "";
                }
                StringBuilder sb = new StringBuilder();
                int i4 = 0;
                for (int i5 = 0; i5 < stackTrace.length && i4 < i3 && sb.length() < i2; i5++) {
                    if (i5 > 1) {
                        i4++;
                        StackTraceElement stackTraceElement = stackTrace[i5];
                        sb.append(stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
                        if (i5 < stackTrace.length - 1) {
                            sb.append(Constant.INTENT_JSON_MARK);
                        }
                    }
                }
                return sb.toString();
            }
        }
        return "";
    }

    public static void init(Context context) {
        if (context != null) {
            a = context;
        }
    }

    public static int utAvaiable() {
        if (f == 0) {
            synchronized (UserTrackMethodJniBridge.class) {
                if (f == 0) {
                    try {
                        g = Class.forName("com.ut.mini.internal.UTOriginalCustomHitBuilder");
                        h = Class.forName("com.ut.mini.UTAnalytics");
                        i = Class.forName("com.ut.mini.UTTracker");
                        try {
                            j = g.getConstructor(new Class[]{String.class, Integer.TYPE, String.class, String.class, String.class, Map.class});
                            k = g.getMethod("build", new Class[0]);
                            l = h.getMethod("getInstance", new Class[0]);
                            m = h.getMethod("getDefaultTracker", new Class[0]);
                            n = i.getMethod("send", new Class[]{Map.class});
                            e = 1;
                        } catch (NoSuchMethodException e2) {
                        }
                    } catch (ClassNotFoundException e3) {
                    }
                    f = 1;
                }
            }
        }
        return e;
    }
}
