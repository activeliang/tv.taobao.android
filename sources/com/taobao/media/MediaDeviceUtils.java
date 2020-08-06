package com.taobao.media;

import android.os.Build;
import android.text.TextUtils;
import com.taobao.taobaoavsdk.util.AndroidUtils;

public class MediaDeviceUtils {
    private static float mCoreNums = 0.0f;
    private static float mMaxCpuFreq = 0.0f;
    private static boolean sSupportH256Goted = false;

    public static boolean isSupportH265(String maxFreq) {
        try {
            if (TextUtils.isEmpty(maxFreq)) {
                return false;
            }
            float maxTargetFreq = Float.parseFloat(maxFreq);
            if (maxTargetFreq < 1.2f) {
                maxTargetFreq = 1.8f;
            }
            if (!sSupportH256Goted) {
                return false;
            }
            if (mCoreNums >= 6.0f || (mMaxCpuFreq > maxTargetFreq && maxTargetFreq >= 1.2f)) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public static void getCpuMaxFreq() {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                mCoreNums = (float) AndroidUtils.getNumCores();
                if (mCoreNums >= 4.0f) {
                    float maxCpuFreq = 0.0f;
                    for (int i = 0; ((float) i) < mCoreNums; i++) {
                        float cpuFreq = AndroidUtils.parseFloat(AndroidUtils.getMaxCpuFreq(i)) / 1000000.0f;
                        if (cpuFreq > maxCpuFreq) {
                            maxCpuFreq = cpuFreq;
                        }
                    }
                    mMaxCpuFreq = maxCpuFreq;
                }
            }
        } catch (Throwable th) {
            mCoreNums = 0.0f;
            mMaxCpuFreq = 0.0f;
        } finally {
            sSupportH256Goted = true;
        }
    }
}
