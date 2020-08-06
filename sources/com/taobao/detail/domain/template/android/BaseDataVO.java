package com.taobao.detail.domain.template.android;

import java.io.Serializable;
import java.util.HashMap;

public class BaseDataVO implements Serializable {
    public String ID;
    public String condition;
    public String key;
    public HashMap<String, String> params;
    public String type;

    public String getParams(String key2) {
        if (key2 == null || key2.length() <= 0 || this.params == null || !this.params.containsKey(key2)) {
            return null;
        }
        return this.params.get(key2);
    }
}
