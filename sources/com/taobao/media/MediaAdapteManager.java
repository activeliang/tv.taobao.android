package com.taobao.media;

import com.taobao.adapter.ABTestAdapter;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.adapter.INetworkUtilsAdapter;
import com.taobao.mediaplay.common.IMediaMeasureAdapter;

public class MediaAdapteManager {
    public static ABTestAdapter mABTestAdapter = new MediaABTestAdapter();
    public static ConfigAdapter mConfigAdapter = new MediaConfigAdapter();
    public static IMediaMeasureAdapter mMeasureAdapter = new MediaMeasureAdapter();
    public static INetworkUtilsAdapter mMediaNetworkUtilsAdapter = new MediaNetworkUtilsAdapter();
}
