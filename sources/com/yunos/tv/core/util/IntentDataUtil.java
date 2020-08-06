package com.yunos.tv.core.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.zhiping.dev.android.logger.ZpLogger;

public class IntentDataUtil {
    private static String TAG = "IntentDataUtil";

    public static Integer getInteger(Intent intent, String name, int defaultValue) {
        String valueStr;
        if (intent == null) {
            return null;
        }
        Integer value = Integer.valueOf(defaultValue);
        if (intent.hasExtra(name)) {
            return Integer.valueOf(intent.getIntExtra(name, defaultValue));
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(name)) {
            return Integer.valueOf(bundle.getInt(name, defaultValue));
        }
        Uri uri = intent.getData();
        if (!(uri == null || (valueStr = uri.getQueryParameter(name)) == null || valueStr.length() <= 0)) {
            try {
                value = Integer.valueOf(Integer.parseInt(valueStr));
            } catch (Exception e) {
                ZpLogger.i(TAG, TAG + ".getInteger name=" + name + ", valueStr=" + valueStr + ", uri=" + uri);
            }
        }
        ZpLogger.i(TAG, TAG + ".getString uri = " + uri + ", name=" + name + ", value=" + value + ", defaultValue=" + defaultValue);
        return value;
    }

    public static Long getLong(Intent intent, String name, long defaultValue) {
        String valueStr;
        if (intent == null) {
            return null;
        }
        ZpLogger.i(TAG, TAG + ".getLong name=" + name + ", defaultValue=" + defaultValue + ", intent=" + intent);
        Long value = Long.valueOf(defaultValue);
        if (intent.hasExtra(name)) {
            return Long.valueOf(intent.getLongExtra(name, defaultValue));
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(name)) {
            return Long.valueOf(bundle.getLong(name, defaultValue));
        }
        Uri uri = intent.getData();
        if (!(uri == null || (valueStr = uri.getQueryParameter(name)) == null || valueStr.length() <= 0)) {
            try {
                value = Long.valueOf(Long.parseLong(valueStr));
            } catch (Exception e) {
                ZpLogger.i(TAG, TAG + ".getLong name=" + name + ", valueStr=" + valueStr + ", uri=" + uri);
            }
        }
        ZpLogger.i(TAG, TAG + ".getLong uri = " + uri + ", name=" + name + ", value=" + value + ", defaultValue=" + defaultValue);
        return value;
    }

    public static String getString(Intent intent, String name, String defaultValue) {
        String value = getStringFromBundle(intent, name, defaultValue);
        if (TextUtils.isEmpty(value)) {
            return getStringFromUri(intent, name, defaultValue);
        }
        return value;
    }

    public static String getStringFromBundle(Intent intent, String name, String defaultValue) {
        if (intent == null) {
            return null;
        }
        String value = defaultValue;
        if (intent.hasExtra(name)) {
            return intent.getStringExtra(name);
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null || !bundle.containsKey(name)) {
            return value;
        }
        ZpLogger.i(TAG, TAG + ".getString 1==> name=" + name + ", value=" + value + ", defaultValue=" + defaultValue + ", bundle=" + bundle);
        return bundle.getString(name, defaultValue);
    }

    public static Object getObjectFromBundle(Intent intent, String name, Object defaultValue) {
        if (intent == null) {
            return null;
        }
        Object value = defaultValue;
        Bundle bundle = intent.getExtras();
        if (bundle == null || !bundle.containsKey(name)) {
            return value;
        }
        ZpLogger.i(TAG, TAG + ".getObject 1==> name=" + name + ", value=" + value + ", defaultValue=" + defaultValue + ", bundle=" + bundle);
        return bundle.getSerializable(name);
    }

    public static String getStringFromUri(Intent intent, String name, String defaultValue) {
        if (intent == null) {
            return null;
        }
        String value = defaultValue;
        Uri uri = intent.getData();
        if (uri != null) {
            try {
                value = uri.getQueryParameter(name);
                if (TextUtils.isEmpty(value)) {
                    value = defaultValue;
                }
            } catch (Exception e) {
                value = defaultValue;
            }
        }
        ZpLogger.i(TAG, TAG + ".getString 2==> name=" + name + ", value=" + value + ", defaultValue=" + defaultValue + ", uri = " + uri);
        return value;
    }

    public static Boolean getBoolean(Intent intent, String name, boolean defaultValue) {
        if (intent == null) {
            return null;
        }
        Boolean value = Boolean.valueOf(defaultValue);
        if (intent.hasExtra(name)) {
            return Boolean.valueOf(intent.getBooleanExtra(name, defaultValue));
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(name)) {
            return Boolean.valueOf(bundle.getBoolean(name, defaultValue));
        }
        Uri uri = intent.getData();
        if (uri != null) {
            value = Boolean.valueOf(uri.getBooleanQueryParameter(name, defaultValue));
        }
        ZpLogger.i(TAG, TAG + ".getString uri = " + uri + ", name=" + name + ", value=" + value + ", defaultValue=" + defaultValue);
        return value;
    }

    public static String getHost(Intent intent) {
        if (intent == null) {
            return null;
        }
        Uri uri = intent.getData();
        ZpLogger.i(TAG, TAG + ".getHost uri = " + uri);
        if (uri != null) {
            return uri.getHost();
        }
        return null;
    }
}
