package com.ali.user.open.ucc.util;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.WUAData;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.util.SystemUtils;
import com.ali.user.open.ucc.data.ApiConstants;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

public class RiskControlInfoContext {
    public static String buildRiskControlInfo() {
        JSONObject jsonObject = new JSONObject();
        StorageService storageService = (StorageService) AliMemberSDK.getService(StorageService.class);
        WUAData wua = storageService.getWUA();
        if (wua != null) {
            jsonObject.put("wua", (Object) wua.wua);
            jsonObject.put("t", (Object) wua.t);
        }
        jsonObject.put("umidToken", (Object) storageService.getUmid());
        Context context = KernelContext.getApplicationContext();
        jsonObject.put("osName", (Object) DispatchConstants.ANDROID);
        jsonObject.put("osVersion", (Object) Build.VERSION.RELEASE);
        jsonObject.put("mac", (Object) SystemUtils.getDeviceMac());
        jsonObject.put("deviceModel", (Object) Build.MODEL);
        jsonObject.put(ApiConstants.ApiField.DEVICE_NAME, (Object) Build.MODEL);
        double[] location = SystemUtils.getLocation(context);
        if (location != null && location.length >= 2) {
            jsonObject.put(ClientTraceData.b.f54c, (Object) Double.valueOf(location[1]));
            jsonObject.put(ClientTraceData.b.d, (Object) Double.valueOf(location[0]));
        }
        jsonObject.put("imei", (Object) SystemUtils.getImei(context));
        jsonObject.put("deviceBrand", (Object) Build.MANUFACTURER);
        DisplayMetrics metric = new DisplayMetrics();
        context.getResources().getDisplayMetrics();
        int width = metric.widthPixels;
        jsonObject.put(VPMConstants.DIMENSION_SCREENSIZE, (Object) width + "x" + metric.heightPixels);
        jsonObject.put("appStore", (Object) AliMemberSDK.ttid);
        jsonObject.put("ssid", (Object) SystemUtils.getSSID(context));
        jsonObject.put(DispatchConstants.BSSID, (Object) SystemUtils.getBSSID(context));
        return jsonObject.toJSONString();
    }
}
