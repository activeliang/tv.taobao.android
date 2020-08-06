package com.yunos.tv.core.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.taobao.detail.DisplayTypeConstants;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityDataUtil {
    private static ActivityDataUtil activityData;
    private static final Object initLock = new Object();
    private List<String> keyData;
    private HashMap<String, String> mAppHostMap;
    private HashMap<String, Class<?>> mSelfActivityMap;

    public static ActivityDataUtil getInstance() {
        if (activityData == null) {
            synchronized (initLock) {
                if (activityData == null) {
                    activityData = new ActivityDataUtil();
                    activityData.initKeyData();
                    activityData.initAppHostMap();
                }
            }
        }
        return activityData;
    }

    private void initKeyData() {
        this.keyData = new ArrayList();
        this.keyData.add("shopId");
        this.keyData.add("itemId");
    }

    public void setSelfActivityMap(HashMap<String, Class<?>> selfActivityMap) {
        this.mSelfActivityMap = selfActivityMap;
    }

    private void initAppHostMap() {
        if (this.mAppHostMap == null) {
            this.mAppHostMap = new HashMap<>();
            this.mAppHostMap.put("zhuanti", "tvtaobao://zhuanti?");
            this.mAppHostMap.put("browser", "tvtaobao://browser?");
            this.mAppHostMap.put("taobaosdk", "tvtaobao://taobaosdk?");
            this.mAppHostMap.put(BaseConfig.INTENT_KEY_MODULE_JUHUASUAN, "tvtaobao://juhuasuan?");
            this.mAppHostMap.put(DisplayTypeConstants.SEC_KILL, "tvtaobao://seckill?");
            this.mAppHostMap.put("chaoshi", "tvtaobao://chaoshi?");
            this.mAppHostMap.put("caipiao", "tvtaobao://caipiao?");
            this.mAppHostMap.put(BaseConfig.INTENT_KEY_MODULE_TVBUY_SHOPPING, "tvtaobao://tvshopping?");
            this.mAppHostMap.put(BaseConfig.INTENT_KEY_MODULE_FLASHSALE_MAIN, "tvtaobao://flashsale?");
        }
    }

    public void setKeyData(List<String> keys) {
        this.keyData.addAll(keys);
    }

    public Uri getPreviousUri(Class<?> c, Intent intent) {
        if (this.mSelfActivityMap != null) {
            for (String moudle : this.mSelfActivityMap.keySet()) {
                if (this.mSelfActivityMap.get(moudle) == c) {
                    Bundle bundle = intent.getExtras();
                    StringBuilder sb = new StringBuilder();
                    sb.append("tvtaobao://home?module=" + moudle);
                    for (String key : bundle.keySet()) {
                        for (int i = 0; i < this.keyData.size(); i++) {
                            if (key.equals(this.keyData.get(i))) {
                                sb.append("&" + key + "=" + bundle.get(key));
                            }
                        }
                    }
                    return Uri.parse(sb.toString());
                }
            }
        }
        return null;
    }

    public Uri getAppHostUri(Uri uri) {
        if (this.mAppHostMap == null) {
            return uri;
        }
        String path = uri.toString();
        for (String key : this.mAppHostMap.keySet()) {
            if (path.startsWith(this.mAppHostMap.get(key))) {
                if (path.contains("app=")) {
                    path = path.replace(this.mAppHostMap.get(key), "tvtaobao://home?");
                } else {
                    path = path.replace(this.mAppHostMap.get(key), "tvtaobao://home?app=" + key);
                }
            }
        }
        return Uri.parse(path);
    }
}
