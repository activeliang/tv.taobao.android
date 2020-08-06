package com.alibaba.appmonitor.event;

import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.ReuseJSONArray;
import com.alibaba.appmonitor.pool.ReuseJSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class AlarmEvent extends Event {
    private static final int ERROR_MSG_MAX_LENGTH = 100;
    public Map<String, Integer> errorCodeCount;
    public Map<String, String> errorMsgMap;
    public int failCount = 0;
    public int successCount = 0;

    public synchronized void incrSuccess(Long commitTime) {
        this.successCount++;
        super.commit(commitTime);
    }

    public synchronized void incrFail(Long commitTime) {
        this.failCount++;
        super.commit(commitTime);
    }

    public synchronized void addError(String errorCode, String msg) {
        int i = 100;
        synchronized (this) {
            if (!StringUtils.isBlank(errorCode)) {
                if (this.errorMsgMap == null) {
                    this.errorMsgMap = new HashMap();
                }
                if (this.errorCodeCount == null) {
                    this.errorCodeCount = new HashMap();
                }
                if (StringUtils.isNotBlank(msg)) {
                    if (msg.length() <= 100) {
                        i = msg.length();
                    }
                    this.errorMsgMap.put(errorCode, msg.substring(0, i));
                }
                if (!this.errorCodeCount.containsKey(errorCode)) {
                    this.errorCodeCount.put(errorCode, 1);
                } else {
                    this.errorCodeCount.put(errorCode, Integer.valueOf(this.errorCodeCount.get(errorCode).intValue() + 1));
                }
            }
        }
    }

    public synchronized JSONObject dumpToJSONObject() {
        JSONObject jobject;
        jobject = super.dumpToJSONObject();
        jobject.put("successCount", (Object) Integer.valueOf(this.successCount));
        jobject.put("failCount", (Object) Integer.valueOf(this.failCount));
        if (this.errorCodeCount != null) {
            JSONArray errorInfos = (JSONArray) BalancedPool.getInstance().poll(ReuseJSONArray.class, new Object[0]);
            for (Map.Entry<String, Integer> entry : this.errorCodeCount.entrySet()) {
                JSONObject errorInfo = (JSONObject) BalancedPool.getInstance().poll(ReuseJSONObject.class, new Object[0]);
                String key = entry.getKey();
                errorInfo.put("errorCode", (Object) key);
                errorInfo.put("errorCount", (Object) entry.getValue());
                if (this.errorMsgMap.containsKey(key)) {
                    errorInfo.put("errorMsg", (Object) this.errorMsgMap.get(key));
                }
                errorInfos.add(errorInfo);
            }
            jobject.put("errors", (Object) errorInfos);
        }
        return jobject;
    }

    public synchronized void clean() {
        super.clean();
        this.successCount = 0;
        this.failCount = 0;
        if (this.errorMsgMap != null) {
            this.errorMsgMap.clear();
        }
        if (this.errorCodeCount != null) {
            this.errorCodeCount.clear();
        }
    }
}
