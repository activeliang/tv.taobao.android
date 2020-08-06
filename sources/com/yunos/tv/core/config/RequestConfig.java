package com.yunos.tv.core.config;

import dalvik.system.DexClassLoader;

public class RequestConfig {
    private static final String AUTH_TYPE = "md5";
    private static final String DEFAULT_API_VERSION = "1.0";
    public static final int DEFAULT_PAGE_SIZE = 30;
    public static final String DOMAIN_PAY_DAILY = "http://mali.alipay.net";
    public static final String DOMAIN_PAY_PREDEPLOY = "http://mali.alipay.com";
    public static final String DOMAIN_PAY_PRODUCTION = "http://mali.alipay.com";
    static DexClassLoader cl;

    private static final class ImageServer {
        public static final String[] DALIY = {"http://img01.daily.taobaocdn.net/bao/uploaded/", "http://img02.daily.taobaocdn.net/bao/uploaded/", "http://img03.daily.taobaocdn.net/bao/uploaded/"};
        public static final String[] PREDEPLOY = {"http://img01.taobaocdn.com/bao/uploaded/", "http://img02.taobaocdn.com/bao/uploaded/", "http://img03.taobaocdn.com/bao/uploaded/", "http://img04.taobaocdn.com/bao/uploaded/"};
        public static final String[] PRODUCTION = {"http://img01.taobaocdn.com/bao/uploaded/", "http://img02.taobaocdn.com/bao/uploaded/", "http://img03.taobaocdn.com/bao/uploaded/", "http://img04.taobaocdn.com/bao/uploaded/"};

        private ImageServer() {
        }
    }

    public static String getApiVersion() {
        return "1.0";
    }

    public static String[] getImageServer() {
        if (Config.getRunMode() == RunMode.PRODUCTION) {
            return ImageServer.PRODUCTION;
        }
        if (Config.getRunMode() == RunMode.PREDEPLOY) {
            return ImageServer.PREDEPLOY;
        }
        return ImageServer.DALIY;
    }
}
