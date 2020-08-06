package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentStatus;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CascadeComponent extends Component {
    public CascadeComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
        JSONArray targets = this.fields.getJSONArray("targets");
        if (targets == null || targets.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public boolean isExpand() {
        return this.fields.getBooleanValue("expand");
    }

    public void setExpand(boolean expand) {
        this.fields.put("expand", (Object) Boolean.valueOf(expand));
    }

    public List<Component> getTargets() {
        Map<String, Component> index = this.engine.getContext().getIndex();
        JSONArray targets = this.fields.getJSONArray("targets");
        List<Component> a = new ArrayList<>(targets.size());
        Iterator<Object> it = targets.iterator();
        while (it.hasNext()) {
            Component component = index.get((String) it.next());
            if (component != null) {
                a.add(component);
            }
        }
        return a;
    }

    public void unfold() {
        setExpand(true);
        for (Component t : getTargets()) {
            t.setStatus(ComponentStatus.NORMAL);
        }
    }

    public void fold() {
        setExpand(false);
        for (Component t : getTargets()) {
            t.setStatus(ComponentStatus.HIDDEN);
        }
    }
}
