package com.yunos.tvtaobao.blitz;

import android.content.Context;
import android.os.Build;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.listener.BzJsCallBaseListener;
import com.yunos.tv.core.common.DeviceJudge;
import com.zhiping.dev.android.logger.ZpLogger;

public class TaobaoBaseBzJsCallBaseListener implements BzJsCallBaseListener {
    private final String TAG = "TaobaoBaseBzJsCallBaseListener";

    public String onBaseGetDeviceInfo(Context context, String param) {
        ZpLogger.i("TaobaoBaseBzJsCallBaseListener", "onBaseGetDeviceInfo , param  = " + param);
        BzResult result = new BzResult();
        result.addData("uuid", CloudUUIDWrapper.getCloudUUID());
        result.addData("model", Build.MODEL);
        result.addData("totalMemory", DeviceJudge.getTotalMemorySizeInMB());
        result.setSuccess();
        String rtn = result.toJsonString();
        ZpLogger.i("TaobaoBaseBzJsCallBaseListener", "onBaseGetDeviceInfo , rtn   = " + rtn);
        return rtn;
    }
}
