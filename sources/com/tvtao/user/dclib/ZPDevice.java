package com.tvtao.user.dclib;

import android.content.Context;
import com.tvtao.user.dclib.impl.ZPDeviceImpl;
import java.util.Map;

public final class ZPDevice {
    public static void init(Context context, Map<String, String> extParams) {
        ZPDeviceImpl.init(context, extParams, ZPDeviceImpl.SDKScene.OPNE);
    }

    public static void init(Context context, Map<String, String> extParams, ZPDeviceImpl.SDKScene scene) {
        ZPDeviceImpl.init(context, extParams, scene);
    }

    public static String getZpDid(Context context) {
        return ZPDeviceImpl.getZpDid(context);
    }

    public static String getAugurZpId(Context context) {
        return ZPDeviceImpl.getUserId(context);
    }
}
