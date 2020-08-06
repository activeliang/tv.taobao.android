package com.yunos.tv.blitz.request.common;

import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.global.BzEnvEnum;

public class MtopRequestConfig {

    private static final class HttpDomain {
        public static final String DALIY = "http://api.waptest.taobao.com/rest/api3.do";
        public static final String PREDEPLOY = "http://api.wapa.taobao.com/rest/api3.do";
        public static final String PRODUCTION = "http://api.m.taobao.com/rest/api3.do";

        private HttpDomain() {
        }
    }

    public static String getHttpDomain() {
        if (BzAppConfig.env == BzEnvEnum.ONLINE) {
            return HttpDomain.PRODUCTION;
        }
        if (BzAppConfig.env == BzEnvEnum.PRE) {
            return "http://api.wapa.taobao.com/rest/api3.do";
        }
        return "http://api.waptest.taobao.com/rest/api3.do";
    }
}
