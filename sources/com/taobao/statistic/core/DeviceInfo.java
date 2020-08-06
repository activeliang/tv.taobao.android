package com.taobao.statistic.core;

import android.content.Context;
import com.alibaba.analytics.utils.PhoneInfoUtils;
import com.ut.device.UTDevice;

public class DeviceInfo {
    private static Device s_device = null;

    @Deprecated
    public static Device getDevice(Context context) {
        if (s_device != null) {
            return s_device;
        }
        Device lDevice = new Device();
        lDevice.setImei(PhoneInfoUtils.getImei(context));
        lDevice.setImsi(PhoneInfoUtils.getImsi(context));
        lDevice.setUdid(UTDevice.getUtdid(context));
        s_device = lDevice;
        return s_device;
    }
}
