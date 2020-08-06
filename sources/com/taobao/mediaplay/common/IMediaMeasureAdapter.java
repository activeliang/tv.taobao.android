package com.taobao.mediaplay.common;

import com.taobao.mediaplay.MediaPlayControlContext;

public interface IMediaMeasureAdapter {
    int getNetSpeedValue();

    boolean isLowPerformance(MediaPlayControlContext mediaPlayControlContext);
}
