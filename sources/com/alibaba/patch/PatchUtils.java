package com.alibaba.patch;

public class PatchUtils {
    public static native int applyPatch(String str, String str2, String str3);

    static {
        System.loadLibrary("PatchLibrary");
    }
}
