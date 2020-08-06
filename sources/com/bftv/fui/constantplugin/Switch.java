package com.bftv.fui.constantplugin;

public class Switch {
    private static boolean sIsUseSdk = true;

    public static boolean getIsUseSdk() {
        return sIsUseSdk;
    }

    public static void setUseSdk(boolean isUseSdk) {
        sIsUseSdk = isUseSdk;
    }
}
