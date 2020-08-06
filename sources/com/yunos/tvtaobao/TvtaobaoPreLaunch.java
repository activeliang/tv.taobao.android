package com.yunos.tvtaobao;

import android.content.Context;
import android.taobao.atlas.runtime.AtlasPreLauncher;
import com.zhiping.dev.android.logger.ZpLogger;

public class TvtaobaoPreLaunch implements AtlasPreLauncher {
    public void initBeforeAtlas(Context context) {
        ZpLogger.d("prelaunch", "prelaunch invokded");
    }
}
