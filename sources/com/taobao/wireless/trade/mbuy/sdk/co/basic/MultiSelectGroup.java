package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiSelectGroup {
    private JSONObject data;
    private List<SelectOption> options;

    public MultiSelectGroup(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
        loadOptions(data2.getJSONArray("options"));
    }

    private void loadOptions(JSONArray optionJSONArray) {
        if (optionJSONArray == null) {
            throw new IllegalStateException();
        }
        this.options = new ArrayList(optionJSONArray.size());
        Iterator<Object> it = optionJSONArray.iterator();
        while (it.hasNext()) {
            this.options.add(new SelectOption((JSONObject) it.next()));
        }
    }

    public String getTitle() {
        return this.data.getString("title");
    }

    public boolean isRequired() {
        return this.data.getBooleanValue("required");
    }

    public List<SelectOption> getOptions() {
        return this.options;
    }

    public String toString() {
        return "[title=" + getTitle() + ", required=" + isRequired() + ", options=" + this.options + "]";
    }
}
