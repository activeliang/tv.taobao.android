package com.taobao.windvane.zipdownload;

import java.util.Map;

public interface DownLoadListener {
    public static final int TOKEN_2G_NOT_UPDATE = 8;
    public static final int TOKEN_ALL_CONFIG = 1;
    public static final int TOKEN_NOT_CONTIONUE = 10;
    public static final int TOKEN_NOT_NEED_UPDATE = 9;
    public static final int TOKEN_UPDATE_CANCEL = 7;
    public static final int TOKEN_ZIPAPP = 4;
    public static final int TOKEN_ZIPAPP_DELTA = 2;

    void callback(String str, String str2, Map<String, String> map, int i, Object obj);
}
