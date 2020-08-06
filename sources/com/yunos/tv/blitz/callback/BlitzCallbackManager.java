package com.yunos.tv.blitz.callback;

public class BlitzCallbackManager {
    private static native void nativeCall(String str, String str2, String str3);

    private static native void nativeReleaseCallback();

    public static void callNative(String name, String data, String key) {
        nativeCall(name, data, key);
    }

    public static void releaseAllCallbacks() {
        nativeReleaseCallback();
    }
}
