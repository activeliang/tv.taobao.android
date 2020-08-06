package com.ali.user.open.core;

import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Site {
    public static final String ALIPAY = "alipay";
    public static final String AMAP = "amap";
    public static final String DAMAI = "damai";
    public static final String DING = "dingding";
    public static final String ELEME = "eleme";
    public static final String ICBU = "icbu";
    public static final String KAOLA = "kaola";
    public static final String LAIFENG = "lai_feng";
    public static final String STARBUCKS = "starbucks";
    public static final String TAOBAO = "taobao";
    public static final String UC = "uc";
    public static final String XIAMI = "xiami";
    public static final String YABO = "yabo";
    public static final String YOUKU = "youku";

    @Retention(RetentionPolicy.SOURCE)
    public @interface SiteName {
    }

    public static boolean isHavanaSite(String site) {
        if (TextUtils.equals(site, TAOBAO) || TextUtils.equals(site, ICBU) || TextUtils.equals(site, DAMAI) || TextUtils.equals(site, YABO) || TextUtils.equals(site, YOUKU)) {
            return true;
        }
        return false;
    }

    public static int getHavanaSite(String site) {
        if (TextUtils.equals(site, TAOBAO)) {
            return 0;
        }
        if (TextUtils.equals(site, ICBU)) {
            return 4;
        }
        if (TextUtils.equals(site, DAMAI)) {
            return 18;
        }
        if (TextUtils.equals(site, YABO)) {
            return 28;
        }
        if (TextUtils.equals(site, KAOLA)) {
            return 39;
        }
        return 0;
    }

    public static String getMtopInstanceTag(String site) {
        return "havana-instance-" + site;
    }
}
