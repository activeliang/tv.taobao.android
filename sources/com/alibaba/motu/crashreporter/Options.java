package com.alibaba.motu.crashreporter;

import com.alibaba.motu.crashreporter.Options.Option;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Options<T extends Option> {
    Map<String, T> mData;

    public static class Option {
        String name;
        boolean readOnly;
        Object value;

        Option(String name2, Object value2, boolean readOnly2) {
            this.name = name2;
            this.value = value2;
            this.readOnly = readOnly2;
        }

        public Option(String name2, Object value2) {
            this(name2, value2, false);
        }
    }

    Options() {
        this(false);
    }

    Options(boolean isThreadSafe) {
        if (isThreadSafe) {
            this.mData = new ConcurrentHashMap();
        } else {
            this.mData = new HashMap();
        }
    }

    public void add(T option) {
        if (option != null && StringUtils.isNotBlank(option.name) && option.value != null) {
            T originalOption = (Option) this.mData.get(option.name);
            if (originalOption == null || (originalOption != null && !originalOption.readOnly)) {
                this.mData.put(option.name, option);
            }
        }
    }

    public void remove(T option) {
        if (option != null && StringUtils.isBlank(option.name)) {
            this.mData.remove(option.name);
        }
    }

    public Object getValue(String name) {
        T originalOption = (Option) this.mData.get(name);
        if (originalOption != null) {
            return originalOption.value;
        }
        return null;
    }

    public Object getValue(String name, Object defaultVal) {
        Object value = getValue(name);
        return value != null ? value : defaultVal;
    }

    public String getString(String name, String defaultVal) {
        try {
            Object val = getValue(name);
            if (val instanceof String) {
                return (String) val;
            }
        } catch (Exception e) {
        }
        return defaultVal;
    }

    public int getInt(String name, int defaultVal) {
        try {
            Object val = getValue(name);
            if (val instanceof Integer) {
                return ((Integer) val).intValue();
            }
            if (val instanceof String) {
                return Integer.parseInt((String) val);
            }
            return defaultVal;
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public boolean getBoolean(String name, boolean defaultVal) {
        try {
            Object val = getValue(name);
            if (val instanceof Boolean) {
                return ((Boolean) val).booleanValue();
            }
            return defaultVal;
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
