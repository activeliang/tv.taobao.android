package com.alibaba.analytics.core.sync;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;

public class HttpsHostPortMgr implements SystemConfigMgr.IKVChangeListener {
    public static final String TAG_HTTPS_HOST_PORT = "utanalytics_https_host";
    public static HttpsHostPortMgr instance;
    private String mHttpsUrl = "https://h-adashx.ut.taobao.com/upload";

    public static synchronized HttpsHostPortMgr getInstance() {
        HttpsHostPortMgr httpsHostPortMgr;
        synchronized (HttpsHostPortMgr.class) {
            if (instance == null) {
                instance = new HttpsHostPortMgr();
            }
            httpsHostPortMgr = instance;
        }
        return httpsHostPortMgr;
    }

    HttpsHostPortMgr() {
        try {
            parseConifg(AppInfoUtil.getString(Variables.getInstance().getContext(), TAG_HTTPS_HOST_PORT));
            parseConifg(SpSetting.get(Variables.getInstance().getContext(), TAG_HTTPS_HOST_PORT));
            parseConifg(SystemConfigMgr.getInstance().get(TAG_HTTPS_HOST_PORT));
            SystemConfigMgr.getInstance().register(TAG_HTTPS_HOST_PORT, this);
        } catch (Throwable th) {
        }
    }

    public void onChange(String key, String value) {
        parseConifg(value);
    }

    private void parseConifg(String value) {
        if (!TextUtils.isEmpty(value)) {
            this.mHttpsUrl = "https://" + value + "/upload";
        }
    }

    public String getHttpsUrl() {
        Logger.d("", "mHttpsUrl", this.mHttpsUrl);
        return this.mHttpsUrl;
    }
}
