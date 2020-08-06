package com.yunos.tv.core.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private static final String RECORDS_TAOKE = "TvBuytaoke";
    private static final String RECORDS_TAOKE_BTOC = "TvBuytaokeBtoc";
    private static final String RECORDS_TAOKE_TIME = "taoketime";
    private static final String RECORDS_TAOKE_TIME_BTOC = "taoketimebtoc";
    private static final long RECORDS_TAOKE_TIME_DEFAULT = -1;
    private static final String RECORDS_UPDATEUSERID_FLAG = "updateUserIdFlag";
    private static final String RECORDS_UPDATEUSERID_FLAG_TIME = "updateUserTime";
    private static SharedPreferences sp;

    public static void saveTvBuyTaoKe(Context context, long time) {
        sp = context.getSharedPreferences(RECORDS_TAOKE, 0);
        sp.edit().putLong(RECORDS_TAOKE_TIME, time).commit();
    }

    public static long getTaoKeLogin(Context context) {
        sp = context.getSharedPreferences(RECORDS_TAOKE, 0);
        return sp.getLong(RECORDS_TAOKE_TIME, -1);
    }

    public static void saveTaoKeBtoc(Context context, long time) {
        sp = context.getSharedPreferences(RECORDS_TAOKE_BTOC, 0);
        sp.edit().putLong(RECORDS_TAOKE_TIME_BTOC, time).commit();
    }

    public static long getTaoKeBtoc(Context context) {
        sp = context.getSharedPreferences(RECORDS_TAOKE_BTOC, 0);
        return sp.getLong(RECORDS_TAOKE_TIME_BTOC, -1);
    }

    public static void saveUpdateUserIdFlag(Context context, long time) {
        sp = context.getSharedPreferences(RECORDS_UPDATEUSERID_FLAG, 0);
        sp.edit().putLong(RECORDS_UPDATEUSERID_FLAG_TIME, time).commit();
    }

    public static long getUpdateUserIdFlag(Context context) {
        sp = context.getSharedPreferences(RECORDS_UPDATEUSERID_FLAG, 0);
        return sp.getLong(RECORDS_UPDATEUSERID_FLAG_TIME, -1);
    }
}
