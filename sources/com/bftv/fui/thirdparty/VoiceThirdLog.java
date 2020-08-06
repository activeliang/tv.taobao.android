package com.bftv.fui.thirdparty;

import android.util.Log;

public class VoiceThirdLog {
    public static boolean sIsDebug = false;

    public static void l(String msg) {
        if (sIsDebug) {
            Log.e("less", msg);
        }
    }
}
