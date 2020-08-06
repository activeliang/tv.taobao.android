package com.ali.auth.third.core.ut;

import android.os.Build;
import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.RpcRequest;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.core.util.SystemUtils;
import com.alibaba.analytics.core.Constants;
import java.util.Map;
import org.json.JSONObject;

public class UserTracer implements UserTrackerService {
    private static final String TAG = "login.UserTracer";

    public void init() {
    }

    public void send(String action, Map<String, String> props) {
        final JSONObject appInfo = getAppInfo();
        try {
            if (!TextUtils.isEmpty(action)) {
                appInfo.put("action", action);
            }
            if (props != null) {
                appInfo.put("actionExt", JSONUtils.toJsonObject(props));
            }
            new Thread(new Runnable() {
                public void run() {
                    RpcRequest request = new RpcRequest();
                    request.target = "mtop.taobao.havana.mlogin.userTracerLog";
                    request.version = "1.0";
                    request.addParam("userTracerInfo", appInfo);
                    request.addParam("ts", System.currentTimeMillis() + "");
                    RpcResponse<String> invoke = ((RpcService) KernelContext.getService(RpcService.class)).invoke(request, String.class);
                }
            }).start();
        } catch (Exception e) {
        }
    }

    public static JSONObject getAppInfo() {
        JSONObject all = new JSONObject();
        JSONObject deviceInfo = all;
        try {
            deviceInfo.put("diskSize", CommonUtils.getSDCardSize());
            deviceInfo.put("sysSize", CommonUtils.getSystemSize());
            deviceInfo.put("memorySize", CommonUtils.getTotalMemory());
            deviceInfo.put("deviceId", CommonUtils.getAndroidId());
            deviceInfo.put("uuid", KernelContext.UUID);
        } catch (Exception e) {
        }
        JSONObject osInfo = all;
        try {
            osInfo.put("osType", DispatchConstants.ANDROID);
            osInfo.put("osVersion", Build.VERSION.SDK_INT + "");
        } catch (Exception e2) {
        }
        JSONObject sdkInfo = all;
        try {
            sdkInfo.put("sdkName", "alibabauth_sdk");
            sdkInfo.put("sdkVersion", KernelContext.sdkVersion);
            if (KernelContext.isMini) {
                sdkInfo.put("sdkType", Constants.SDK_TYPE);
            } else {
                sdkInfo.put("sdkType", "std");
            }
        } catch (Exception e3) {
        }
        JSONObject jSONObject = all;
        try {
            all.put("appId", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            all.put("appKey", KernelContext.getAppKey());
            all.put("appName", CommonUtils.getAppLabel());
            all.put("appVersion", CommonUtils.getAndroidAppVersion());
        } catch (Exception e4) {
        }
        try {
            all.put("openId", CredentialManager.INSTANCE.getInternalSession().user.openId);
        } catch (Exception e5) {
        }
        return all;
    }
}
