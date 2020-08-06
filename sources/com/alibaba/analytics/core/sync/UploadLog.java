package com.alibaba.analytics.core.sync;

import android.text.TextUtils;
import com.ali.auth.third.offline.login.LoginConstants;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.UTBaseConfMgr;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.utils.Logger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class UploadLog {
    protected NetworkStatus mAllowedNetworkStatus = NetworkStatus.ALL;
    protected IUploadExcuted mIUploadExcuted = null;
    protected int mMaxUploadTimes = 3;

    public enum NetworkStatus {
        ALL,
        WIFI,
        TWO_GENERATION,
        THRID_GENERATION,
        FOUR_GENERATION,
        NONE
    }

    public void setUploadTimes(int uploadTimes) {
        this.mMaxUploadTimes = uploadTimes;
    }

    public void setAllowedNetworkStatus(NetworkStatus allowedNetworkStatus) {
        this.mAllowedNetworkStatus = allowedNetworkStatus;
    }

    public void setIUploadExcuted(IUploadExcuted uploadExcuted) {
        this.mIUploadExcuted = uploadExcuted;
    }

    /* access modifiers changed from: protected */
    public NetworkStatus getNetworkStatus() {
        String status = NetworkUtil.getNetworkType();
        if ("2G".equalsIgnoreCase(status)) {
            return NetworkStatus.TWO_GENERATION;
        }
        if ("3G".equalsIgnoreCase(status)) {
            return NetworkStatus.THRID_GENERATION;
        }
        if ("4G".equalsIgnoreCase(status)) {
            return NetworkStatus.FOUR_GENERATION;
        }
        if ("Wi-Fi".equalsIgnoreCase(status)) {
            return NetworkStatus.WIFI;
        }
        return NetworkStatus.NONE;
    }

    public void parserConfig(String data) {
        JSONObject configObject;
        Iterator<String> keys;
        String str;
        if (!TextUtils.isEmpty(data)) {
            try {
                UTBaseConfMgr configMgr = Variables.getInstance().getConfMgr();
                if (configMgr != null && (configObject = new JSONObject(data).getJSONObject(LoginConstants.CONFIG)) != null) {
                    Iterator<String> namespaces = configObject.keys();
                    if (namespaces == null || !namespaces.hasNext()) {
                        Logger.i((String) null, "No Config Update");
                        return;
                    }
                    while (namespaces.hasNext()) {
                        String namespace = namespaces.next();
                        if (!TextUtils.isEmpty(namespace)) {
                            HashMap<String, String> configs = new HashMap<>();
                            JSONObject configContentItem = configObject.getJSONObject(namespace);
                            if (!(configContentItem == null || (keys = configContentItem.keys()) == null)) {
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    if (configContentItem.get(key) == null) {
                                        str = null;
                                    } else {
                                        str = configContentItem.get(key) + "";
                                    }
                                    configs.put(key, str);
                                }
                            }
                            Logger.d("Config Update", "namespace", namespace, "configs", configs);
                            configMgr.updateAndDispatch(namespace, (Map<String, String>) configs);
                        }
                    }
                    UTBaseConfMgr.sendConfigTimeStamp("1");
                }
            } catch (Throwable e) {
                Logger.e("", e, new Object[0]);
            }
        } else {
            Logger.w((String) null, "Config Is Empty");
        }
    }
}
