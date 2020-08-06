package com.taobao.alimama.global;

public final class Constants {
    public static final String LOG_TAG = "Munion";
    public static final String ORANGE_GROUP_NAME = "alimama_ad";
    public static final String SDK_VERSION = "5.0.2-proguard";
    public static final String TAG = "AlimamaSdk";

    public static final class ClickIdPrefix {
        public static final String APP_LINK_CPC = "A270_";
        public static final String APP_LINK_CPS = "A220_";
        public static final String CPC = "A1_";
        public static final String CPM = "A17_";
        public static final String CPM_BRAND = "A42_";
        public static final String CPS = "A18_";
    }

    public static final class Monitor {
        public static final String PAGE_NAME = "Munion";

        public static final class Points {
            public static final String INIT = "Munion_SDK_Init";
            public static final String Init_Req_Cps_ChannelE = "Munion_Init_Req_Cps_ChannelE";
            public static final String MUNION_FETCH_PARAM = "Munion_fetch_param";
            public static final String MUNION_INIT = "Munion_Init";
            public static final String MUNION_TK_CPS_PARAM_PARSE = "Munion_tk_cps_param_parse";
            public static final String Req_Cps_ChannelE = "Munion_Req_Cps_ChannelE";
            public static final String Req_Cps_MiLingE_Fail = "Munion_Req_Cps_MiLingE_Fail";
            public static final String URL_PARAM_REPACE = "Munion_Url_Param_Replace";
            public static final String Upload_CpsE_Fail = "Munion_Upload_CpsE_Fail";
            public static final String Upload_Cps_E = "Munion_Upload_Cps_E";
            public static final String Url_Handle_Cps9 = "Munion_Url_Handle_Cps9";
            public static final String Url_Handle_GlobalE = "Munion_Url_Handle_GlobalE";
        }
    }

    public static final class PerfScene {
        public static final String CPM_LOAD_CACHE = "cpm_load_cache";
        public static final String CPM_REQUEST = "cpm_request";
    }

    public static final class UtEventId {
        public static final int CLICK_BEFORE = 9001;
        public static final int CLICK_RETURN = 9002;
        public static final int CLICK_WAKEUP = 9000;
        public static final int CUSTOM = 19999;
        public static final int DEBUG = 9005;
        public static final int TRACK = 9004;
    }
}
