package com.alibaba.appmonitor.sample;

import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.Ingore;
import com.alibaba.analytics.utils.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AMConifg extends Entity implements Cloneable {
    @Column("module")
    protected String module;
    @Column("mp")
    protected String monitorPoint;
    @Column("offline")
    protected String offline;
    @Ingore
    private HashMap<String, AMConifg> relationMap;
    @Column("cp")
    private int sampling;

    public boolean isSampled(int samplingSeed, String module2, String monitorPoint2, Map<String, String> map) {
        ArrayList<String> keys = new ArrayList<>(2);
        keys.add(module2);
        keys.add(monitorPoint2);
        return sampling(samplingSeed, keys);
    }

    private boolean sampling(int samplingSeed, ArrayList<String> keys) {
        if (keys == null || keys.size() == 0) {
            return checkSelfSampling(samplingSeed);
        }
        String nextkey = keys.remove(0);
        if (isContains(nextkey)) {
            return this.relationMap.get(nextkey).sampling(samplingSeed, keys);
        }
        return checkSelfSampling(samplingSeed);
    }

    /* access modifiers changed from: protected */
    public boolean checkSelfSampling(int samplingSeed) {
        Logger.d(SampleConfigConstant.SAMPLING, "module", this.module, SampleConfigConstant.MONITORPOINT, this.monitorPoint, "samplingSeed", Integer.valueOf(samplingSeed), SampleConfigConstant.SAMPLING, Integer.valueOf(this.sampling));
        if (samplingSeed < this.sampling) {
            return true;
        }
        return false;
    }

    public synchronized void add(String key, AMConifg config) {
        if (this.relationMap == null) {
            this.relationMap = new HashMap<>();
        }
        if (isContains(key)) {
            AMConifg temp = this.relationMap.get(key);
            if (!(temp == null || temp.relationMap == null || config.relationMap == null)) {
                config.relationMap.putAll(temp.relationMap);
            }
            Logger.w("config object order errror", "config:", config + "");
        }
        this.relationMap.put(key, config);
    }

    /* access modifiers changed from: protected */
    public synchronized boolean isContains(String key) {
        boolean containsKey;
        if (this.relationMap == null) {
            containsKey = false;
        } else {
            containsKey = this.relationMap.containsKey(key);
        }
        return containsKey;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.alibaba.appmonitor.sample.AMConifg} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.alibaba.appmonitor.sample.AMConifg getOrBulidNext(java.lang.String r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            com.alibaba.appmonitor.sample.AMConifg r1 = r4.getNext(r5)     // Catch:{ all -> 0x001d }
            if (r1 != 0) goto L_0x0011
            java.lang.Object r3 = r4.clone()     // Catch:{ CloneNotSupportedException -> 0x0018 }
            r0 = r3
            com.alibaba.appmonitor.sample.AMConifg r0 = (com.alibaba.appmonitor.sample.AMConifg) r0     // Catch:{ CloneNotSupportedException -> 0x0018 }
            r1 = r0
            r1.module = r5     // Catch:{ CloneNotSupportedException -> 0x0018 }
        L_0x0011:
            java.util.HashMap<java.lang.String, com.alibaba.appmonitor.sample.AMConifg> r3 = r4.relationMap     // Catch:{ all -> 0x001d }
            r3.put(r5, r1)     // Catch:{ all -> 0x001d }
            monitor-exit(r4)
            return r1
        L_0x0018:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ all -> 0x001d }
            goto L_0x0011
        L_0x001d:
            r3 = move-exception
            monitor-exit(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.sample.AMConifg.getOrBulidNext(java.lang.String):com.alibaba.appmonitor.sample.AMConifg");
    }

    public synchronized AMConifg getNext(String key) {
        if (this.relationMap == null) {
            this.relationMap = new HashMap<>();
        }
        return this.relationMap.get(key);
    }

    public String getModule() {
        return this.module;
    }

    public void setSampling(int sampling2) {
        this.sampling = sampling2;
    }

    public boolean isOffline(String module2, String monitorPoint2) {
        ArrayList<String> keys = new ArrayList<>(2);
        keys.add(module2);
        keys.add(monitorPoint2);
        return isOffline(keys);
    }

    private boolean isOffline(ArrayList<String> keys) {
        if (keys == null || keys.size() == 0) {
            return checkSelfOffline();
        }
        String nextkey = keys.remove(0);
        if (isContains(nextkey)) {
            return this.relationMap.get(nextkey).isOffline(keys);
        }
        return checkSelfOffline();
    }

    private boolean checkSelfOffline() {
        return "1".equalsIgnoreCase(this.offline);
    }

    public void enableOffline() {
        this.offline = "1";
    }

    /* access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Deprecated
    public void enableOffline(boolean enable) {
        if (enable) {
            this.offline = "1";
        } else {
            this.offline = null;
        }
    }
}
