package com.alibaba.appmonitor.event;

import com.alibaba.fastjson.JSONObject;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

public class CountEvent extends Event {
    public int count;
    public double value;

    public synchronized void addValue(double value2, Long commitTime) {
        this.value += value2;
        this.count++;
        super.commit(commitTime);
    }

    public synchronized JSONObject dumpToJSONObject() {
        JSONObject jobject;
        jobject = super.dumpToJSONObject();
        jobject.put("count", (Object) Integer.valueOf(this.count));
        jobject.put("value", (Object) Double.valueOf(this.value));
        return jobject;
    }

    public synchronized void fill(Object... params) {
        super.fill(params);
        this.value = ClientTraceData.b.f47a;
        this.count = 0;
    }
}
