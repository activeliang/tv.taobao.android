package com.alibaba.motu.crashreporter.utrestapi;

public class UTConstants {
    public static final String G_ABTEST_URL = "http://adash.m.taobao.com/rest/abtest";
    public static final String G_CONF_URL = "http://c-adash.m.taobao.com/rest/gc";
    public static String G_FIXED_TRANSFER_URL = "http://adash.m.taobao.com/rest/sur";
    public static final int G_LOG_CACHE_CAPACITY = 1000;
    public static final int G_MAX_CONNECTION_TIME_OUT = 10000;
    public static final int G_MAX_PER_LOG_SIZE = 102400;
    public static final int G_MAX_PER_TRANSFER_COUNT = 100;
    public static final int G_MAX_PER_UPLOAD_PACKAGE_SIZE = 102400;
    public static final int G_MAX_READ_CONNECTION_STREAM_TIME_OUT = 30000;
    public static final String G_SDK_VERSION = "4.1.0";
    private static String G_TRANSFER_URL = "http://adash.m.taobao.com/rest/sur";
    public static final int G_UPLOAD_INTERVAL = 30;

    public static class EventID {
        public static final int AGGREGATION_LOG = 6699;
    }

    public static class LogContentKeys {
        public static final String PRIORITY = "_priority";
    }

    public static synchronized String getTransferUrl() {
        String str;
        synchronized (UTConstants.class) {
            str = G_TRANSFER_URL;
        }
        return str;
    }

    public static synchronized void setTransferUrl(String aUrl) {
        synchronized (UTConstants.class) {
            G_TRANSFER_URL = aUrl;
        }
    }

    public static String getRC4PrivateKey() {
        return "QrMgt8GGYI6T52ZY5AnhtxkLzb8egpFn3j5JELI8H6wtACbUnZ5cc3aYTsTRbmkAkRJeYbtx92LPBWm7nBO9UIl7y5i5MQNmUZNf5QENurR5tGyo7yJ2G0MBjWvy6iAtlAbacKP0SwOUeUWx5dsBdyhxa7Id1APtybSdDgicBDuNjI0mlZFUzZSS9dmN8lBD0WTVOMz0pRZbR3cysomRXOO1ghqjJdTcyDIxzpNAEszN8RMGjrzyU7Hjbmwi6YNK";
    }

    public final class PrivateLogFields {
        public static final String HOST_APPKEY = "_hak";
        public static final String HOST_APPVERSION = "_hav";
        public static final String SEND_LOG_SYNC = "_sls";

        public PrivateLogFields() {
        }
    }
}
