package com.yunos.tvtaobao.biz.common;

import java.util.HashMap;
import java.util.Map;

public class NoPutToStack {
    private static Map<String, Boolean> stackMap;

    public static Map<String, Boolean> getMap() {
        if (stackMap == null) {
            stackMap = new HashMap();
            stackMap.put("com.yunos.tvtaobao.splashscreen.activity.StartActivity", true);
            stackMap.put("com.yunos.tvtaobao.activity.loading.WelcomeActivity", true);
            stackMap.put("com.yunos.tvtaobao.splashscreen.activity.LoadingActivity", true);
            stackMap.put("com.yunos.tvtaobao.zhuanti.activity.PreviousActivity", true);
            stackMap.put("com.yunos.seckill.activity.HomeActivity", true);
            stackMap.put(BaseConfig.SWITCH_TO_JUHUASUAN_ACTIVITY, true);
            stackMap.put(BaseConfig.SWITCH_TO_HOME_ACTIVITY, true);
            stackMap.put(BaseConfig.SWITCH_TO_MENU_ACTIVITY, true);
            stackMap.put("com.yunos.tvtaobao.activity.advertisement.activity.AdvertisementActivity", true);
        }
        return stackMap;
    }

    public static Map<String, Boolean> getVoiceMap() {
        if (stackMap == null) {
            stackMap = new HashMap();
            stackMap.put("com.yunos.tvtaobao.splashscreen.activity.StartActivity", true);
            stackMap.put("com.yunos.tvtaobao.activity.loading.WelcomeActivity", true);
            stackMap.put("com.yunos.tvtaobao.splashscreen.activity.LoadingActivity", true);
            stackMap.put("com.yunos.tvtaobao.zhuanti.activity.PreviousActivity", true);
            stackMap.put("com.yunos.seckill.activity.HomeActivity", true);
            stackMap.put(BaseConfig.SWITCH_TO_JUHUASUAN_ACTIVITY, true);
            stackMap.put(BaseConfig.SWITCH_TO_MENU_ACTIVITY, true);
            stackMap.put("com.yunos.tvtaobao.activity.advertisement.activity.AdvertisementActivity", true);
        }
        return stackMap;
    }
}
