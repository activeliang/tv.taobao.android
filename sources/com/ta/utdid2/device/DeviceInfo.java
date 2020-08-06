package com.ta.utdid2.device;

import android.content.Context;
import com.ta.utdid2.android.utils.PhoneInfoUtils;
import com.ta.utdid2.android.utils.StringUtils;
import java.util.zip.Adler32;

public class DeviceInfo {
    static final Object CREATE_DEVICE_METADATA_LOCK = new Object();
    private static Device mDevice = null;

    static long getMetadataCheckSum(Device device) {
        if (device != null) {
            String checkSumContent = String.format("%s%s%s%s%s", new Object[]{device.getUtdid(), device.getDeviceId(), Long.valueOf(device.getCreateTimestamp()), device.getImsi(), device.getImei()});
            if (!StringUtils.isEmpty(checkSumContent)) {
                Adler32 adler32 = new Adler32();
                adler32.reset();
                adler32.update(checkSumContent.getBytes());
                return adler32.getValue();
            }
        }
        return 0;
    }

    private static Device initDeviceMetadata(Context aContext) {
        if (aContext != null) {
            synchronized (CREATE_DEVICE_METADATA_LOCK) {
                String utdid = UTUtdid.instance(aContext).getValue();
                if (!StringUtils.isEmpty(utdid)) {
                    if (utdid.endsWith("\n")) {
                        utdid = utdid.substring(0, utdid.length() - 1);
                    }
                    Device device = new Device();
                    long timestamp = System.currentTimeMillis();
                    String imei = PhoneInfoUtils.getImei(aContext);
                    String imsi = PhoneInfoUtils.getImsi(aContext);
                    device.setDeviceId(imei);
                    device.setImei(imei);
                    device.setCreateTimestamp(timestamp);
                    device.setImsi(imsi);
                    device.setUtdid(utdid);
                    device.setCheckSum(getMetadataCheckSum(device));
                    return device;
                }
            }
        }
        return null;
    }

    public static synchronized Device getDevice(Context context) {
        Device device;
        synchronized (DeviceInfo.class) {
            if (mDevice != null) {
                device = mDevice;
            } else if (context != null) {
                device = initDeviceMetadata(context);
                mDevice = device;
            } else {
                device = null;
            }
        }
        return device;
    }
}
