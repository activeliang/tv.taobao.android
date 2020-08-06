package com.taobao.orange.model;

import android.text.TextUtils;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.util.OLog;
import java.io.Serializable;
import java.util.Map;

public class ConfigDO implements Serializable {
    private static final String TAG = "ConfigDO";
    public String appKey;
    public String appVersion;
    public CandidateDO candidate;
    public Map<String, String> content;
    public String createTime;
    public String id;
    public String loadLevel;
    public String name;
    public String resourceId;
    public String type;
    public String version;

    public boolean checkValid() {
        boolean vaild;
        if (TextUtils.isEmpty(this.appKey) || TextUtils.isEmpty(this.appVersion) || TextUtils.isEmpty(this.id) || TextUtils.isEmpty(this.name) || TextUtils.isEmpty(this.resourceId) || TextUtils.isEmpty(this.type) || TextUtils.isEmpty(this.loadLevel) || TextUtils.isEmpty(this.version) || this.content == null || this.content.isEmpty()) {
            OLog.w(TAG, "lack param", new Object[0]);
            return false;
        }
        if ((this.appVersion.equals("*") || this.appVersion.equals(GlobalOrange.appVersion)) && this.appKey.equals(GlobalOrange.appKey)) {
            vaild = true;
        } else {
            vaild = false;
        }
        if (vaild) {
            return vaild;
        }
        OLog.w(TAG, "invaild", new Object[0]);
        return vaild;
    }

    public String getCurVersion() {
        return this.candidate == null ? this.version : this.candidate.version;
    }

    public String toString() {
        return String.format("ConfigDO{name:'%s', appVersion:'%s', verison:'%s'}", new Object[]{this.name, this.appVersion, this.version});
    }
}
