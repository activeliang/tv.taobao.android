package com.taobao.alimama.cpm;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CpmAdvertiseBundle implements Serializable, Cloneable {
    @JSONField(name = "ads")
    public Map<String, CpmAdvertise> advertises;
    @JSONField(name = "cache_time_in_millis")
    public long cacheTimeInMillis;
    @JSONField(name = "time_stamp")
    public long timeStamp;
    @JSONField(name = "user_nick")
    public String userNick;

    public CpmAdvertiseBundle clone() {
        try {
            CpmAdvertiseBundle cpmAdvertiseBundle = (CpmAdvertiseBundle) super.clone();
            cpmAdvertiseBundle.advertises = new HashMap();
            for (Map.Entry next : this.advertises.entrySet()) {
                cpmAdvertiseBundle.advertises.put(next.getKey(), ((CpmAdvertise) next.getValue()).clone());
            }
            return cpmAdvertiseBundle;
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }
}
