package com.alibaba.appmonitor.pool;

import com.alibaba.fastjson.JSONObject;

public class ReuseJSONObject extends JSONObject implements Reusable {
    private static final long serialVersionUID = 1465414806753619992L;

    public void clean() {
        for (Object obj : values()) {
            if (obj instanceof Reusable) {
                BalancedPool.getInstance().offer((Reusable) obj);
            }
        }
        super.clear();
    }

    public void fill(Object... params) {
    }
}
