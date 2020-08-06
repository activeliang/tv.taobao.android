package com.yunos.tv.blitz.listener.internal;

import android.content.Context;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.utils.NetworkUtil;

public class BzJsCallImpNetListener implements BzJsCallNetListener {
    public String onIsNetworkAvailable(Context context, String param) {
        boolean isAvailable = NetworkUtil.isNetworkAvailable();
        BzResult result = new BzResult();
        result.addData("result", isAvailable);
        result.setSuccess();
        return result.toJsonString();
    }
}
