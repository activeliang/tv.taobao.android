package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.crashreporter.Options;
import java.util.Map;

public class Propertys extends Options<Property> {
    Context mContext;

    public Propertys() {
        super(true);
    }

    public static class Property extends Options.Option {
        Property(String name, String value, boolean readOnly) {
            super(name, value, readOnly);
        }

        public Property(String name, String value) {
            super(name, value);
        }
    }

    public String getValue(String name) {
        return (String) super.getValue(name);
    }

    public void copyTo(Map<String, String> data) {
        for (String name : this.mData.keySet()) {
            Property property = (Property) this.mData.get(name);
            if (property.value instanceof String) {
                data.put(name, (String) property.value);
            }
        }
    }
}
