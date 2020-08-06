package com.yunos.tv.core.config;

public class MtopRequestConfig extends RequestConfig {

    private static final class HttpDomain {
        public static final String DALIY = "http://api.waptest.taobao.com/rest/api3.do";
        public static final String PREDEPLOY = "http://api.wapa.taobao.com/rest/api3.do";
        public static final String PRODUCTION = "https://m.yunos.wasu.tv/rest/api3.do?";

        private HttpDomain() {
        }
    }

    public static String getHttpDomain() {
        if (Config.getRunMode() == RunMode.PRODUCTION) {
            return HttpDomain.PRODUCTION;
        }
        if (Config.getRunMode() == RunMode.PREDEPLOY) {
            return "http://api.wapa.taobao.com/rest/api3.do";
        }
        return "http://api.waptest.taobao.com/rest/api3.do";
    }
}
