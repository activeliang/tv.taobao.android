package com.yunos.tvtaobao.blitz;

import com.yunos.tv.blitz.listener.BzAppGlobalListener;
import com.zhiping.dev.android.logger.ZpLogger;

public class BzAccountListener implements BzAppGlobalListener {
    private String TAG = "BzAccountListener";

    public void onAccountUpdate(String arg0, String arg1) {
        ZpLogger.v(this.TAG, this.TAG + ".onRequestDetainMent --> onAccountUpdate --> arg0 = " + arg0 + "; arg1 = " + arg1);
    }
}
