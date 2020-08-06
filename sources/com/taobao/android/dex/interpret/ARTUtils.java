package com.taobao.android.dex.interpret;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

@TargetApi(21)
public class ARTUtils {
    private static final String TAG = "ARTUtils";
    private static boolean sInit = false;

    private static native boolean IsVerificationEnabledNative();

    private static native boolean abortNative();

    private static native boolean isDex2oatEnabledNative();

    private static native boolean nativeInit(boolean z, int i);

    private static native boolean setIsDex2oatEnabledNative(boolean z);

    private static native boolean setSignalCatcherHaltFlagNative(boolean z);

    private static native boolean setVerificationEnabledNative(boolean z);

    public static boolean init(Context context) {
        return init(context, false);
    }

    public static boolean init(Context context, boolean hookedJavaVM) {
        try {
            System.loadLibrary("dexinterpret");
            nativeInit(hookedJavaVM, context.getApplicationInfo().targetSdkVersion);
            sInit = true;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Couldn't initialize.", e);
        } catch (NoSuchMethodError e2) {
            Log.e(TAG, "Couldn't initialize.", e2);
        } catch (Throwable e3) {
            Log.e(TAG, "Couldn't initialize.", e3);
        }
        return sInit;
    }

    public static boolean isInit() {
        return sInit;
    }

    @Nullable
    public static Boolean setIsDex2oatEnabled(boolean enabled) {
        if (!sInit) {
            return null;
        }
        return Boolean.valueOf(setIsDex2oatEnabledNative(enabled));
    }

    @Nullable
    public static Boolean isDex2oatEnabled() {
        if (!sInit) {
            return null;
        }
        return Boolean.valueOf(isDex2oatEnabledNative());
    }

    @Nullable
    public static Boolean setVerificationEnabled(boolean enabled) {
        if (!sInit) {
            return null;
        }
        boolean success = setVerificationEnabledNative(enabled);
        if (success && enabled) {
            setSignalCatcherHaltFlag(false);
        } else if (success && !enabled) {
            setSignalCatcherHaltFlag(true);
        }
        return Boolean.valueOf(success);
    }

    @Nullable
    public static Boolean IsVerificationEnabled() {
        if (!sInit) {
            return null;
        }
        return Boolean.valueOf(IsVerificationEnabledNative());
    }

    @Nullable
    public static Boolean setSignalCatcherHaltFlag(boolean enabled) {
        if (!sInit) {
            return null;
        }
        return Boolean.valueOf(setSignalCatcherHaltFlagNative(enabled));
    }

    @Nullable
    public static Boolean abort() {
        if (!sInit) {
            return null;
        }
        return Boolean.valueOf(abortNative());
    }
}
