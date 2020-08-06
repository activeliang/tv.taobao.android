package com.uc.browser.aerie;

import android.util.Log;

public class DalvikPatch {
    private static final String TAG = "DalvikPatch";

    private static native int adjustLinearAlloc();

    private static native void dumpLinearAlloc();

    private static native int getError();

    private static native int getMapAddr();

    private static native int getMapLength();

    private static native int getMapUsed();

    private static native boolean isDalvik();

    public static int patchIfPossible() {
        try {
            System.loadLibrary("dalvikpatch");
            if (isDalvik()) {
                int adjustLinearAlloc = adjustLinearAlloc();
                Log.d(TAG, "- patchIfPossible: adjustLinearAlloc=" + adjustLinearAlloc);
                return adjustLinearAlloc;
            }
        } catch (UnsatisfiedLinkError ingored) {
            Log.e(TAG, ingored.getMessage(), ingored);
        }
        return 0;
    }
}
