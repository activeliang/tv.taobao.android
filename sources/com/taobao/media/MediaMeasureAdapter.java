package com.taobao.media;

import android.os.Build;
import android.util.Log;
import anet.channel.monitor.BandWidthSampler;
import com.ali.alihadeviceevaluator.AliHAHardware;
import com.taobao.mediaplay.MediaPlayControlContext;
import com.taobao.mediaplay.common.IMediaMeasureAdapter;
import com.taobao.taobaoavsdk.util.DWLogUtils;

public class MediaMeasureAdapter implements IMediaMeasureAdapter {
    private int mDeviceMeausreResult = -1;
    private int mLastMeausreResult = -1;
    private long mLastMeausreTime;

    public int getNetSpeedValue() {
        try {
            return ((int) BandWidthSampler.getInstance().getNetSpeedValue()) * 8;
        } catch (Throwable e) {
            DWLogUtils.e("MediaMeasureAdapter", " MediaMeasureAdapter getNetSpeedValue error:" + e.getMessage());
            return Integer.MAX_VALUE;
        }
    }

    public boolean isLowPerformance(MediaPlayControlContext context) {
        try {
            if (Build.VERSION.SDK_INT > 27) {
                return false;
            }
            if (System.currentTimeMillis() - this.mLastMeausreTime >= 7000 || this.mLastMeausreResult < 0) {
                this.mLastMeausreTime = System.currentTimeMillis();
                AliHAHardware.OutlineInfo outlineInfo = AliHAHardware.getInstance().getOutlineInfo();
                int i = outlineInfo.runtimeLevel;
                context.mRuntimeLevel = i;
                this.mLastMeausreResult = i;
                this.mDeviceMeausreResult = outlineInfo.deviceLevel;
            } else {
                context.mRuntimeLevel = this.mLastMeausreResult;
            }
            if (context.mRuntimeLevel > 2) {
                return true;
            }
            return false;
        } catch (Throwable e) {
            context.mRuntimeLevel = 1;
            Log.d("MediaMeasureAdapter", " MediaMeasureAdapter isLowPerformance error:" + e.getMessage());
            return false;
        }
    }
}
