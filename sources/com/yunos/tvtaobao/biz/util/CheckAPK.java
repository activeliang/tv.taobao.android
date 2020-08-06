package com.yunos.tvtaobao.biz.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashSet;

public class CheckAPK {
    private static final String TAG = "CheckAPK";

    public static boolean checkAPKFile(Context context, String path, String versionCode) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            ZpLogger.e(TAG, "CheckAPK.checkAPKFile.cannot get PackageManager object");
            return false;
        }
        PackageInfo info = pm.getPackageArchiveInfo(path, 64);
        if (info == null) {
            ZpLogger.e(TAG, "CheckAPK.checkAPKFile.cannot get PackageInfo object");
            return false;
        }
        ZpLogger.d(TAG, "CheckAPK.checkAPKFile.apk packageName: " + info.packageName + " apk versionCode: " + info.versionCode + " apk versionName: " + info.versionName);
        if (!context.getPackageName().equalsIgnoreCase(info.packageName)) {
            ZpLogger.e(TAG, "CheckAPK.checkAPKFile.packageName mismatch, apk packageName: " + info.packageName + " app packageName: " + context.getPackageName());
            return false;
        } else if (!versionCode.equalsIgnoreCase(String.valueOf(info.versionCode))) {
            ZpLogger.e(TAG, "CheckAPK.checkAPKFile.versionCode mismatch between apk and server, apk versionCode: " + info.versionCode + " server versionCode: " + versionCode);
            return false;
        } else {
            try {
                PackageInfo myInfo = pm.getPackageInfo(context.getPackageName(), 64);
                if (myInfo.versionCode < info.versionCode) {
                    return isSignatureSame(info.signatures, myInfo.signatures);
                }
                ZpLogger.e(TAG, "CheckAPK.checkAPKFile.apk versionCode is downgraded, apk versionCode: " + info.versionCode + " app versionCode: " + myInfo.versionCode);
                return false;
            } catch (PackageManager.NameNotFoundException e) {
                ZpLogger.e(TAG, "CheckAPK.checkAPKFile.cannot get local package info: " + e.getLocalizedMessage());
                return false;
            }
        }
    }

    private static boolean isSignatureSame(Signature[] s1, Signature[] s2) {
        if (s1 == null || s2 == null) {
            ZpLogger.d(TAG, "at least one signature is null");
            return false;
        }
        HashSet<Signature> set1 = new HashSet<>();
        for (Signature sig : s1) {
            set1.add(sig);
        }
        HashSet<Signature> set2 = new HashSet<>();
        for (Signature sig2 : s2) {
            set2.add(sig2);
        }
        if (set1.equals(set2)) {
            ZpLogger.d(TAG, "signature is same");
            return true;
        }
        ZpLogger.e(TAG, "signature is not consistent");
        return false;
    }
}
