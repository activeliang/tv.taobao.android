package com.yunos.tvtaobao.request;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tvtaobao.biz.request.bo.DeviceBo;
import com.yunos.tvtaobao.biz.request.item.TvTaobaoBusinessRequest;
import com.yunos.tvtaobao.payment.request.ScanBindRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public class DeviceChannelBuilder {
    private static final String TAG = "DeviceChannelBuilder";
    private TvTaobaoBusinessRequest mBusinessRequest = TvTaobaoBusinessRequest.getBusinessRequest();
    private Context mContext;

    public DeviceChannelBuilder(Context context) {
        this.mContext = context;
    }

    public void onRequestData() {
        String model = Build.MODEL;
        ZpLogger.d(TAG, " app model " + model);
        if (!TextUtils.isEmpty(model)) {
            this.mBusinessRequest.requestDevice(model, new DeviceBoListener(new WeakReference(this.mContext)));
        }
    }

    private static class DeviceBoListener implements RequestListener<DeviceBo> {
        private WeakReference<Context> ref;

        public DeviceBoListener(WeakReference<Context> service) {
            this.ref = service;
        }

        public void onRequestDone(DeviceBo data, int resultCode, String msg) {
            if (this.ref != null || this.ref.get() != null) {
                if (resultCode != 200) {
                    ZpLogger.d(DeviceChannelBuilder.TAG, " DeviceBoListener resultCode " + resultCode + " msg " + msg);
                    return;
                }
                ZpLogger.d(DeviceChannelBuilder.TAG, "DeviceBo " + data);
                if (data != null) {
                    ZpLogger.d(DeviceChannelBuilder.TAG, "DeviceBo " + data.toString());
                    if (!TextUtils.isEmpty(data.getAppKey())) {
                        SharePreferences.put("device_appkey", data.getAppKey());
                        ScanBindRequest.setAppKey(data.getAppKey());
                    }
                    if (!TextUtils.isEmpty(data.getBrandName())) {
                        SharePreferences.put("device_brandname", data.getBrandName());
                        return;
                    }
                    return;
                }
                ZpLogger.d(DeviceChannelBuilder.TAG, " DeviceBoListener onError data " + data + "msg" + " 获取设备请求数据出错");
            }
        }
    }
}
