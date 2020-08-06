package com.tvtaobao.android.marketgames.dfw;

import android.os.Handler;

public class DfwHandler extends Handler {
    public static final int MSG_PLAY_CHANCE_UPDATE = 1;

    public DfwHandler(Handler.Callback callback) {
        super(callback);
    }
}
