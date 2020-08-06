package com.yunos.alitvcompliance.utils;

import java.util.Map;

public class UTHelper {

    public interface IUTCustomEventSender {
        void sendCustomEvent(String str, Map<String, String> map);
    }

    public interface IUTInitializer {
        void initUT();
    }
}
