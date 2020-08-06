package com.yunos.tvtaobao.payment.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.analytics.utils.StringUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class TvTaoSharedPerference {
    public static final String LOGIN23 = "login23";
    public static final String NICK = "nick";
    public static final String SAFE_CODE = "safecode";
    public static final String SHOP_CART_IDS = "shopcartids";
    private static final String TAG = "TvTaoSharedPerference";
    private static String fileName = TAG;

    public static void saveSp(Context context, String key, Object value) {
        save(context, key, value, context.getSharedPreferences(fileName, 0));
    }

    public static void saveSp(Context context, String key, Object value, String fileName2) {
        save(context, key, value, context.getSharedPreferences(fileName2, 0));
    }

    private static void save(Context context, String key, Object value, SharedPreferences sp) {
        ZpLogger.v(TAG, new StringBuilder().append(".save -->switch value= ").append(value).toString() == null ? "" : value.toString());
        String type = value.getClass().getSimpleName();
        SharedPreferences.Editor editor = sp.edit();
        if ("Integer".equals(type)) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if ("String".equals(type)) {
            editor.putString(key, (String) value);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if ("Long".equals(type)) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.commit();
    }

    public static Object getSp(Context context, String key, Object defaultValue) {
        return get(context, key, defaultValue, context.getSharedPreferences(fileName, 0));
    }

    public static Object getSp(Context context, String key, Object defaultValue, String fileName2) {
        return get(context, key, defaultValue, context.getSharedPreferences(fileName2, 0));
    }

    private static Object get(Context context, String key, Object defaultValue, SharedPreferences sp) {
        ZpLogger.v(TAG, new StringBuilder().append(".get -->switch defaultValue= ").append(defaultValue).toString() == null ? "" : defaultValue.toString());
        String type = defaultValue.getClass().getSimpleName();
        if (!StringUtils.isBlank(type)) {
            if ("Integer".equals(type)) {
                return Integer.valueOf(sp.getInt(key, ((Integer) defaultValue).intValue()));
            }
            if ("Boolean".equals(type)) {
                return Boolean.valueOf(sp.getBoolean(key, ((Boolean) defaultValue).booleanValue()));
            }
            if ("String".equals(type)) {
                return sp.getString(key, (String) defaultValue);
            }
            if ("Float".equals(type)) {
                return Float.valueOf(sp.getFloat(key, ((Float) defaultValue).floatValue()));
            }
            if ("Long".equals(type)) {
                return Long.valueOf(sp.getLong(key, ((Long) defaultValue).longValue()));
            }
        }
        return null;
    }

    public static <K extends Serializable, V extends Serializable> void putMap(Context context, String key, Map<K, V> map, String fileName2) {
        try {
            put(context, key, map, fileName2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <K extends Serializable, V extends Serializable> Map<K, V> getMap(Context context, String key, String fileName2) {
        try {
            return (Map) get(context, key, fileName2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void put(Context context, String key, Object obj, String fileName2) throws IOException {
        if (obj != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            String objectStr = new String(Base64.encode(baos.toByteArray(), 0));
            baos.close();
            oos.close();
            putString(context, key, objectStr, fileName2);
        }
    }

    public static void putString(Context context, String key, String value, String fileName2) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName2, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static Object get(Context context, String key, String fileName2) throws IOException, ClassNotFoundException {
        String wordBase64 = getString(context, key, fileName2);
        if (TextUtils.isEmpty(wordBase64)) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(wordBase64.getBytes(), 0));
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object readObject = ois.readObject();
        bais.close();
        ois.close();
        return readObject;
    }

    public static String getString(Context context, String key, String fileName2) {
        return context.getSharedPreferences(fileName2, 0).getString(key, "");
    }

    public static void clear(Context context, String fileName2) {
        context.getSharedPreferences(fileName2, 0).edit().clear().commit();
    }
}
