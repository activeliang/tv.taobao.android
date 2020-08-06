package com.taobao.orange.model;

import android.text.TextUtils;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.util.OLog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndexDO implements Serializable {
    private static final String TAG = "IndexDO";
    public String appIndexVersion;
    public String appKey;
    public String appVersion;
    public String cdn;
    public String createTime;
    public String id;
    public String md5;
    public List<NameSpaceDO> mergedNamespaces = new ArrayList();
    public String protocol;
    public String version;
    public String versionIndexVersion;

    public IndexDO() {
    }

    public IndexDO(IndexDO indexDO) {
        this.appKey = indexDO.appKey;
        this.appVersion = indexDO.appVersion;
        this.appIndexVersion = indexDO.appIndexVersion;
        this.versionIndexVersion = indexDO.versionIndexVersion;
        this.createTime = indexDO.createTime;
        this.id = indexDO.id;
        this.cdn = indexDO.cdn;
        this.version = indexDO.version;
        this.mergedNamespaces.addAll(indexDO.mergedNamespaces);
    }

    public boolean checkValid() {
        boolean vaild;
        if (TextUtils.isEmpty(this.appKey) || TextUtils.isEmpty(this.appVersion) || TextUtils.isEmpty(this.appIndexVersion) || TextUtils.isEmpty(this.versionIndexVersion) || TextUtils.isEmpty(this.id) || TextUtils.isEmpty(this.cdn) || TextUtils.isEmpty(this.version) || this.mergedNamespaces == null || this.mergedNamespaces.isEmpty()) {
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
}
