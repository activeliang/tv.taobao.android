package com.yunos.tv.blitz.global;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;
import com.yunos.tv.blitz.packagemanager.BzPackageManager;

public class BzAppJniContext {
    static final String TAG = BzAppJniContext.class.getSimpleName();

    private native boolean nativeInitAppJniContext();

    public native void nativeClearBackgroundImg(int i);

    public native String nativeGetVersionInfo();

    public native boolean nativeReadFromAssets(AssetManager assetManager);

    public native void nativeSetBackgroundImgFromAssets(String str, int i);

    public native boolean nativeSetMtopDomain(String str);

    public boolean initAppJniContext() {
        SharedPreferences.Editor editor = BzAppConfig.context.getContext().getSharedPreferences("blitz_version", 0).edit();
        editor.putString("version", nativeGetVersionInfo());
        editor.apply();
        if (nativeInitAppJniContext()) {
            return true;
        }
        Log.e(TAG, "init app jni context fail!!");
        return false;
    }

    public void throwNativeSignalCrash(int signal) {
        Log.e(TAG, "throwNativeSignalCrash signal:" + signal);
    }

    public void notifyUiBlocked(long coreIndex, String url, String pageData) {
        Log.e(TAG, "notifyUiBlocked: index=" + coreIndex + "; url=" + url + "; pageData=" + pageData);
    }

    public byte[] getAppIcon(String packageName) {
        return BzPackageManager.getAppIcon(packageName);
    }
}
