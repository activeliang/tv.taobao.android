package com.alibaba.appmonitor.event;

import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.Reusable;
import com.alibaba.appmonitor.pool.ReuseJSONObject;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.alibaba.fastjson.JSONObject;

public abstract class Event implements Reusable {
    protected static final String EXTRA_KEY_BASE = "arg";
    public long begin = Long.MAX_VALUE;
    public long end = 0;
    public int eventId;
    public String extraArg;
    public String module;
    public String monitorPoint;

    public JSONObject dumpToJSONObject() {
        JSONObject jobject = (JSONObject) BalancedPool.getInstance().poll(ReuseJSONObject.class, new Object[0]);
        jobject.put("page", (Object) this.module);
        jobject.put(SampleConfigConstant.MONITORPOINT, (Object) this.monitorPoint);
        jobject.put("begin", (Object) Long.valueOf(this.begin));
        jobject.put("end", (Object) Long.valueOf(this.end));
        if (this.extraArg != null) {
            jobject.put(EXTRA_KEY_BASE, (Object) this.extraArg);
        }
        return jobject;
    }

    public void clean() {
        this.eventId = 0;
        this.module = null;
        this.monitorPoint = null;
        this.extraArg = null;
        this.begin = Long.MAX_VALUE;
        this.end = 0;
    }

    public void fill(Object... params) {
        this.eventId = params[0].intValue();
        this.module = params[1];
        this.monitorPoint = params[2];
        if (params.length > 3 && params[3] != null) {
            this.extraArg = params[3];
        }
    }

    public void commit(Long commitTime) {
        if (commitTime == null) {
            commitTime = Long.valueOf(System.currentTimeMillis() / 1000);
        }
        if (this.begin > commitTime.longValue()) {
            this.begin = commitTime.longValue();
        }
        if (this.end < commitTime.longValue()) {
            this.end = commitTime.longValue();
        }
    }
}
