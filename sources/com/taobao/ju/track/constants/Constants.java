package com.taobao.ju.track.constants;

public class Constants {
    public static final String CSV_PARAM_INTERNAL_ANDROID_PAGE_KEY = "_AndroidKey";
    public static final String CSV_PARAM_INTERNAL_KEY = "_key";
    public static final String PARAM_OUTER_SPM_AB_OR_CD_NONE = "0.0";
    public static final String PARAM_OUTER_SPM_CNT = "spm-cnt";
    public static final String PARAM_OUTER_SPM_NONE = "0.0.0.0";
    public static final String PARAM_OUTER_SPM_URL = "spm-url";
    public static final String PARAM_PAGER_POS = "pager_pos";
    public static final String PARAM_POS = "pos";
    public static boolean sPosStartFromOne = true;

    public static boolean isPosStartFromOne() {
        return sPosStartFromOne;
    }

    public static void setPosStartFromOne(boolean sPosStartFromOne2) {
        sPosStartFromOne = sPosStartFromOne2;
    }
}
