package com.alibaba.appmonitor.pool;

import com.alibaba.fastjson.JSONArray;
import java.util.Iterator;

public class ReuseJSONArray extends JSONArray implements Reusable {
    private static final long serialVersionUID = -4243576223670082606L;

    public void clean() {
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            Object obj = i$.next();
            if (obj instanceof Reusable) {
                BalancedPool.getInstance().offer((Reusable) obj);
            }
        }
        super.clear();
    }

    public void fill(Object... params) {
    }
}
