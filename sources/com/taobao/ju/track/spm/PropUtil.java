package com.taobao.ju.track.spm;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropUtil {
    @TargetApi(9)
    public static Map<String, String> toMap(Properties properties) {
        Map<String, String> result = new HashMap<>();
        if (properties != null && properties.size() > 0) {
            for (String name : properties.stringPropertyNames()) {
                result.put(name, properties.getProperty(name));
            }
        }
        return result;
    }

    @TargetApi(9)
    public static String toArray(Properties properties) {
        if (properties == null || properties.size() <= 0) {
            return null;
        }
        String[] result = new String[properties.size()];
        int index = 0;
        properties.toString();
        for (String key : properties.stringPropertyNames()) {
            result[index] = key + properties.getProperty(key);
            index++;
        }
        return null;
    }

    public static Properties buildProps(String[] types, String[] values) {
        Properties prop = new Properties();
        if (!(types == null || values == null)) {
            int maxLen = Math.max(types.length, values.length);
            for (int i = 0; i < maxLen; i++) {
                if (!TextUtils.isEmpty(types[i]) && !TextUtils.isEmpty(values[i])) {
                    prop.put(types[i], values[i]);
                }
            }
        }
        return prop;
    }

    @TargetApi(9)
    public static Properties loadConfig(Context context, InputStream input) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(input, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void saveConfig(Context context, String file, Properties properties) {
        try {
            properties.store(new FileOutputStream(file, false), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
