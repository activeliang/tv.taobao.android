package com.taobao.wireless.detail;

import com.taobao.wireless.lang.CheckUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DetailConfig implements Serializable {
    public static final String BETA = "beta";
    public static final String ONLINE = "m";
    public static String TEST_ENV = "test_env";
    public static final Map<String, String> URL_DESC_MAP = new HashMap();
    public static final Map<String, String> URL_MAP = new HashMap();
    public static final String WAPA = "wapa";
    public static final String WAPTEST = "waptest";
    public static String env = ONLINE;
    public static String testCdnUrl;
    public static String ttid;

    static {
        URL_MAP.put(ONLINE, "http://hws.m.taobao.com/cache/wdetail/5.0/");
        URL_MAP.put(WAPTEST, "http://item.daily.taobao.net/modulet/v5/wdetailEsi.do");
        URL_MAP.put(WAPA, "http://hws.wapa.taobao.com/cache/wdetail/5.0/");
        URL_MAP.put("beta", "http://hwsbeta.m.taobao.com/cache/wdetail/5.0/");
        URL_DESC_MAP.put(ONLINE, "http://hws.m.taobao.com/ds/json/wap/dwdetailDesc.do");
        URL_DESC_MAP.put(WAPTEST, "http://detailskip.daily.taobao.net/json/wap/dwdetailDesc.do");
        URL_DESC_MAP.put(WAPA, "http://hws.wapa.taobao.com/ds/json/wap/dwdetailDesc.do");
        URL_DESC_MAP.put("beta", "http://hws.m.taobao.com/ds/json/wap/dwdetailDesc.do");
    }

    public static String getDetailMainUrl() {
        if (testCdnUrl != null && testCdnUrl.length() > 0) {
            return testCdnUrl;
        }
        String url = URL_MAP.get(env);
        if (CheckUtils.isEmpty(url)) {
            return "http://hws.m.taobao.com/cache/wdetail/5.0/";
        }
        return url;
    }

    public static String getDetailMainUrl(String env2) {
        String url = URL_MAP.get(env2);
        if (CheckUtils.isEmpty(url)) {
            return "http://hws.m.taobao.com/cache/wdetail/5.0/";
        }
        return url;
    }

    public static String getDescLayoutMainUrl() {
        String url = URL_DESC_MAP.get(env);
        if (CheckUtils.isEmpty(url)) {
            return URL_DESC_MAP.get(WAPA);
        }
        return url;
    }
}
