package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentStatus;

public class Submit {
    private JSONObject data;

    public Submit(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getTitle() {
        return this.data.getString("title");
    }

    public void setTitle(String title) {
        this.data.put("title", (Object) title);
    }

    public ComponentStatus getStatus() {
        return ComponentStatus.getComponentStatusByDesc(this.data.getString("status"));
    }

    public void setStatus(ComponentStatus status) {
        if (ComponentStatus.DISABLE == status) {
            this.data.put("status", (Object) "disable");
        } else if (ComponentStatus.HIDDE == status) {
            this.data.put("status", (Object) "hidden");
        } else {
            this.data.put("status", (Object) "normal");
        }
    }

    public String toString() {
        return "Submit [title=" + getTitle() + ",status=" + getStatus() + "]";
    }
}
