package com.loc;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: SPUtil */
public final class es {
    public static int a(Context context, String str, String str2, int i) {
        try {
            return context.getSharedPreferences(str, 0).getInt(str2, i);
        } catch (Throwable th) {
            en.a(th, "SpUtil", "getPrefsInt");
            return i;
        }
    }

    public static long a(Context context, String str, String str2, long j) {
        try {
            return context.getSharedPreferences(str, 0).getLong(str2, j);
        } catch (Throwable th) {
            en.a(th, "SpUtil", "getPrefsLong");
            return j;
        }
    }

    public static SharedPreferences.Editor a(Context context, String str) {
        return context.getSharedPreferences(str, 0).edit();
    }

    public static String a(Context context, String str, String str2, String str3) {
        try {
            return context.getSharedPreferences(str, 0).getString(str2, str3);
        } catch (Throwable th) {
            en.a(th, "SpUtil", "getPrefsInt");
            return str3;
        }
    }

    public static void a(SharedPreferences.Editor editor) {
        if (editor != null) {
            editor.apply();
        }
    }

    public static void a(SharedPreferences.Editor editor, String str, int i) {
        try {
            editor.putInt(str, i);
        } catch (Throwable th) {
            en.a(th, "SpUtil", "setPrefsInt");
        }
    }

    public static void a(SharedPreferences.Editor editor, String str, long j) {
        try {
            editor.putLong(str, j);
        } catch (Throwable th) {
            en.a(th, "SpUtil", "setPrefsLong");
        }
    }

    public static void a(SharedPreferences.Editor editor, String str, String str2) {
        try {
            editor.putString(str, str2);
        } catch (Throwable th) {
            en.a(th, "SpUtil", "setPrefsStr");
        }
    }

    public static void a(SharedPreferences.Editor editor, String str, boolean z) {
        try {
            editor.putBoolean(str, z);
        } catch (Throwable th) {
            en.a(th, "SpUtil", "updatePrefsBoolean");
        }
    }

    public static boolean a(Context context, String str, String str2, boolean z) {
        try {
            return context.getSharedPreferences(str, 0).getBoolean(str2, z);
        } catch (Throwable th) {
            en.a(th, "SpUtil", "getPrefsBoolean");
            return z;
        }
    }
}
