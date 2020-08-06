package com.ta.utdid2.device;

import android.content.Context;
import com.ta.audid.Constants;
import com.ta.audid.Variables;
import com.ta.audid.device.AppUtdid;
import com.ta.utdid2.android.utils.StringUtils;

public class UTDevice {
    @Deprecated
    public static String getUtdid(Context context) {
        if (context == null) {
            return Constants.UTDID_INVALID;
        }
        Variables.getInstance().initContext(context);
        if (Variables.getInstance().getOldMode()) {
            return getUtdidOld(context);
        }
        Variables.getInstance().init();
        return AppUtdid.getInstance().getUtdid();
    }

    @Deprecated
    public static String getUtdidForUpdate(Context context) {
        if (context == null) {
            return Constants.UTDID_INVALID;
        }
        Variables.getInstance().initContext(context);
        if (Variables.getInstance().getOldMode()) {
            return getUtdidForUpdateOld(context);
        }
        Variables.getInstance().init();
        return AppUtdid.getInstance().getUtdidFromFile();
    }

    private static String getUtdidOld(Context context) {
        Device device = DeviceInfo.getDevice(context);
        if (device == null || StringUtils.isEmpty(device.getUtdid())) {
            return Constants.UTDID_INVALID;
        }
        return device.getUtdid();
    }

    private static String getUtdidForUpdateOld(Context context) {
        String utdid = UTUtdid.instance(context).getValueForUpdate();
        return (utdid == null || StringUtils.isEmpty(utdid)) ? Constants.UTDID_INVALID : utdid;
    }
}
