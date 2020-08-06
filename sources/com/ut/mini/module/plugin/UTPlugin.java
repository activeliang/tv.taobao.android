package com.ut.mini.module.plugin;

import java.util.Map;

public abstract class UTPlugin {
    public abstract int[] getAttentionEventIds();

    public Map<String, String> onEventDispatch(String aPageName, int aEventID, String aArg1, String aArg2, String aArg3) {
        return null;
    }

    public Map<String, String> onEventDispatch(String aPageName, int aEventID, String aArg1, String aArg2, String aArg3, Map<String, String> map) {
        return onEventDispatch(aPageName, aEventID, aArg1, aArg2, aArg3);
    }

    public static boolean isEventIDInRange(int[] aUserPluginEventA, int aLogEventID) {
        if (aUserPluginEventA != null) {
            if (aUserPluginEventA[0] == -1) {
                return true;
            }
            for (int i : aUserPluginEventA) {
                if (i == aLogEventID) {
                    return true;
                }
            }
        }
        return false;
    }
}
