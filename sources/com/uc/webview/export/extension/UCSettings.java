package com.uc.webview.export.extension;

import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.utility.Log;

@Api
/* compiled from: ProGuard */
public abstract class UCSettings {
    public static int FORCE_USER_SCALABLE_DEFAULT = 0;
    public static int FORCE_USER_SCALABLE_DISABLE = 2;
    public static int FORCE_USER_SCALABLE_ENABLE = 1;
    public static final int FORM_SAVE_TYPE_AUTO = 1;
    public static final int FORM_SAVE_TYPE_NO = 2;
    public static final int FORM_SAVE_TYPE_PROMPT = 0;
    public static final int IMAGE_QUALITY_FULL_COLOR = 3;
    public static final int IMAGE_QUALITY_LOW_COLOR = 1;
    public static final int IMAGE_QUALITY_NO_IMAGE = 0;
    public static final int IMAGE_QUALITY_STANDARD = 2;
    public static final int LAYOUT_MODE_ADAPT = 2;
    public static final int LAYOUT_MODE_ZOOM = 1;
    public static final int PREREAD_TYPE_NON = 0;
    public static final int PREREAD_TYPE_WAP = 1;
    public static final int PREREAD_TYPE_WAP_AND_WEB = 3;
    public static final int PREREAD_TYPE_WEB = 2;
    public static final int THEME_DEFAULT = 0;
    public static final int THEME_GREEN = 1;
    public static final int THEME_TRANSPARENT = -1;

    public abstract boolean getDisplaySoftKeyboardOnFocused();

    public abstract boolean getEnableUCProxy();

    public abstract boolean getForceUCProxy();

    public abstract int getUCCookieType();

    public abstract void setDisplaySoftKeyboardOnFocused(boolean z);

    public abstract void setEnableUCProxy(boolean z);

    public abstract void setForceUCProxy(boolean z);

    public abstract void setUCCookieType(int i);

    public static void setLayoutMode(int i) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null && iGlobalSettings.getIntValue("LayoutStyle") != i) {
            iGlobalSettings.setIntValue("LayoutStyle", i);
        }
    }

    public static int getLayoutMode() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getIntValue("LayoutStyle");
        }
        return -1;
    }

    public static void setNightMode(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null && iGlobalSettings.getBoolValue("IsNightMode") != z) {
            iGlobalSettings.setBoolValue("IsNightMode", z);
        }
    }

    public static boolean isNightMode() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getBoolValue("IsNightMode");
        }
        return false;
    }

    public static void setEnableCustomErrorPage(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null && iGlobalSettings.getBoolValue("EnableCustomErrPage") != z) {
            iGlobalSettings.setBoolValue("EnableCustomErrPage", z);
        }
    }

    public static boolean isEnableCustomErrorPage() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getBoolValue("EnableCustomErrPage");
        }
        return false;
    }

    public static void setGlobalEnableUCProxy(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("global_enable_ucproxy", z);
        }
    }

    public static void setEnableAdblock(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("EnableAdBlock", z);
        }
    }

    public static void setEnableDispatcher(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("enable_dispatcher", z);
        }
    }

    public static void setEnableMultiThreadParser(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("enable_multithread_parser", z);
        }
    }

    public static void setEnableAllResourceCallBack(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("enable_allresponse_callback", z);
        }
    }

    public static void setEnableRequestIntercept(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("enable_request_intercept", z);
        }
    }

    public static boolean getEnableRequestIntercept() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getBoolValue("enable_request_intercept");
        }
        return false;
    }

    public static boolean getEnableAllResourceCallBack() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getBoolValue("enable_allresponse_callback");
        }
        return false;
    }

    public static void setPageCacheCapacity(int i) {
        if (i < 0 || i > 20) {
            throw new IllegalArgumentException("capacity : " + i + ", should be a non-negative integer between 0 (no cache) and 20 (max).");
        }
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setIntValue("CachePageNumber", i);
        }
    }

    public static int getPageCacheCapacity() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getIntValue("CachePageNumber");
        }
        return -1;
    }

    public void setEnableFastScroller(boolean z) {
        Log.w("UCSettings", "setEnableFastScroller not override");
    }

    public boolean enableFastScroller() {
        Log.w("UCSettings", "enableFastScroller not override");
        return false;
    }

    public static void setForceUserScalable(int i) {
        if (i == FORCE_USER_SCALABLE_DEFAULT || i == FORCE_USER_SCALABLE_ENABLE || i == FORCE_USER_SCALABLE_DISABLE) {
            IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
            if (iGlobalSettings != null) {
                iGlobalSettings.setIntValue("PageForceUserScalable", i);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("enable : " + i + ", should be one of FORCE_USER_SCALABLE_DEFAULT/FORCE_USER_SCALABLE_ENABLE/FORCE_USER_SCALABLE_DISABLE");
    }

    public static void setPageColorTheme(int i) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings == null) {
            return;
        }
        if (i == -1) {
            iGlobalSettings.setBoolValue("IsTransparentTheme", true);
            return;
        }
        if (iGlobalSettings.getBoolValue("IsTransparentTheme")) {
            iGlobalSettings.setBoolValue("IsTransparentTheme", false);
        }
        iGlobalSettings.setIntValue("PageColorTheme", i);
    }

    public static int getPageColorTheme() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings == null) {
            return 0;
        }
        if (iGlobalSettings.getBoolValue("IsTransparentTheme")) {
            return -1;
        }
        return iGlobalSettings.getIntValue("PageColorTheme");
    }

    public static void setImageQuality(int i) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setIntValue("ImageQuality", i);
        }
    }

    public static int getImageQuality() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getIntValue("ImageQuality");
        }
        return 2;
    }

    public static void setSmartReader(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("EnableSmartReader", z);
        }
    }

    public static boolean getSmartReader() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getBoolValue("EnableSmartReader");
        }
        return false;
    }

    public static void setPrereadType(int i) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setIntValue("PrereadOptions", i);
        }
    }

    public static int getPrereadType() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getIntValue("PrereadOptions");
        }
        return 0;
    }

    public static void setFormSaveType(int i) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setIntValue("FormSave", i);
        }
    }

    public static int getFormSaveType() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getIntValue("FormSave");
        }
        return 0;
    }

    public static void setEnableUCVideoViewFullscreen(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("enable_uc_videoview_fullscreen", z);
        }
    }

    public static boolean enableUCVideoViewFullscreen() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getBoolValue("enable_uc_videoview_fullscreen");
        }
        return false;
    }

    public static void disableNetwork(Boolean bool) {
        Log.w("UCSettings", "background netoff current value=" + bool);
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("OFFNET_ON", bool.booleanValue());
        }
    }

    public static boolean isNetworkDisabled() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getBoolValue("OFFNET_ON");
        }
        return false;
    }

    public static void setEnableUCParam(boolean z) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setBoolValue("SDKUCParam", z);
        }
    }

    public static boolean enableUCParam() {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            return iGlobalSettings.getBoolValue("SDKUCParam");
        }
        return false;
    }

    public static void setGlobalIntValue(String str, int i) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setIntValue(str, i);
        }
    }

    public static void setGlobalStringValue(String str, String str2) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setStringValue(str, str2);
        }
    }

    public static void setRIPort(int i) {
        IGlobalSettings iGlobalSettings = (IGlobalSettings) d.a(10022, new Object[0]);
        if (iGlobalSettings != null) {
            iGlobalSettings.setStringValue("SDKRIPort", String.valueOf(i));
        }
    }
}
