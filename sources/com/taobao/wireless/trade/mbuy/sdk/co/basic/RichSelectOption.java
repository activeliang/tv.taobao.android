package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RichSelectOption {
    private JSONObject data;
    private List<String> tips;

    public RichSelectOption(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getOptionId() {
        return this.data.getString("optionId");
    }

    public String getName() {
        return this.data.getString("name");
    }

    public String getValue() {
        return this.data.getString("value");
    }

    public boolean isHighLight() {
        Boolean highLight = this.data.getBoolean("highLight");
        if (highLight != null) {
            return highLight.booleanValue();
        }
        return false;
    }

    public boolean isDisabled() {
        Boolean disabled = this.data.getBoolean("disabled");
        if (disabled != null) {
            return disabled.booleanValue();
        }
        return false;
    }

    public List<String> getTips() {
        if (this.tips != null) {
            return this.tips;
        }
        this.tips = new ArrayList();
        JSONArray array = this.data.getJSONArray("tips");
        if (array != null && array.size() > 0) {
            Iterator<Object> it = array.iterator();
            while (it.hasNext()) {
                this.tips.add(String.valueOf(it.next()));
            }
        }
        return this.tips;
    }
}
