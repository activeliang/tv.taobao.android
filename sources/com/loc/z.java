package com.loc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.text.TextUtils;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/* compiled from: Log */
public final class z {
    public static final String a = "/a/";
    static final String b = "b";
    static final String c = "c";
    static final String d = "d";
    public static String e = "s";
    public static final String f = "g";
    public static final String g = "h";
    public static final String h = "e";
    public static final String i = "f";
    public static final String j = "j";
    public static final String k = "k";
    private static long l = 0;

    public static String a(Context context, String str) {
        return context.getSharedPreferences("AMSKLG_CFG", 0).getString(str, "");
    }

    public static void a(final Context context) {
        try {
            if (System.currentTimeMillis() - l >= 60000) {
                l = System.currentTimeMillis();
                ExecutorService d2 = ab.d();
                if (d2 != null && !d2.isShutdown()) {
                    d2.submit(new Runnable() {
                        public final void run() {
                            try {
                                ac.b(context);
                                ac.d(context);
                                ac.c(context);
                                bg.a(context);
                                be.a(context);
                            } catch (RejectedExecutionException e) {
                            } catch (Throwable th) {
                                ab.b(th, "Lg", "proL");
                            }
                        }
                    });
                }
            }
        } catch (Throwable th) {
            ab.b(th, "Lg", "proL");
        }
    }

    @TargetApi(9)
    public static void a(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("AMSKLG_CFG", 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    static boolean a(String[] strArr, String str) {
        if (strArr == null || str == null) {
            return false;
        }
        try {
            String[] split = str.split("\n");
            for (String trim : split) {
                String trim2 = trim.trim();
                if (!TextUtils.isEmpty(trim2) && trim2.startsWith("at ") && trim2.contains("uncaughtException")) {
                    return false;
                }
            }
            for (String trim3 : split) {
                if (b(strArr, trim3.trim())) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    static List<t> b(Context context) {
        List<t> list;
        Throwable th;
        List<t> list2 = null;
        try {
            synchronized (Looper.getMainLooper()) {
                try {
                    list = new ak(context, false).a();
                    try {
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        list2 = list;
                        th = th3;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            Throwable th5 = th4;
                            list = list2;
                            th = th5;
                        }
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }
        } catch (Throwable th7) {
            Throwable th8 = th7;
            list = null;
            th = th8;
            th.printStackTrace();
            return list;
        }
    }

    public static void b(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences("AMSKLG_CFG", 0).edit();
        edit.remove(str);
        edit.apply();
    }

    static boolean b(String[] strArr, String str) {
        if (strArr == null || str == null) {
            return false;
        }
        try {
            for (String str2 : strArr) {
                str = str.trim();
                if (str.startsWith("at ") && str.contains(str2 + ".") && str.endsWith(")") && !str.contains("uncaughtException")) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public static String c(Context context, String str) {
        return context.getFilesDir().getAbsolutePath() + a + str;
    }
}
